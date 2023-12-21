package com.safetynetalerts.services;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.service.impl.FireStationServiceImpl;
import com.safetynetalerts.utils.Data;
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
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest
class PersonServiceTest {

	@MockBean
	private Utils utils;

	@Autowired
	private IPersonService service;

	@MockBean
	private FireStationServiceImpl firestationService;

	@MockBean
	private Data data;

	@MockBean
	private IPersonFirestationService personFirestationService;

	@MockBean
	private IMedicalRecordService medicalRecordService;


	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}



	@Test
	void getAllPersonsByCityTest() throws Exception {
		String vCity = "Culver";
		List<Person> persons = this.data.getPersons();
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
		List<Person> persons = this.data.getPersons();
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
		List<Person> people = List.of(new Person.PersonBuilder().build(), new Person.PersonBuilder().build());

		when(this.data.getPersons()).thenReturn(people);
		List<Person> peopleToCompare = this.service.getAllPersons();

		assertEquals(people, peopleToCompare);
	}

	@Test
	void updatePersonAddressTest() throws Exception {
		String address = "18 rue Jean Moulin";
		Person person = new Person.PersonBuilder().firstName("John").lastName("Boyd").address("1509 Culver St").city("Culver").zip("97451").phone("841-874-6512").email("jaboyd@email.com").build();
		List<Person> persons = new ArrayList<>();
		persons.add(person);

		when(this.data.getPersons()).thenReturn(persons);
		Person personToCompare = new Person.PersonBuilder().firstName("John").lastName("Boyd").address("1509 Culver St").city("Culver").zip("97451").phone("841-874-6512").email("jaboyd@email.com").build();
		this.service.updatePerson(address, personToCompare.firstName, personToCompare.lastName);

		assertEquals(person.getFirstName(), personToCompare.getFirstName());
		assertEquals(person.getLastName(), personToCompare.getLastName());
	}

	@Test
	void updatePersonAddressNotFoundTest() throws Exception {
		String address = "18 rue Jean Moulin";
		Person person = new Person.PersonBuilder().firstName("John").lastName("Boyd").address("1509 Culver St").city("Culver").zip("97451").phone("841-874-6512").email("jaboyd@email.com").build();
		List<Person> persons = new ArrayList<>();
		persons.add(person);
		String resource = "person"+ " " + person.firstName + " " + person.lastName;
		Person personToCompare = person;

		when(this.data.getPersons()).thenReturn(new ArrayList<>());
		try {
			this.service.updatePerson(address, personToCompare.firstName, personToCompare.lastName);
			fail("Should throw resource not found exception");
		}catch(ResourceNotFoundException resourceNotFoundException){
			assert(resourceNotFoundException.getMessage().contains(resource));
		}
	}


	@Test
	void getPersonByFullNameTest () throws Exception {
		Person p = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("12 rue de la marine").city("Lille").zip("62000").phone("05-66-99-88").email("test@gmail.com").build();
		List<Person> persons = new ArrayList<>();
		List<Person> personsToCompare = new ArrayList<>();
		persons.add(p);
		when(this.data.getPersons()).thenReturn(persons);
		String firstName = "Jean";
		String lastName = "Dubois";
		Person personToCompare = this.service.getPersonByFullName(firstName, lastName);
		// verify(this.utils, times(1)).getPersons();
		assertEquals(persons.get(0).firstName, personToCompare.firstName);
		assertEquals(persons.get(0), personToCompare);
	}

	@Test
	public void deletePersonTest () throws IOException {
		// ARRANGE
		List<Person> expectedPersons = new ArrayList<>();
		Person person = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("12 rue de la marine").city("Lille").zip("62000").phone("05-66-99-88").email("test@gmail.com").build();
		expectedPersons.add(person);

		// ACT
		when(this.data.getPersons()).thenReturn(expectedPersons);
		List<Person> persons = this.data.getPersons();
		this.service.deletePerson("Jean", "Dubois");

		// ASSERT
		verify(this.data, times(1)).setPersons(persons);
	}

	@Test
	public void getPersonByAddressTest() throws IOException {
		// ARRANGE
		String address = "18 rue jean moulin";
		Person person = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address(address).city("Strasbourg").zip("67400").phone("0454").email("test@gmail.com").build();
		List<Person> expectedPersons = new ArrayList<>();
		expectedPersons.add(person);
		// ACT
		when(this.data.getPersons()).thenReturn(expectedPersons);
		List<Person> persons = this.service.getPersonsByAddress(address);
		// ASSERT
		assertEquals(expectedPersons, persons);
	}

	@Test
	public void convertToSimplePersonDtoTest() {
		SimplePersonDto person = new SimplePersonDto("John", "Boyd", "13 rue Jean Moulin", "04-91-45-87-36");
		Person personToChange = new Person.PersonBuilder().firstName("John").lastName("Boyd").address("13 rue Jean Moulin").city("Strasbourg").zip("67000").phone("04-91-45-87-36").email("test@gmail.com").build();
		com.safetynetalerts.dto.SimplePersonDto personToCompare = this.service.convertToSimplePersonDto(personToChange);
		assertEquals(person.getFirstName(), personToCompare.getFirstName());
	}

	@Test
	public void getFamilyMembersTest() {
		// ARRANGE
		Person p1 = new Person.PersonBuilder().firstName("John").lastName("Dubois").address("13 rue Jean Moulin").city("Strasbourg").zip("67000").phone("04-91-45-87-36").email("test@gmail.com").build();
		Person p2 = new Person.PersonBuilder().firstName("Mathias").lastName("Dubois").address("13 rue Jean Moulin").city("Strasbourg").zip("67000").phone("04-91-45-87-36").email("test@gmail.com").build();
		Person p3 = new Person.PersonBuilder().firstName("Lukas").lastName("Dubois").address("13 rue Jean Moulin").city("Strasbourg").zip("67000").phone("04-91-45-87-36").email("test@gmail.com").build();
		List<Person> expectedPersons = new ArrayList<>();
		expectedPersons.add(p1);
		expectedPersons.add(p2);
		expectedPersons.add(p3);
		String lastName = "Dubois";
		// ACT
		when(this.data.getPersons()).thenReturn(expectedPersons);
		List<Person> persons = this.data.getPersons();
		List<Person> personsToCompare = this.service.getFamilyMembers(expectedPersons, lastName);
		// ASSERT
		assertEquals(persons, personsToCompare);
	}

	@Test
	public void convertToDtoListTest () {
		List<Person> persons = new ArrayList<>();
		Person p1 = new Person.PersonBuilder().build();
		Person p2 = new Person.PersonBuilder().build();
		Person p3 = new Person.PersonBuilder().build();
		persons.add(p1);
		persons.add(p2);
		persons.add(p3);
		List<SimplePersonDto> personsDto = new ArrayList<>();
		List<SimplePersonDto> personsToCompare = new ArrayList<>();
		for (Person person : persons) {
			personsDto.add(this.service.convertToSimplePersonDto(person));
		}

		personsToCompare = this.service.convertToDtoList(persons);
		assertEquals(personsDto, personsToCompare);
	}

	@Test
	public void getChildByAddressTest () throws IOException {
		String address = "95 rue du maréchal pétain";
		List<Person> personsByAddress = new ArrayList<>();
		Person person = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address(address).build();
		Person adult = new Person.PersonBuilder().firstName("Marc").lastName("Dubois").address(address).build();
		personsByAddress.add(person);
		personsByAddress.add(adult);
		ChildAlertDto childAlertDto = new ChildAlertDto("Jean", "Dubois", 0, personsByAddress);
		List<ChildAlertDto> childAlertDtos = new ArrayList<>();
		childAlertDtos.add(childAlertDto);


		when(this.medicalRecordService.isUnderaged(person.firstName, person.lastName)).thenReturn(true);
		when(this.service.getPersonsByAddress(address)).thenReturn(personsByAddress);
		List<ChildAlertDto> childAlertDtosToCompare = this.service.getChildByAddress(address);

		assertEquals(childAlertDtos.get(0).getAge(), childAlertDtosToCompare.get(0).getAge());
		assertEquals(childAlertDtos.get(0).getFirstName(), childAlertDtosToCompare.get(0).getFirstName());
		assertEquals(childAlertDtos.get(0).getLastName(), childAlertDtosToCompare.get(0).getLastName());
	}


}
