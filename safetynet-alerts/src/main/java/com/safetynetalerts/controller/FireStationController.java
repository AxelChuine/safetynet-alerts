package com.safetynetalerts.controller;

import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
public class FireStationController {

	@Autowired
	private IMedicalRecordService medicalService;

	@Autowired
	private IFireStationService service;

	@Autowired
	private IPersonFirestationService personFirestationService;

	@GetMapping("/firestation")
	public ResponseEntity<StationNumberDto> getHeadCountByFirestation(@RequestParam("stationNumber") String stationNumber) throws IOException {
		StationNumberDto persons = this.service.getHeadCountByFirestation(stationNumber);
		return ResponseEntity.ok(persons);
	}

	@GetMapping("/phone-alert")
	public ResponseEntity<PhoneAlertDto> getCellNumbers(@RequestParam("stationNumber") String stationNumber) throws IOException {
		PhoneAlertDto cellNumbers = this.personFirestationService.getCellNumbers(stationNumber);
		return ResponseEntity.ok(cellNumbers);
	}

	@GetMapping("/firestations")
	public ResponseEntity<List<FireStation>> getFirestations() throws IOException {
		return ResponseEntity.ok(this.service.getAllFireStations());
	}

	@PostMapping("/firestation")
	public ResponseEntity createFirestation (@RequestBody FireStation pFirestation) {
		this.service.createFirestation(pFirestation);
		if (Objects.isNull(pFirestation)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<List<PersonMedicalRecordDto>> getAllPersonsAndMedicalRecordByFirestation(@RequestParam("stations") List<String> stations) {
		return ResponseEntity.ok(this.service.getPersonsAndMedicalRecordsByFirestation(stations));
	}

}
