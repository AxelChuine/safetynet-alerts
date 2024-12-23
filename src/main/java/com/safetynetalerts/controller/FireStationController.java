package com.safetynetalerts.controller;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.StationNumberDto;
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
	public ResponseEntity<StationNumberDto> getHeadCountByFirestation(@RequestParam("station-number") String stationNumber) throws IOException, BadResourceException {
		logger.info("get head count by firestation");
		StationNumberDto persons = this.personFirestationService.getHeadCountByFirestation(stationNumber);
		if (Objects.isNull(persons)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(persons, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<FireStationDto>> getFirestations() throws IOException {
		logger.info("retrieve all firestations");
		return new ResponseEntity<>(this.service.getAllFireStations(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity createFirestation (@RequestBody FireStationDto pFirestation) throws IOException {
		logger.info("create firestation");
		try {
			return new ResponseEntity<>(this.service.createFirestation(pFirestation), HttpStatus.CREATED);
		} catch (ResourceAlreadyExistsException e) {
			return new ResponseEntity<>(e.getMessage(), e.getStatus());
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), e.getStatus());
		}
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
