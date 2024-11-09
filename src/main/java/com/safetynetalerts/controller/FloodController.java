package com.safetynetalerts.controller;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.service.PersonFirestationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/flood")
public class FloodController {

    private final PersonFirestationServiceImpl service;

    Logger logger = LoggerFactory.getLogger(FloodController.class);

    public FloodController(PersonFirestationServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/station")
    public ResponseEntity<List<PersonMedicalRecordDto>> getAllPersonsAndMedicalRecordByFirestation(@RequestParam("stations") List<String> stations) throws IOException, ResourceNotFoundException, BadResourceException {
        logger.info("get all persons and associated medical records by firestation");
        if (stations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
        List<PersonMedicalRecordDto> personMedicalRecordDtos = this.service.getPersonsAndMedicalRecordsByFirestation(stations);
        if (personMedicalRecordDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(personMedicalRecordDtos, HttpStatus.OK);
    }
}
