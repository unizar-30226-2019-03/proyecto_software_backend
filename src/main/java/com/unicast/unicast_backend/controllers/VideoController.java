/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.mp4.Mp4Directory;
import com.unicast.unicast_backend.assemblers.VideoResourceAssembler;
import com.unicast.unicast_backend.async.NotificationAsync;
import com.unicast.unicast_backend.exceptions.NotMp4Exception;
import com.unicast.unicast_backend.exceptions.NotUploaderException;
import com.unicast.unicast_backend.exceptions.ProfessorNotInSubjectException;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.rest.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.rest.VideoRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;
import com.unicast.unicast_backend.s3handlers.S3ImageHandler;
import com.unicast.unicast_backend.s3handlers.S3VideoHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/* 
 * Controlador REST para los videos
 */

@RestController
public class VideoController {

    // Repositorio para los videos
    @Autowired
    private VideoRepository videoRepository;

    // Repositorio para las asignaturas
    @Autowired
    private SubjectRepository subjectRepository;

    // Ensamblador
    @Autowired
    private VideoResourceAssembler videoAsssembler;

    // Rutina de control de videos
    @Autowired
    private S3VideoHandler s3VideoHandler;

    // Rutina de control de imagenes
    @Autowired
    private S3ImageHandler s3ImageHandler;

    // Notificaciones asincronas
    @Autowired
    private NotificationAsync notificationAsync;


    /*
     * Permite subir un video
     * Parametros
     * @param userAuth: token con los datos del usuario loggeado
     * @param videoFile: archivo multimedia a subir
     * @param thumbnail: miniatura del video
     * @param title: titulo del video
     * @param description: descripcion del contenido del video
     * @param subjectId: identificador de la asignatura a la que pertenece
     */
    @PostMapping(value = "/api/videos/upload", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('CREATE_VIDEO_PRIVILEGE')")
    public ResponseEntity<?> uploadVideo(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestPart("file") MultipartFile videoFile, @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestParam("title") String title, @RequestParam("description") String description,
            @RequestParam("subject_id") Long subjectId) throws ProfessorNotInSubjectException, IllegalStateException,
            IOException, URISyntaxException, ImageProcessingException, NotMp4Exception{
        
        // Extraccion de los datos del usuario
        User user = userAuth.getUser();
        Video video = new Video();

        // Obtencion de la asignatura del video a subir
        Subject subject = subjectRepository.findById(subjectId).get();

        // Comprobar que el usuario que sube el video pertenece a la asignatura
        if (!subject.getProfessors().contains(user)) {
            throw new ProfessorNotInSubjectException("El que sube video debe pertenecer a la asignatura del video");
        }

        // Asignacion de atributos al video
        Timestamp now = Timestamp.from(Instant.now());
        video.setTitle(title);
        video.setDescription(description);
        video.setSubject(subject);
        video.setUploader(user);
        video.setTimestamp(now);

        // Anyadir la URL del video, el archivo multimedia y la miniatura
        URI videoURL = s3VideoHandler.uploadFile(videoFile);
        URI thumbnailURL = s3ImageHandler.uploadFile(thumbnail);

        video.setUrl(videoURL);
        video.setThumbnailUrl(thumbnailURL);

        File tempFile = s3VideoHandler.getLastUploadedTmpFile();
        Metadata metadata = ImageMetadataReader.readMetadata(tempFile);

        Mp4Directory mp4Directory = metadata.getFirstDirectoryOfType(Mp4Directory.class);

        // Comprobar que el directorio de subida es correcto
        if (mp4Directory == null) {
            throw new NotMp4Exception("No existe el directorio mp4");
        }
        
        // Poner los segundos de duracion del video
        video.setSeconds(mp4Directory.getInteger(Mp4Directory.TAG_DURATION));
        s3VideoHandler.deleteLastUploadedTmpFile();
        s3ImageHandler.deleteLastUploadedTmpFile();

        // Guardar cambios en el repositorio
        videoRepository.saveInternal(video);

        // Crear notificacion de subida de video
        notificationAsync.createUserNotificationsVideo(subject.getId(), now);

        // Mandar respuesta de la operacion
        Resource<Video> resourceVideo = videoAsssembler.toResource(video);
        return ResponseEntity.created(new URI(resourceVideo.getId().expand().getHref())).body(resourceVideo);
    }

    
    /*
     * Permite subir un video
     * @param userAuth: token con los datos del usuario loggeado
     * @param videoId: identificador del video a borrar
     */
    @DeleteMapping(value = "/api/videos/delete", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('DELETE_VIDEO_PRIVILEGE')")
    public ResponseEntity<?> deleteVideo(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("id") Long videoId) throws NotUploaderException{
        // TODO: gestionar tags y errores

        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Encontrar video por id en el repositorio
        Video video = videoRepository.findById(videoId).get();

        // Comprobar que es propietario
        if (video.getUploader() != user) {
            throw new NotUploaderException("El usuario no el propietario del video");
        }

        // Borrar URL y miniatura
        s3VideoHandler.deleteFile(video.getUrl());
        s3ImageHandler.deleteFile(video.getThumbnailUrl());

        // Borrar video del repositorio
        videoRepository.delete(video);

        // Respuesta de la operacion
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
