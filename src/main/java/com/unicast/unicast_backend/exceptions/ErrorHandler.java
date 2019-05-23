/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.exceptions;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import com.amazonaws.AmazonServiceException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
 * Controlador general de todas las excepciones manejadas
 */
@ControllerAdvice
public class ErrorHandler {

    /*
     * Esta peticion se ejecuta cuando el cliente realiza un request al servidor y
     * alguno de los parametros esta mal pasado
     */
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<ErrorInfo> badArgumentException(HttpServletRequest request,
            java.util.NoSuchElementException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta peticion se ejecuta cuando el cliente realiza un request al servidor y
     * alguno de los parametros no es de un tipo valido
     */
    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<ErrorInfo> methodArgumentNotValidException(HttpServletRequest request, URISyntaxException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), "Fallo en la Uri", request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion gestiona violaciones de constraints en la base de datos
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorInfo> constraintFail(HttpServletRequest request,
            javax.validation.ConstraintViolationException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Fallo de las constraints en la base de datos", request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
     * Esta excepcion controla la violacion de atributos o campos que tienen la
     * restriccion de unique en la base de datos
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> uniqueConstraintDuplicated(HttpServletRequest request,
            org.springframework.dao.DataIntegrityViolationException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.CONFLICT.value(), "Constante unica duplicada",
                request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    /*
     * Esta excepcion salta cuando un usuario quiere realizar un comentario en un
     * instante de video que es mayor que su duracion
     */
    @ExceptionHandler(GreaterSecondsException.class)
    public ResponseEntity<ErrorInfo> greaterSeconds(HttpServletRequest request, GreaterSecondsException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion se ejecuta cuando un usuario profesor quiere subir un video de
     * una asignatura y no se encuentra impartiendo dicha asignatura
     */
    @ExceptionHandler(ProfessorNotInSubjectException.class)
    public ResponseEntity<ErrorInfo> professorNotInSubject(HttpServletRequest request,
            ProfessorNotInSubjectException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion salta cuando el usuario desea subir un video y dicho usuario
     * no es el propietario de el video que se desea subir
     */
    @ExceptionHandler(NotUploaderException.class)
    public ResponseEntity<ErrorInfo> notUploader(HttpServletRequest request, NotUploaderException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion salta cuando se desea subir un video que no tiene como
     * extension mp4
     */
    @ExceptionHandler(com.drew.imaging.ImageProcessingException.class)
    public ResponseEntity<ErrorInfo> notMp4(HttpServletRequest request, com.drew.imaging.ImageProcessingException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "No es mp4",
                request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /*
     * Esta excepcion controla que un usuario que no sea profesor puedar dar
     * permisos de profesor a otros usuarios
     */
    @ExceptionHandler(NotProfessorException.class)
    public ResponseEntity<ErrorInfo> notProfessor(HttpServletRequest request, NotProfessorException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion gestiona que un metodo ha sido invocado de forma erronea o
     * inadecuada
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorInfo> illegalState(HttpServletRequest request, IllegalStateException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion gestiona que la lista de reproduccion que se desea borrar
     * nunca pueda ser una que esta marcada como favoritos
     */
    @ExceptionHandler(FavouriteListException.class)
    public ResponseEntity<ErrorInfo> favouriteReproductionList(HttpServletRequest request, FavouriteListException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion controla que un usuario no pueda borrar una lista de
     * reproduccion de la que no es propietario
     */
    @ExceptionHandler(NotOwnerReproductionList.class)
    public ResponseEntity<ErrorInfo> notOwnerReproList(HttpServletRequest request, NotOwnerReproductionList e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion se usa para controlar que el directorio donde se van a
     * almacenar los archivos multimedia de extension mp4 no este especificado
     */
    @ExceptionHandler(NotMp4Exception.class)
    public ResponseEntity<ErrorInfo> notMp4Dir(HttpServletRequest request, NotMp4Exception e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
     * Esta excepcion se usa para controlar que los usuarios que son administradores
     * no puedan mandar mensajes a otros usuarios
     */
    @ExceptionHandler(NotAdminSenderException.class)
    public ResponseEntity<ErrorInfo> notAdminSender(HttpServletRequest request, NotAdminSenderException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<ErrorInfo> amazonFail(HttpServletRequest request, AmazonServiceException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),"Fallo en amazon", request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotImageException.class)
    public ResponseEntity<ErrorInfo> notImage(HttpServletRequest request, NotImageException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorInfo> invalidToken(HttpServletRequest request, InvalidTokenException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

}
