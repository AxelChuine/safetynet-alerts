package com.safetynetalerts.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.impl.FireStationServiceImpl;
import com.safetynetalerts.service.impl.PersonServiceImpl;
import com.safetynetalerts.utils.Utils;

@SpringBootTest
class PersonServiceTest {

	@MockBean
	private Utils utils;

	@Autowired
	private PersonServiceImpl service;

	@MockBean
	private FireStationServiceImpl firestationService;

	@MockBean
	private IMedicalRecordService medicalRecordService;

	@Test
	void getAllPersonsByFireStationTest() throws IOException {
		List<Person> personsToCompare = this.utils.getAllPersons();
		List<FireStation> firestations = this.firestationService.getAllFireStations();
		List<Person> personsToReturn = new ArrayList<>();
		for (Person person : personsToCompare) {
			for (FireStation firestation : firestations) {
				personsToReturn.add(person);
			}
		}

		assertEquals(service.getAllPersonsByFireStation("4"), personsToReturn);
	}

	@Test
	void getAllPersonsByCityTest() throws Exception {
		String vCity = "Culver";
		List<Person> persons = this.utils.getAllPersons();
		List<Person> personsToCompare = this.service.getAllPersonsByCity(vCity);
		for (Person p : persons) {
			if (!p.city.equals(vCity)) {
				persons.remove(p);
			}
		}
		assertEquals(persons, personsToCompare);
	}

	@Test
	void getAllEmailAddressesByCity() throws Exception {
		String vCity = "Culver";
		List<Person> persons = this.utils.getAllPersons();
		List<String> emailAddresses = new ArrayList<>();
		List<String> emailAddressesToCompare = this.service.getAllEmailAddressesByCity(vCity);
		for (Person p : persons) {
			if (p.city.equals(vCity)) {
				emailAddresses.add(p.email);
			}
		}
		assertEquals(emailAddresses, emailAddressesToCompare);
	}

	@Test
	void getPersonInformation() throws IOException {
		String firstName = "John";
		String lastName = "Boyd";
		String informations = "";
		List<String> personInformations = new ArrayList<>();
		List<Person> persons = this.utils.getAllPersons();
		List<MedicalRecord> medicalRecords = this.utils.getAllMedicalRecords();
		List<String> personInformationsToCompare = this.service.getPersonInformation(firstName, lastName);
		for (Person p : persons) {
			for (MedicalRecord r : medicalRecords) {
				if (p.firstName.equals(r.getFirstName()) && p.firstName.equals(firstName)
						&& p.lastName.equals(r.getLastName()) && p.lastName.equals(lastName)) {
					informations = p.firstName + " " + p.lastName + " "
							+ this.medicalRecordService.getAgeOfPerson(firstName, lastName) + " " + p.address + " "
							+ p.email + " " + r.getAllergies() + " " + r.getMedications();
				}
			}
		}
	}

}
