package com.safetynetalerts.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.utils.Utils;

@Service
public class PersonServiceImpl implements IPersonService {

	@Autowired
	private FireStationServiceImpl fireStationService;

	@Autowired
	private Utils utils;

	@Autowired
	private IMedicalRecordService medicalRecordService;

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
			for (Person person : persons) {
				if (firestation.getAddresses().contains(person.getAddress())) {
					if (!personsByFirestation.contains(person)) {
						personsByFirestation.add(person);
					}
				}
			}
			/*
			 * personsByFirestation .addAll(persons.stream().filter(person ->
			 * person.getAddress().equals(firestation.getAddresses()))
			 * .collect(Collectors.toList()));
			 */
		}
		return personsByFirestation;
	}

	@Override
	public List<Person> getAllPersonsByCity(String pCity) throws Exception {
		List<Person> persons = this.utils.getAllPersons();
		List<Person> personsToReturn = new ArrayList<>();
		for (Person p : persons) {
			if (p.city.equals(pCity)) {
				personsToReturn.add(p);
			}
		}
		return personsToReturn;
	}

	@Override
	public List<String> getAllEmailAddressesByCity(String pCity) throws Exception {
		List<Person> persons = this.getAllPersonsByCity(pCity);
		List<String> emailAddresses = new ArrayList<>();
		for (Person p : persons) {
			if (p.city.equals(pCity)) {
				emailAddresses.add(p.email);
			}
		}
		return emailAddresses;
	}

	@Override
	public List<String> getPersonInformation(String pFirstName, String pLastName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Person> getPersonByFullName(String pFirstName, String pLastName) throws IOException {
		Integer count = 0;
		List<Person> persons = this.utils.getAllPersons();
		while (count < persons.size()) {
			if (!(persons.get(count).getFirstName().equals(pFirstName)
					&& persons.get(count).getLastName().equals(pLastName))) {
				persons.remove(count);
			}
			count++;
		}
		return persons;
	}

	public List<Person> getPersonByAddress(String pAddress) throws IOException {
		List<Person> personsByAddress = this.utils.getAllPersons();
		personsByAddress = personsByAddress.stream().filter(p -> Objects.equals(p.address, pAddress))
				.collect(Collectors.toList());
		return personsByAddress;
	}

	@Override
	public ChildAlertDto getChildByAddress(String pAddress) throws IOException {
		ChildAlertDto childDto = new ChildAlertDto();
		childDto.getChildren().addAll(this.getPersonByAddress(pAddress).stream().filter(p -> {
			try {
				return this.medicalRecordService.isUnderaged(p.firstName, p.lastName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}).collect(Collectors.toList()));
		return childDto;
	}

}
