package com.safetynetalerts.controller;

import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.impl.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FireStationController {

	@Autowired
	private PersonServiceImpl personService;

	@Autowired
	private IMedicalRecordService medicalService;

	@Autowired
	private IFireStationService service;

	@GetMapping("/firestation")
	public StationNumberDto getFireStation(@RequestParam("stationNumber") String stationNumber) throws IOException {
		StationNumberDto persons = new StationNumberDto();
		persons.getPersons().addAll(this.personService.getAllPersonsByFireStation(stationNumber));
		Map<String, Integer> numberOfPersonByAge = new HashMap<>();
		numberOfPersonByAge = this.medicalService.countAllPersons(persons.getPersons());
		persons.setAdult(numberOfPersonByAge.get("majeurs"));
		persons.setUnderaged(numberOfPersonByAge.get("mineurs"));
		return persons;
	}

	@GetMapping("/phone-alert")
	public PhoneAlertDto getCellNumber(@RequestParam("stationNumber") String stationNumber) throws IOException {
		PhoneAlertDto cellNumbers = new PhoneAlertDto();
		List<Person> persons = this.personService.getAllPersonsByFireStation(stationNumber);
		for (Person p : persons) {
			cellNumbers.getCellNumbers().add(p.phone);
		}
		return cellNumbers;
	}

	@GetMapping("/firestations")
	public List<FireStation> getFirestations() throws IOException {
		return this.service.getAllFireStations();
	}

	@PostMapping("/firestation")
	public void createFirestation (@RequestBody FireStation pFirestation) {
		this.service.createFirestation(pFirestation);
	}

}
