package com.safetynetalerts.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.impl.PersonServiceImpl;

@RestController
public class FireStationController {

	@Autowired
	private PersonServiceImpl personService;

	@Autowired
	private IMedicalRecordService medicalService;

	@GetMapping("/firestation")
	public PersonDto getFireStation(@RequestParam("stationNumber") String stationNumber) throws IOException {
		PersonDto persons = new PersonDto();
		persons.getPersons().addAll(this.personService.getAllPersonsByFireStation(stationNumber));
		Map<String, Integer> numberOfPersonByAge = new HashMap<>();
		numberOfPersonByAge = this.medicalService.countAllPersons(persons.getPersons());
		persons.setAdult(numberOfPersonByAge.get("majeurs"));
		persons.setUnderaged(numberOfPersonByAge.get("mineurs"));
		return persons;
	}

}
