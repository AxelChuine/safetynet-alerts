package com.safetynetalerts.services;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IPersonRepository;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.impl.PersonServiceImpl;
import com.safetynetalerts.utils.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

	@Autowired
	private PersonServiceImpl service;

	@MockBean
	private Data data;

	@MockBean
	private IMedicalRecordService medicalRecordService;
	
	@MockBean
	private IPersonRepository repository;

	private List<Person> persons;

	private List<PersonDto> personDtos;

	private String firstName = "Jean";

	private String lastName = "Dubois";

	private String city = "Culver";

	private String zip = "45877";

	private PersonDto personDto;

	private Person person;

	private String address = "18 rue Jean moulin";


	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		person = new Person.PersonBuilder().firstName(firstName).lastName(lastName).address(address).city(city).build();
		persons = List.of(this.person);
		this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).city(city).build();
		personDtos = List.of(this.personDto);
	}



	@Test
	void getAllPersonsByCityTest() throws Exception {
		String vCity = "Culver";
		List<Person> persons = List.of(new Person.PersonBuilder().city(vCity).build(), new Person.PersonBuilder().city(vCity).build());

		when(this.repository.getAllPersons()).thenReturn(persons);
		List<Person> personsToCompare = this.service.getAllPersonsByCity(vCity);

		assertEquals(persons, personsToCompare);
	}

	@Test
	void getAllEmailAddressesByCity() throws Exception {
		String vCity = "Culver";
		String email1 = "test@gmail.com";
		String email2 = "test2@gmail.com";
		List<Person> personList = List.of(new Person.PersonBuilder().city(vCity).email(email1).build(), new Person.PersonBuilder().city(vCity).email(email2).build());
		List<String> emailAddresses = new ArrayList<>();
		emailAddresses.add(email1);
		emailAddresses.add(email2);

		when(this.repository.getAllPersons()).thenReturn(personList);
		List<String> emailAddressesToCompare = this.service.getAllEmailAddressesByCity(vCity);

		assertEquals(emailAddresses, emailAddressesToCompare);
	}

	@Test
	void getAllPersonsTest() throws IOException {
		List<Person> people = List.of(new Person.PersonBuilder().build(), new Person.PersonBuilder().build());
		List<PersonDto> persons = List.of(new PersonDto.PersonDtoBuilder().build(), new PersonDto.PersonDtoBuilder().build());

		when(this.repository.getAllPersons()).thenReturn(people);
		List<PersonDto> peopleToCompare = this.service.getAllPersons();

		assertEquals(persons, peopleToCompare);
	}


	@Test
	void getPersonByFullNameTest () throws Exception {
		String firstName = "Jean";
		String lastName = "Dubois";
		List<Person> persons = List.of(new Person.PersonBuilder().firstName(firstName).lastName(lastName).build(), new Person.PersonBuilder().build());

		when(this.repository.getAllPersons()).thenReturn(persons);
		PersonDto person = this.service.getPersonByFullName(firstName, lastName);

		verify(this.repository).getAllPersons();
	}

	@Test
	public void deletePersonTest () throws IOException, ResourceNotFoundException {
		// ARRANGE
		List<Person> expectedPersons = new ArrayList<>();
		Person person = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("12 rue de la marine").city("Lille").zip("62000").phone("05-66-99-88").email("test@gmail.com").build();
		expectedPersons.add(person);

		// ACT
		when(this.repository.getAllPersons()).thenReturn(expectedPersons);
		List<Person> persons = this.repository.getAllPersons();
		this.service.deletePerson("Jean", "Dubois");

		// ASSERT
		verify(this.data, times(1)).setPersons(persons);
	}

	@Test
	public void deleteAPersonThatDoesNotExistShouldThrowResourceNotFoundException() throws ResourceNotFoundException {
		String firstName = "Jean";
		String lastName = "Dubois";
		PersonDto personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).build();

		ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class, () ->
				this.service.deletePerson("Jean", "Dubois"));

		Assertions.assertEquals(resourceNotFoundException.getMessage(), "La personne " +
				personDto.getFirstName() + " " + personDto.getLastName() +" n'existe pas.");
	}

	@Test
	public void getPersonByAddressTest() throws ResourceNotFoundException {
		when(this.repository.getAllPersons()).thenReturn(this.persons);
		List<PersonDto> persons = this.service.getPersonsByAddress(this.address);
		// ASSERT
		assertEquals(this.personDtos, persons);
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
		when(this.repository.getAllPersons()).thenReturn(this.persons);
		List<PersonDto> personsToCompare = this.service.getFamilyMembers(this.personDtos, lastName);

		assertEquals(personDtos, personsToCompare);
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

		personsToCompare = this.service.convertToSimplePersonDtoList(persons);
		assertEquals(personsDto, personsToCompare);
	}

	@Test
	public void getChildByAddressTest () throws IOException, ResourceNotFoundException {
		List<PersonDto> personsByAddress = new ArrayList<>();
		PersonDto person = new PersonDto.PersonDtoBuilder().firstName(this.firstName).lastName(this.lastName).address(address).build();
		PersonDto adult = new PersonDto.PersonDtoBuilder().firstName("Marc").lastName(this.lastName).address(address).build();
		personsByAddress.add(person);
		personsByAddress.add(adult);
		ChildAlertDto childAlertDto = new ChildAlertDto(this.firstName, this.lastName, 0, personsByAddress);
		List<ChildAlertDto> childAlertDtos = new ArrayList<>();
		childAlertDtos.add(childAlertDto);


		when(this.medicalRecordService.isUnderaged(person.firstName, person.lastName)).thenReturn(true);
		when(this.repository.getAllPersons()).thenReturn(this.persons);
		List<ChildAlertDto> childAlertDtosToCompare = this.service.getChildByAddress(address);

		assertEquals(childAlertDtos.get(0).getAge(), childAlertDtosToCompare.get(0).getAge());
		assertEquals(childAlertDtos.get(0).getFirstName(), childAlertDtosToCompare.get(0).getFirstName());
		assertEquals(childAlertDtos.get(0).getLastName(), childAlertDtosToCompare.get(0).getLastName());
	}

	// FIXME: personinfo à régler sur une autre branche
	/*@Test
	public void getPersonInfoShouldReturnAListOfPersonsInfoIfExists() {
		String lastName = "Dubois";
		List<String> allergies = List.of("peanut", "milk");
		List<String> medications = List.of("ventoline");
		SpecificPersonInfo specificPersonInfo = new SpecificPersonInfo("Jean", "Dubois", 15, "test@gmail.com", medications, allergies);
		PersonInfo personInfo = new PersonInfo(List.of(specificPersonInfo));

		Mockito.when(this.repository.getAllPersons()).thenReturn(this.persons);
		*//*PersonInfo personInfoToCompare = this.service.getPersonInfo(lastName);*//*

		Assertions.assertEquals(personInfo, personInfoToCompare);
	}*/

	@Test
	public void convertToPersonDtoShouldReturnAPersonDto () throws ResourceNotFoundException {
		Mockito.when(this.repository.getAllPersons()).thenReturn(this.persons);
		PersonDto personToCompare = this.service.convertToPersonDto(this.person);

		Assertions.assertEquals(this.personDto, personToCompare);
	}

	@Test
	public void convertToPersonDtoListShouldReturnAListOfDto() throws ResourceNotFoundException {
		Mockito.when(this.repository.getAllPersons()).thenReturn(this.persons);
		List<PersonDto> personsToCompare = this.service.convertToDtoList(this.persons);

		Assertions.assertEquals(this.personDtos, personsToCompare);
	}

	@Test
	public void convertPersonDtoToPersonShouldReturnAPerson() throws ResourceNotFoundException {
		Person personToCompare = this.service.convertToPerson(this.personDto);

		Assertions.assertEquals(this.person, personToCompare);
	}

	@Test
	public void updatePersonShouldReturnAPerson() throws BadResourceException, ResourceNotFoundException {
		Person updatedPerson = new Person.PersonBuilder().firstName(firstName).lastName(lastName).address(address).zip(zip).build();
		PersonDto updatedPersonDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).zip(zip).build();

		Mockito.when(this.repository.getAllPersons()).thenReturn(this.persons);
		Mockito.when(this.repository.savePerson(this.person, updatedPerson)).thenReturn(updatedPerson);
		PersonDto personDtoToCompare = this.service.updatePerson(updatedPersonDto);

		Assertions.assertEquals(updatedPersonDto, personDtoToCompare);
	}

	@Test
	public void getPersonByLastNameShouldReturnAListOfPersonHavingTheSameLastName () {
		Mockito.when(this.repository.getAllPersons()).thenReturn(this.persons);
		List<PersonDto> personDtoListToCompare = this.service.getPersonByLastName(this.lastName);

		Assertions.assertEquals(this.personDtos, personDtoListToCompare);
	}

}
