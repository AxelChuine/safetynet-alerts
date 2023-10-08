package com.safetynetalerts.controller;

import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.service.IMedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
public class MedicalRecordController {

    @Autowired
    private IMedicalRecordService service;

    @PostMapping("/medical-records")
    public ResponseEntity createMedicalRecord(@RequestBody MedicalRecordDto pMedicalRecord){
        this.service.createMedicalRecord(pMedicalRecord);
        if (Objects.isNull(pMedicalRecord)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/medical-records")
    public ResponseEntity<List<MedicalRecordDto>> getAllMedicalRecords() throws IOException {
        return ResponseEntity.ok(this.service.getAllMedicalRecords());
    }

    @PutMapping("/medical-record")
    public ResponseEntity updateMedicalRecord (@RequestParam("firstName") String pFirstName, @RequestParam("lastName") String pLastName, @RequestParam("allergie") String pAllergie) throws IOException {
        this.service.updateMedicalRecord(pFirstName, pLastName, pAllergie);
        if (Objects.isNull(this.service.getMedicalRecordByFullName(pFirstName, pLastName))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    @DeleteMapping("/medical-record")
    public ResponseEntity deleteMedicalRecord(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws IOException {
        this.service.deleteMedicalRecordByFullName(firstName, lastName);
        if (Objects.isNull(this.service.getMedicalRecordByFullName(firstName, lastName))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
