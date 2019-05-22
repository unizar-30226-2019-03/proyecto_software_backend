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


import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.net.URISyntaxException;

/*
 * Controlador general de todas las excepciones manejadas
 */
@ControllerAdvice
public class ErrorHandler {

    /*
     * Esta peticion se ejecuta cuando el cliente realiza un request al servidor
     * y alguno de los parametros esta mal pasado
     * Retorna como codigo de error 800
     */
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<ErrorInfo> badArgumentException(HttpServletRequest request, java.util.NoSuchElementException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta peticion se ejecuta cuando el cliente realiza un request al servidor
     * y alguno de los parametros no es de un tipo valido
     * Retorna como codigo de error 801
     */
    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<ErrorInfo> methodArgumentNotValidException(HttpServletRequest request, URISyntaxException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),"hola", request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }


    /*
     * Esta excepcion gestiona violaciones de claves o fallos en la creacion de
     * emails de los usuarios en la base de datos
     * Retorna codigo de error 802
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorInfo> constraintFail(HttpServletRequest request, javax.validation.ConstraintViolationException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),"Fallo de las constantes en la base o en mail no esta bien construido",request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }


    /*
     * Esta excepcion controla la violacion de atributos o campos que tienen la
     * restriccion de unique en la base de datos
     * Retorna codigo de error 803
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> uniqueConstraintDuplicated(HttpServletRequest request,org.springframework.dao.DataIntegrityViolationException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),"Constante unica duplicada",request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }


    /*
     * Esta excepcion salta cuando un usuario quiere realizar un comentario en
     * un instante de video que es mayor que su duracion
     * Responde con codigo de error 804
     */
    @ExceptionHandler(GreaterSecondsException.class)
    public ResponseEntity<ErrorInfo> greaterSeconds(HttpServletRequest request,GreaterSecondsException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }


    /*
     * Esta excepcion se ejecuta cuando un usuario profesor quiere subir un
     * video de una asignatura y no se encuentra impartiendo dicha asignatura
     * Retorna codigo de error 805
     */
    @ExceptionHandler(ProfessorNotInSubjectException.class)
    public ResponseEntity<ErrorInfo> professorNotInSubject(HttpServletRequest request,ProfessorNotInSubjectException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }


    /*
     * Esta excepcion salta cuando el usuario desea subir un video y dicho
     * usuario no es el propietario de el video que se desea subir
     * Retorna codigo de error 806
     */
    @ExceptionHandler(NotUploaderException.class)
    public ResponseEntity<ErrorInfo> notUploader(HttpServletRequest request,NotUploaderException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion salta cuando se desea subir un video que no tiene como
     * extension mp4
     * Retorna codigo de error 807
     */
    @ExceptionHandler( com.drew.imaging.ImageProcessingException.class)
    public ResponseEntity<ErrorInfo> notMp4(HttpServletRequest request, com.drew.imaging.ImageProcessingException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),"No es mp4",request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion controla que un usuario que no sea profesor puedar dar
     * permisos de profesor a otros usuarios
     * Devuelve codigo de error 808
     */
    @ExceptionHandler(NotProfessorException.class)
    public ResponseEntity<ErrorInfo> notProfessor(HttpServletRequest request, NotProfessorException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion gestiona que un metodo ha sido invocado de forma erronea o
     * inadecuada
     * Retorno de error de codigo 809
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorInfo> ilegalState(HttpServletRequest request, IllegalStateException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion gestiona que la lista de reproduccion que se desea borrar
     * nunca pueda ser una que esta marcada como favoritos
     * Devuelve error 810
     */
    @ExceptionHandler(FavouriteListException.class)
    public ResponseEntity<ErrorInfo> favouriteReproductionList (HttpServletRequest request, FavouriteListException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion controla que un usuario no pueda borrar una lista de
     * reproduccion de la que no es propietario
     * DEvuelve como codigo de error 811
     */
    @ExceptionHandler(NotOwnerReproductionList.class)
    public ResponseEntity<ErrorInfo> notOwnerReproList (HttpServletRequest request, NotOwnerReproductionList e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    /*
     * Esta excepcion se usa para controlar que el directorio donde se van a
     * almacenar los archivos multimedia de extension mp4 no este especificado
     * Devuelve codiog de error 812
     */
    @ExceptionHandler(NotMp4Exception.class)
    public ResponseEntity<ErrorInfo> notMp4Dir (HttpServletRequest request, NotMp4Exception e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }


    /*
     * Esta excepcion se usa para controlar que los usuarios que son administradores no puedan 
     * mandar mensajes a otros usuarios
     * Devuelve codiog de error 812
     */
    @ExceptionHandler(NotMp4Exception.class)
    public ResponseEntity<ErrorInfo> notAdminSender (HttpServletRequest request, NotAdminSenderException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }


    /*
     * Esta excepcion se usa para controlar que solo los usuarios profesores puedan recibir correos 
     * de otros usuarios
     * Devuelve codiog de error 813
     */
    @ExceptionHandler(NotMp4Exception.class)
    public ResponseEntity<ErrorInfo> notProfessorReceiver (HttpServletRequest request, NotProfessorReceiver e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }



    // Codigo de error 808
    // Excepcion de usuario con credenciales identicas -- USER CONTROLLER

    // @ExceptionHandler(java.util.NoSuchElementException.class)
    // public ResponseEntity<ErrorInfo> favouriteReproductionList (HttpServletRequest request, java.util.NoSuchElementException e) {
    //     ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
    //     return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    // }


    // Codigo de error 810
    // Destinatario es profesor de una de las asignaturas del usuario MESSAGE CONTROLLER

    // @ExceptionHandler(java.util.NoSuchElementException.class)
    // public ResponseEntity<ErrorInfo> favouriteReproductionList (HttpServletRequest request, java.util.NoSuchElementException e) {
    //     ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
    //     return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    // }
}
