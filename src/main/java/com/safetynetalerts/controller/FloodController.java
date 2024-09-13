package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.service.impl.PersonFirestationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<List<PersonMedicalRecordDto>> getAllPersonsAndMedicalRecordByFirestation(@RequestParam("stations") List<String> stations) throws IOException, ResourceNotFoundException {
        logger.info("get all persons and associated medical records by firestation");
        return ResponseEntity.ok(this.service.getPersonsAndMedicalRecordsByFirestation(stations));
    }
}
