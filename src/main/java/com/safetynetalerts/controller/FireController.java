package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireDto;
import com.safetynetalerts.service.PersonMedicalRecordsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/fire")
public class FireController {

    private final PersonMedicalRecordsServiceImpl service;

    Logger logger = LoggerFactory.getLogger(FireController.class);

    public FireController(PersonMedicalRecordsServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<FireDto> getAllPersonsAndTheirInfosByAddress(@RequestParam("address") String address) throws IOException, ResourceNotFoundException {
        logger.info("get all persons and associated medical records by address");
        return new ResponseEntity<>(this.service.getAllConcernedPersonsAndTheirInfosByFire(address), HttpStatus.OK);
    }
}
