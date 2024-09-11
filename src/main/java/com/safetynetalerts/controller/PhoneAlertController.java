package com.safetynetalerts.controller;

import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.service.impl.PersonFirestationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PhoneAlertController {
    private final PersonFirestationServiceImpl personFirestationService;

    Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);

    public PhoneAlertController(PersonFirestationServiceImpl personFirestationService) {
        this.personFirestationService = personFirestationService;
    }

    @GetMapping("/phone-alert")
    public ResponseEntity<PhoneAlertDto> getCellNumbers(@RequestParam("station-number") String stationNumber) throws IOException {
        logger.info("get cell numbers by station number");
        return new ResponseEntity<>(this.personFirestationService.getCellNumbers(stationNumber), HttpStatus.OK);
    }
}
