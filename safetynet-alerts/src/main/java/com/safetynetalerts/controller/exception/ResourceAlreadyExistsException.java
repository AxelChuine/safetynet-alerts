package com.safetynetalerts.controller.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Data
public class ResourceAlreadyExistsException extends Exception {
    private String message;
    private HttpStatus status;

    public ResourceAlreadyExistsException(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
