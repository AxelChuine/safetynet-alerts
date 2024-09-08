package com.safetynetalerts.controller.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResourceAlreadyExistsException extends Exception {
    private String message;
    private HttpStatus status;

    public ResourceAlreadyExistsException(String message) {
        this.message = message;
        this.status = HttpStatus.CONFLICT;
    }
}


