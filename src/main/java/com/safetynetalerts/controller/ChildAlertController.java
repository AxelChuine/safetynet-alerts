package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.service.impl.PersonServiceImpl;
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
@RequestMapping("/child-alert")
public class ChildAlertController {

    private final PersonServiceImpl service;

    Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    public ChildAlertController(PersonServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ChildAlertDto>> getChildByAddress(@RequestParam("address") String address) throws IOException, ResourceNotFoundException {
        logger.info("launch of retrieval of every child by address");
        try {
            return new ResponseEntity<>(this.service.getChildByAddress(address), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BadResourceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
