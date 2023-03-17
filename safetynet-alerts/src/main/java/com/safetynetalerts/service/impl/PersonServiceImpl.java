package com.safetynetalerts.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.PersonService;
import com.safetynetalerts.utils.Utils;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private FireStationServiceImpl fireStationService;

	@Autowired
	private Utils utils;

	/**
	 * 
	 * @param stationNumber
	 * @return list of persons concerned by a firestation
	 * @throws IOException
	 */

	public List<Person> getAllPersonsByFireStation(String stationNumber) throws IOException {
		List<Person> persons = utils.getAllPersons();
		List<Person> personsByFirestation = new ArrayList<>();
		List<FireStation> firestations = fireStationService.getFireStationsByStationNumber(stationNumber);
		for (FireStation firestation : firestations) {
			personsByFirestation
					.addAll(persons.stream().filter(person -> person.getAddress().equals(firestation.getAddresses()))
							.collect(Collectors.toList()));
		}
		return personsByFirestation;
	}

}
