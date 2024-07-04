package com.safetynetalerts.controller.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BadResourceException extends Exception {
    private HttpStatus status;
    private String message;

    public BadResourceException(String message) {
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
    }
}
