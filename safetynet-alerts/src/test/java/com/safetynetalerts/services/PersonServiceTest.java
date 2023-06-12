package com.safetynetalerts.services;

import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.FireStation;
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
import static org.mockito.Mockito.*;

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

	@Test
	void getAllPersonsTest() throws IOException {
		List<Person> people = this.utils.getAllPeople();
		List<Person> peopleToCompare = this.service.getAllPersons();
		assertEquals(people, peopleToCompare);
	}

	@Test
	void addPersonTest () throws IOException {
		List<Person> people = this.utils.getPersons();
		when(this.service.getAllPersons()).thenReturn(people);
		people.add(new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("13 allée Jean moulin").city("Strasbourg").zip("67400").phone("04-91-45-68-97").email("test@gmail.com").build());
		List<Person> personsToCompare = this.service.getAllPersons();
		this.service.addPerson(new PersonDto("Jean", "Dubois", "13 allée Jean moulin", "Strasbourg", "67400","04-91-45-68-97", "test@gmail.com"));
		assertEquals(people, personsToCompare);
	}

	// FIXME: Nullpointerexception cannot read field firstname because person is null
	@Test
	void updatePersonAddressTest() throws IOException {
		String address = "18 rue Jean Moulin";
		Person person = new Person.PersonBuilder().firstName("John").lastName("Boyd").address("1509 Culver St").city("Culver").zip("97451").phone("841-874-6512").email("jaboyd@email.com").build();
		Person personToCompare = new Person.PersonBuilder().firstName("John").lastName("Boyd").address("1509 Culver St").city("Culver").zip("97451").phone("841-874-6512").email("jaboyd@email.com").build();
		this.service.updatePerson(address, personToCompare.firstName, personToCompare.lastName);
		assertEquals(person, personToCompare);
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

	@Test
	void getPersonByFullNameTest () {
		Person p = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("12 rue de la marine").city("Lille").zip("62000").phone("05-66-99-88").email("test@gmail.com").build();
		List<Person> persons = new ArrayList<>();
		List<Person> personsToCompare = new ArrayList<>();
		persons.add(p);
		when(this.utils.getPersons()).thenReturn(persons);
		//List<Person> persons = this.utils.getPersons();
		String firstName = "Jean";
		String lastName = "Dubois";
//		Person person = null;
//		for (Person p : persons) {
//			if (Objects.equals(p.firstName, firstName) && Objects.equals(p.lastName, lastName)) {
//				person = p;
//			}
//		}
		Person personToCompare = this.service.getPersonByFullName(firstName, lastName);
		verify(this.utils, times(1)).getPersons();
		assertEquals(persons.get(0).firstName, personToCompare.firstName);
		assertEquals(persons.get(0), personToCompare);
	}
}
