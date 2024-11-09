package com.safetynetalerts.exception;

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
    @ExceptionHandler({ResourceNotFoundException.class, ResourceAlreadyExistsException.class, BadResourceException.class})
    public ResponseEntity<String> handleException(ResourceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage() + e.getMessage(), e.getStatus());
    }

}

