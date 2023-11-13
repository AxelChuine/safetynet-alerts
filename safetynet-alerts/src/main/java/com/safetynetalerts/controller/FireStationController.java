package com.safetynetalerts.controller;

import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
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

@Slf4j
@RestController
public class FireStationController {

	@Autowired
	private IMedicalRecordService medicalService;

	@Autowired
	private IFireStationService service;

	@Autowired
	private IPersonFirestationService personFirestationService;

	Logger logger = LoggerFactory.getLogger(FireStationController.class);

	@GetMapping("/firestation")
	public ResponseEntity<StationNumberDto> getHeadCountByFirestation(@RequestParam("stationNumber") String stationNumber) throws IOException {
		logger.info("get head count by firestation");
		StationNumberDto persons = this.personFirestationService.getHeadCountByFirestation(stationNumber);
		return ResponseEntity.ok(persons);
	}

	@GetMapping("/phone-alert")
	public ResponseEntity<PhoneAlertDto> getCellNumbers(@RequestParam("stationNumber") String stationNumber) throws IOException {
		logger.info("get cell numbers by station number");
		PhoneAlertDto cellNumbers = this.personFirestationService.getCellNumbers(stationNumber);
		return ResponseEntity.ok(cellNumbers);
	}

	@GetMapping("/firestations")
	public ResponseEntity<List<FireStationDto>> getFirestations() throws IOException {
		logger.info("retrieve all firestations");
		return ResponseEntity.ok(this.service.getAllFireStations());
	}

	@PostMapping("/firestation")
	public ResponseEntity createFirestation (@RequestBody FireStationDto pFirestation) {
		logger.info("create firestation");
		this.service.createFirestation(pFirestation);
		if (Objects.isNull(pFirestation)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<List<PersonMedicalRecordDto>> getAllPersonsAndMedicalRecordByFirestation(@RequestParam("stations") List<String> stations) throws IOException {
		logger.info("get all persons and associated medical records by firestation");
		return ResponseEntity.ok(this.personFirestationService.getPersonsAndMedicalRecordsByFirestation(stations));
	}

}
