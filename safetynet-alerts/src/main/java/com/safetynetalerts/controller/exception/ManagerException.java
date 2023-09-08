package com.safetynetalerts.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = { "com.safetynetalerts" })
public class ManagerException {

    /**
     * Method managing the RessourceNotFoundException.
     *
     * @param e The exception
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleException(ResourceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage() + e.getMessage(), e.getStatus());
    }

    /**
     * Method managing the RessourceAlreadyExistException.
     *
     * @param e The exception
     *//*
        @ExceptionHandler(RessourceAlreadyExistException.class)
        public ResponseEntity<String> handleException(RessourceAlreadyExistException e) {

            logger.error("Error : ressource {} already exist", e.getRessource());

            return new ResponseEntity<>(e.getMessage() + e.getRessource(), e.getHttpStatus());
        }

        *//**
     * Method managing the InternalServerErrorException.
     *
     * @param e The exception
     *//*
        @ExceptionHandler(InternalServerErrorException.class)
        public ResponseEntity<String> handleException(InternalServerErrorException e) {

            logger.error("Error during the operation");

            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }*/

}

