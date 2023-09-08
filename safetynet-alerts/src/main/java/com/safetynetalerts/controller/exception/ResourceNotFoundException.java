package com.safetynetalerts.controller.exception;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Data
public class ResourceNotFoundException extends Exception{

    private String message;

    private HttpStatus status;

    public ResourceNotFoundException (String pResources) {
        this.message = pResources;
    }
}
