package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;
import com.safetynetalerts.service.FireStationServiceImpl;
import com.safetynetalerts.service.PersonFirestationServiceImpl;
import com.safetynetalerts.service.PersonMedicalRecordsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/firestation")
public class FireStationController {

	private final FireStationServiceImpl service;

	private final PersonFirestationServiceImpl personFirestationService;

	private final PersonMedicalRecordsServiceImpl personMedicalRecordService;

	Logger logger = LoggerFactory.getLogger(FireStationController.class);

    public FireStationController(FireStationServiceImpl service, PersonFirestationServiceImpl personFirestationService, PersonMedicalRecordsServiceImpl personMedicalRecordService) {
        this.service = service;
        this.personFirestationService = personFirestationService;
        this.personMedicalRecordService = personMedicalRecordService;
    }


    @GetMapping
	public ResponseEntity<StationNumberDto> getHeadCountByFirestation(@RequestParam("stationNumber") String stationNumber) throws IOException {
		logger.info("get head count by firestation");
		StationNumberDto persons = this.personFirestationService.getHeadCountByFirestation(stationNumber);
		if (Objects.isNull(persons)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(persons, HttpStatus.OK);
	}

	@GetMapping("/phone-alert")
	public ResponseEntity<PhoneAlertDto> getCellNumbers(@RequestParam("stationNumber") String stationNumber) throws IOException {
		logger.info("get cell numbers by station number");
		return new ResponseEntity<>(this.personFirestationService.getCellNumbers(stationNumber), HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<FireStationDto>> getFirestations() throws IOException {
		logger.info("retrieve all firestations");
		return ResponseEntity.ok(this.service.getAllFireStations());
	}

	@PostMapping
	public ResponseEntity<FireStationDto> createFirestation (@RequestBody FireStationDto pFirestation) throws ResourceNotFoundException, ResourceAlreadyExistsException, IOException {
		logger.info("create firestation");
		return new ResponseEntity<>(this.service.createFirestation(pFirestation), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<FireStationDto> updateFirestation(@RequestBody FireStationDto fireStationDto) throws ResourceNotFoundException, IOException, BadResourceException {
		logger.info("update firestation");
		return new ResponseEntity<>(this.service.updateFireStationByAddress(fireStationDto), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity deleteFirestation(@RequestBody FireStationDto fireStationDto) throws IOException, ResourceNotFoundException {
		this.service.deleteFirestation(fireStationDto);
		return new ResponseEntity(HttpStatus.OK);
	}
}
