package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.service.IMedicalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController("medical-record")
@Slf4j
public class MedicalRecordController {

    private final IMedicalRecordService service;

    Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    public MedicalRecordController(IMedicalRecordService service) {
        this.service = service;
    }

    @PostMapping("/medical-record")
    public ResponseEntity<MedicalRecordDto> createMedicalRecord(@RequestBody MedicalRecordDto pMedicalRecord) throws ResourceNotFoundException, ResourceAlreadyExistsException {
        logger.info("launch creation medical record");
        return new ResponseEntity<>(this.service.createMedicalRecord(pMedicalRecord), HttpStatus.CREATED);
    }

    @GetMapping("/medical-records")
    public ResponseEntity<List<MedicalRecordDto>> getAllMedicalRecords() throws IOException {
        logger.info("launch retrieval of all medical records");
        return ResponseEntity.ok(this.service.getAllMedicalRecords());
    }

    @GetMapping("/by-full-name")
    public ResponseEntity<MedicalRecordDto> getMedicalRecordByFullName(@RequestParam("first-name") String firstName, @RequestParam("last-name")String lastName) throws IOException, ResourceNotFoundException {
        logger.info("launch retrieval of by-full-name medical record");
        return new ResponseEntity<>(this.service.getMedicalRecordByFullName(firstName, lastName), HttpStatus.OK);
    }

    @PutMapping("/medical-record")
    public ResponseEntity<MedicalRecordDto> updateMedicalRecord (@RequestBody MedicalRecordDto medicalRecordDto) throws ResourceNotFoundException, IOException {
        logger.info("launch of update of a medical record");
        return new ResponseEntity<>(this.service.updateMedicalRecord(medicalRecordDto), HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/medical-record")
    public ResponseEntity deleteMedicalRecord(@RequestParam("first-name") String firstName, @RequestParam("last-name") String lastName) throws IOException, ResourceNotFoundException {
        logger.info("launch of deletion of a medical record");
        this.service.deleteMedicalRecordByFullName(firstName, lastName);
        if (Objects.isNull(this.service.getMedicalRecordByFullName(firstName, lastName))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
