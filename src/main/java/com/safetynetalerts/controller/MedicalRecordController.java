package com.safetynetalerts.controller;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.service.MedicalRecordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("medical-record")
public class MedicalRecordController {

    private final MedicalRecordServiceImpl service;

    Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    public MedicalRecordController(MedicalRecordServiceImpl service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<MedicalRecordDto> createMedicalRecord(@RequestBody MedicalRecordDto pMedicalRecord) throws ResourceNotFoundException, ResourceAlreadyExistsException {
        logger.info("launch creation medical record");
        return new ResponseEntity<>(this.service.createMedicalRecord(pMedicalRecord), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecordDto>> getAllMedicalRecords() throws IOException {
        logger.info("launch retrieval of all medical records");
        return new ResponseEntity<>(this.service.getAllMedicalRecords(), HttpStatus.OK);
    }

    @GetMapping("/by-full-name")
    public ResponseEntity<MedicalRecordDto> getMedicalRecordByFullName(@RequestParam("first-name") String firstName, @RequestParam("last-name")String lastName) throws IOException, ResourceNotFoundException, BadResourceException {
        logger.info("launch retrieval of by-full-name medical record");
        return new ResponseEntity<>(this.service.getMedicalRecordByFullName(firstName, lastName), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MedicalRecordDto> updateMedicalRecord (@RequestBody MedicalRecordDto medicalRecordDto) throws ResourceNotFoundException, IOException, BadResourceException {
        logger.info("launch of update of a medical record");
        return new ResponseEntity<>(this.service.updateMedicalRecord(medicalRecordDto), HttpStatus.ACCEPTED);
    }


    @DeleteMapping
    public ResponseEntity deleteMedicalRecord(@RequestParam("first-name") String firstName, @RequestParam("last-name") String lastName) {
        logger.info("launch of deletion of a medical record");
        try {
            this.service.deleteMedicalRecordByFullName(firstName, lastName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadResourceException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }
}
