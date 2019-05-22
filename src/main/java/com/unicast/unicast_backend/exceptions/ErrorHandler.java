package com.unicast.unicast_backend.exceptions;


import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.net.URISyntaxException;

@ControllerAdvice
public class ErrorHandler {

    // Codigo de error 800
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<ErrorInfo> badArgumentException(HttpServletRequest request, java.util.NoSuchElementException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    // Codigo de error 801
    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<ErrorInfo> methodArgumentNotValidException(HttpServletRequest request, URISyntaxException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),"hola", request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    // Codigo de error 802
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorInfo> constraintFail(HttpServletRequest request, javax.validation.ConstraintViolationException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),"Fallo de las constantes en la base o en mail no esta bien construido",request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
    
    // Codigo de error 803
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> uniqueConstraintDuplicated(HttpServletRequest request,org.springframework.dao.DataIntegrityViolationException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),"Constante unica duplicada",request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GreaterSecondsException.class)
    public ResponseEntity<ErrorInfo> greaterSeconds(HttpServletRequest request,GreaterSecondsException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
    

    @ExceptionHandler(ProfessorNotInSubjectException.class)
    public ResponseEntity<ErrorInfo> professorNotInSubject(HttpServletRequest request,ProfessorNotInSubjectException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotUploaderException.class)
    public ResponseEntity<ErrorInfo> notUploader(HttpServletRequest request,NotUploaderException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler( com.drew.imaging.ImageProcessingException.class)
    public ResponseEntity<ErrorInfo> notMp4(HttpServletRequest request, com.drew.imaging.ImageProcessingException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),"No es mp4",request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotProfessorException.class)
    public ResponseEntity<ErrorInfo> notProfessor(HttpServletRequest request, NotProfessorException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorInfo> ilegalState(HttpServletRequest request, IllegalStateException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(FavouriteListException.class)
    public ResponseEntity<ErrorInfo> favouriteReproductionList (HttpServletRequest request, FavouriteListException e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotOwnerReproductionList.class)
    public ResponseEntity<ErrorInfo> notOwnerReproList (HttpServletRequest request, NotOwnerReproductionList e) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotMp4Exception.class)
    public ResponseEntity<ErrorInfo> notMp4Dir (HttpServletRequest request, NotMp4Exception e) {
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