package com.safetynetalerts.controller;

import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.exception.BadResourceException;
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

@RestController
@RequestMapping("/phone-alert")
public class PhoneAlertController {
    private final PersonFirestationServiceImpl personFirestationService;

    Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);

    public PhoneAlertController(PersonFirestationServiceImpl personFirestationService) {
        this.personFirestationService = personFirestationService;
    }

    @GetMapping
    public ResponseEntity<PhoneAlertDto> getCellNumbers(@RequestParam("station-number") String stationNumber) throws IOException, BadResourceException {
        logger.info("get cell numbers by station number");
        return new ResponseEntity<>(this.personFirestationService.getCellNumbers(stationNumber), HttpStatus.OK);
    }
}
