package com.safetynetalerts.controller;

import com.safetynetalerts.service.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/community-email")
public class CommunityEmailController {
    private final PersonServiceImpl service;

    Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    public CommunityEmailController(PersonServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllEmailAddresses(@RequestParam("city") String pCity) throws Exception {
        logger.info("launch of retrieval of every email addresses by city");
        return new ResponseEntity<>(this.service.getAllEmailAddressesByCity(pCity), HttpStatus.OK);
    }
}
