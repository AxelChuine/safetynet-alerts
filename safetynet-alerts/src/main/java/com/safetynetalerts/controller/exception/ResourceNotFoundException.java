package com.safetynetalerts.controller.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceNotFoundException extends Exception{

    private String resources;

    private HttpStatus status;

}
