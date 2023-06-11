package com.safetynetalerts.services;

import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.impl.FireStationServiceImpl;
import com.safetynetalerts.service.impl.PersonServiceImpl;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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


	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void getAllPersonsByFireStationTest() throws IOException {
		List<Person> personsToCompare = this.utils.getAllPeople();
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
		List<Person> persons = this.utils.getAllPeople();
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
		List<Person> persons = this.utils.getAllPeople();
		List<String> emailAddresses = new ArrayList<>();
		List<String> emailAddressesToCompare = this.service.getAllEmailAddressesByCity(vCity);
		for (Person p : persons) {
			if (p.city.equals(vCity)) {
				emailAddresses.add(p.email);
			}
		}
		assertEquals(emailAddresses, emailAddressesToCompare);
	}

	// FIXME: à régler
	@Test
	void getPersonInformationTest() throws IOException {
		String firstName = "John";
		String lastName = "Boyd";
		String informations = "";
		List<String> personInformations = new ArrayList<>();
		List<Person> persons = this.utils.getAllPeople();
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

	@Test
	void getAllPersonsTest() throws IOException {
		List<Person> people = this.utils.getAllPeople();
		List<Person> peopleToCompare = this.service.getAllPersons();
		assertEquals(people, peopleToCompare);
	}

	@Test
	void addPersonTest () throws IOException {
		List<Person> people = this.utils.getPersons();
		people.add(new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("13 allée Jean moulin").city("Strasbourg").zip("67400").phone("04-91-45-68-97").email("test@gmail.com").build());
		when(this.service.getAllPersons()).thenReturn(people);
		List<Person> personsToCompare = this.service.getAllPersons();
		this.service.addPerson(new PersonDto("Jean", "Dubois", "13 allée Jean moulin", "Strasbourg", "67400","04-91-45-68-97", "test@gmail.com"));
		assertEquals(people, personsToCompare);
	}


	@Test
	void convertToListDtoTest() throws IOException {
		List<PersonDto> persons = new ArrayList<>();
		for (Person p : this.utils.getPersons()) {
			PersonDto person = new PersonDto(p.firstName, p.lastName, p.address, p.city, p.zip, p.phone, p.email);
			persons.add(person);
		}
		List<PersonDto> personsToCompare = this.service.convertToListDto(this.service.getAllPersons());
		assertEquals(persons, personsToCompare);
	}
}
