package com.safetynetalerts.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.impl.PersonServiceImpl;

@RestController
public class FireStationController {

	@Autowired
	private PersonServiceImpl personService;

	@GetMapping("/firestation")
	public List<Person> getFireStation(@RequestParam("stationNumber") String stationNumber) throws IOException {
		List<Person> persons = this.personService.getAllPersonsByFireStation(stationNumber);
		return persons;
	}

}
