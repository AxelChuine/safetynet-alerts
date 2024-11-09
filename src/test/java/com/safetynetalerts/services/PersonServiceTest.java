package com.safetynetalerts.services;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.PersonRepositoryImpl;
import com.safetynetalerts.service.MedicalRecordServiceImpl;
import com.safetynetalerts.service.PersonServiceImpl;
import com.safetynetalerts.utils.mapper.MapperPerson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

	@InjectMocks
	private PersonServiceImpl service;

	@Mock
	private MedicalRecordServiceImpl medicalRecordService;

	@Mock
	private PersonRepositoryImpl repository;

	@Mock
	private MapperPerson mapper;

	private List<Person> personList;

	private List<PersonDto> personDtoList;

	private final String firstName = "Jean";

	private final String lastName = "Dubois";

    private final String zip = "45877";

	private final String email = "test@gmail.com";

	private final String city = "Culver";

	private final String phone  = "23145431321";

	private PersonDto personDto;

	private Person person;

    private final String address = "18 rue Jean moulin";

	MedicalRecordDto medicalRecordDto;

	private List<MedicalRecordDto> medicalRecordDtoList;


	@BeforeEach
	public void setUp() {
		this.person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setAddress(address);
		person.setEmail(email);
		person.setPhone(phone);
		person.setCity(city);
        this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).email(email).phone(phone).city(city).build();
		this.personList = List.of(this.person);
		this.personDtoList = List.of(this.personDto);
        String birthDate = "01/01/2009";
        this.medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
		this.medicalRecordDtoList = new ArrayList<>(List.of(this.medicalRecordDto));
	}



	@Test
	void getAllPersonsByCityTest() throws Exception {
		String vCity = "Culver";

		when(this.repository.getAllPersons()).thenReturn(this.personList);
		List<Person> personsToCompare = this.service.getAllPersonsByCity(vCity);

		assertEquals(this.personList, personsToCompare);
	}

	@Test
	void getAllEmailAddressesByCityShouldReturnAListOfEmailAddress() throws Exception {
		List<String> emailAdresses = List.of(this.email);
		when(this.repository.getAllPersons()).thenReturn(personList);
		List<String> emailAddressesToCompare = this.service.getAllEmailAddressesByCity(city);

		assertEquals(emailAdresses, emailAddressesToCompare);
	}

	@Test
	public void getAllEmailAddressesByCityShouldThrowBadResourceException() throws Exception {
		String message = "this city doesn't exist";

		BadResourceException exceptionToCompare = Assertions.assertThrows(BadResourceException.class, () -> this.service.getAllEmailAddressesByCity(null), message);

		Assertions.assertEquals(exceptionToCompare.getStatus(), HttpStatus.BAD_REQUEST);
		Assertions.assertEquals(exceptionToCompare.getMessage(), message);
	}

	@Test
	public void getAllEmailAddressesShouldThrowResourceNotFoundException() throws Exception {
		String message = "No email address found";

		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.getAllEmailAddressesByCity("Paris"), message);

		Assertions.assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
		Assertions.assertEquals(exception.getMessage(), message);
	}


	@Test
	void getAllPersonsTest() throws IOException, BadResourceException {
		when(this.repository.getAllPersons()).thenReturn(this.personList);
		Mockito.when(this.mapper.toPersonDtoList(this.personList)).thenReturn(this.personDtoList);
		List<PersonDto> peopleToCompare = this.service.getAllPersons();

		assertEquals(this.personDtoList, peopleToCompare);
	}


	@Test
	public void getPersonByFullNameShouldReturnAPersonDto() throws Exception {
		when(this.repository.getAllPersons()).thenReturn(this.personList);
		Mockito.when(this.mapper.toPersonDtoList(this.personList)).thenReturn(this.personDtoList);
		PersonDto person = this.service.getPersonByFullName(firstName, lastName);

		Assertions.assertEquals(this.personDto, person);
	}

	@Test
	public void getPersonByFullNameShouldThrowBadResourceException() throws Exception {
		String message = "One or two parameter(s) is are missing";

		BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.service.getPersonByFullName(null, lastName), message);

		Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
		Assertions.assertEquals(exception.getMessage(), message);
	}

	@Test
	public void getPersonByFullNameShouldThrowResourceNotFoundException() throws Exception {
		String message = "La personne s'appelant " + firstName + " " + lastName + " n'existe pas.";

		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.getPersonByFullName(firstName, lastName), message);

		Assertions.assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
		Assertions.assertEquals(exception.getMessage(), message);
	}

	@Test
	public void deletePersonTest () throws IOException, ResourceNotFoundException {
		// ARRANGE
		List<Person> expectedPersons = new ArrayList<>();
		String phone = "23145431321";
		Person person = new Person(firstName, lastName, address, city, zip, phone, this.email);
		expectedPersons.add(person);

		// ACT
		when(this.repository.getAllPersons()).thenReturn(expectedPersons);
		List<Person> persons = this.repository.getAllPersons();
		this.service.deletePerson("Jean", "Dubois");

		// ASSERT
		verify(this.repository).save(persons);
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
	public void getPersonByAddressShouldReturnAListOfPersonDto() throws ResourceNotFoundException, BadResourceException {
		when(this.repository.getAllPersons()).thenReturn(this.personList);
		Mockito.when(this.mapper.toPersonDtoList(this.personList)).thenReturn(this.personDtoList);
		List<PersonDto> persons = this.service.getPersonsByAddress(this.address);

		assertEquals(this.personDtoList, persons);
	}

	@Test
	public void getPersonByAddressShouldThrowBadRequest() {
		String message = "No person found at this address";

		BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.service.getPersonsByAddress(null), message);

		Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
		Assertions.assertEquals(message, exception.getMessage());
	}

	@Test
	public void getPersonByAddressShouldThrowResourceNotFoundException() {
		String message = "The people you are looking for don't exist.";

		Mockito.when(this.repository.getAllPersons()).thenReturn(List.of());
		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.getPersonsByAddress(address), message);

		Assertions.assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
		Assertions.assertEquals(exception.getMessage(), message);
	}

	@Test
	public void convertToSimplePersonDtoTest() {
		SimplePersonDto person = new SimplePersonDto(firstName, lastName, address, phone);
		Person personToChange = new Person(firstName, lastName, address, city, zip, phone, this.email);
		SimplePersonDto personToCompare = this.service.convertToSimplePersonDto(personToChange);
		assertEquals(person, personToCompare);
		assertEquals(person.hashCode(), personToCompare.hashCode());
		assertEquals(person.toString(), personToCompare.toString());
	}

	@Test
	public void getFamilyMembersTest() {
		List<PersonDto> personsToCompare = this.service.getFamilyMembers(this.personDtoList, lastName);

		assertEquals(personDtoList, personsToCompare);
	}

	@Test
	public void convertToDtoListShouldReturnAListOfPersonDto () {
		List<Person> persons = new ArrayList<>();
		Person p1 = new Person();
		Person p2 = new Person();
		Person p3 = new Person();
		persons.add(p1);
		persons.add(p2);
		persons.add(p3);
		List<SimplePersonDto> personsDto = new ArrayList<>();
		List<SimplePersonDto> personsToCompare;
		for (Person person : persons) {
			personsDto.add(this.service.convertToSimplePersonDto(person));
		}

		personsToCompare = this.service.convertToSimplePersonDtoList(persons);
		assertEquals(personsDto, personsToCompare);
		assertEquals(personsDto.hashCode(), personsToCompare.hashCode());
		assertEquals(personsDto.toString(), personsToCompare.toString());
	}

	@Test
	public void getChildByAddressShouldReturnAListOfChildAlertDto () throws IOException, ResourceNotFoundException, BadResourceException {
		ChildAlertDto childAlertDto = new ChildAlertDto(firstName, lastName, 0, this.personDtoList);
		List<ChildAlertDto> childAlertDtoList = new ArrayList<>();
		childAlertDtoList.add(childAlertDto);


		Mockito.when(this.repository.getAllPersons()).thenReturn(this.personList);
		Mockito.when(this.mapper.toPersonDtoList(this.personList)).thenReturn(this.personDtoList);
		when(this.medicalRecordService.isUnderaged(person.getFirstName(), person.getLastName())).thenReturn(true);
		when(this.repository.getAllPersons()).thenReturn(this.personList);
		List<ChildAlertDto> childAlertDtosToCompare = this.service.getChildByAddress(address);

		assertEquals(childAlertDtoList, childAlertDtosToCompare);
		assertEquals(childAlertDtoList.hashCode(), childAlertDtosToCompare.hashCode());
		assertEquals(childAlertDtoList.toString(), childAlertDtosToCompare.toString());
	}

	@Test
	public void getChildByAddressShouldThrowBadRequestException () {
		String message = "address not provided exception";

		BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.service.getChildByAddress(null), message);

		Assertions.assertEquals(exception.getMessage(), message);
		Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void getChildByAddressShouldThrowResourceNotFoundException () throws IOException {
		String message = "The people you are looking for don't exist.";

		when(this.repository.getAllPersons()).thenReturn(this.personList);
		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.getChildByAddress(address), message);

		Assertions.assertEquals(exception.getMessage(), message);
		Assertions.assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
	}


	@Test
	public void getPersonInfoShouldReturnAListOfPersonsInfoIfExists() throws IOException, ResourceNotFoundException, BadResourceException {
		String lastName = "Dubois";
		PersonInfo specificPersonInfo = new PersonInfo("Jean", "Dubois", 15, "test@gmail.com", null, null);
		List<PersonInfo> personInfo = new ArrayList<>(List.of(specificPersonInfo));

		Mockito.when(this.repository.getAllPersons()).thenReturn(this.personList);
		Mockito.when(this.mapper.toPersonDtoList(this.personList)).thenReturn(this.personDtoList);
		Mockito.when(this.medicalRecordService.getAllMedicalRecordByListOfPersons(this.personDtoList)).thenReturn(this.medicalRecordDtoList);
		Mockito.when(this.medicalRecordService.getAgeOfPerson(firstName, lastName)).thenReturn(15);
		List<PersonInfo> personInfoToCompare = this.service.getPersonInfo(lastName);

		Assertions.assertEquals(personInfo, personInfoToCompare);
		Assertions.assertEquals(personInfo.hashCode(), personInfoToCompare.hashCode());
		Assertions.assertEquals(personInfo.toString(), personInfoToCompare.toString());
	}

	@Test
	public void updatePersonShouldReturnAPerson() throws BadResourceException, ResourceNotFoundException, ResourceAlreadyExistsException {
		String address = "154 rue de la paix";
		String phone = "23145431321";
		Person updatedPerson = new Person(firstName, lastName, address, city, zip, phone, this.email);
		PersonDto updatedPersonDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).zip(zip).build();

		Mockito.when(this.repository.getAllPersons()).thenReturn(this.personList);
		Mockito.when(this.mapper.toPersonDtoList(this.personList)).thenReturn(this.personDtoList);
		Mockito.when(this.mapper.toPerson(this.personDto)).thenReturn(this.person);
		Mockito.when(this.mapper.toPerson(updatedPersonDto)).thenReturn(updatedPerson);
		Mockito.when(this.repository.savePerson(this.person, updatedPerson)).thenReturn(updatedPerson);
		Mockito.when(this.mapper.toPersonDto(updatedPerson)).thenReturn(updatedPersonDto);
		PersonDto personDtoToCompare = this.service.updatePerson(updatedPersonDto);

		Assertions.assertEquals(updatedPersonDto, personDtoToCompare);
		Assertions.assertEquals(updatedPersonDto.hashCode(), personDtoToCompare.hashCode());
		Assertions.assertEquals(updatedPersonDto.toString(), personDtoToCompare.toString());
	}

	@Test
	public void getPersonByLastNameShouldReturnAListOfPersonHavingTheSameLastName () throws BadResourceException {
		Mockito.when(this.repository.getAllPersons()).thenReturn(this.personList);
		Mockito.when(this.mapper.toPersonDtoList(this.personList)).thenReturn(this.personDtoList);
		List<PersonDto> personDtoListToCompare = this.service.getPersonByLastName(this.lastName);

		Assertions.assertEquals(this.personDtoList, personDtoListToCompare);
	}

	@Test
	public void addPersonShouldReturnAPersonDto() throws ResourceAlreadyExistsException, ResourceNotFoundException, BadResourceException {
		String phone = "23145431321";
		Person newPerson = new Person(firstName, lastName, address, city, zip, phone, this.email);
		PersonDto newPersonDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).address(city).zip(zip).email(email).build();

		Mockito.when(this.mapper.toPerson(newPersonDto)).thenReturn(newPerson);
		Mockito.when(this.repository.getAllPersons()).thenReturn(this.personList);
		Mockito.when(this.mapper.toPerson(newPersonDto)).thenReturn(newPerson);
		Mockito.when(this.repository.savePerson(newPerson, newPerson)).thenReturn(newPerson);
		PersonDto personToCompare = this.service.addPerson(newPersonDto);

		Assertions.assertEquals(newPersonDto, personToCompare);
		Assertions.assertEquals(newPersonDto.hashCode(), personToCompare.hashCode());
		Assertions.assertEquals(newPersonDto.toString(), personToCompare.toString());
	}

	@Test
	public void addPersonShouldThrowResourceAlreadyExistsExceptionIfAlreadyExists() throws BadResourceException {
		String message = "Person already exists.";

		Mockito.when(this.repository.getAllPersons()).thenReturn(personList);
		Mockito.when(this.mapper.toPersonDtoList(this.personList)).thenReturn(this.personDtoList);
		ResourceAlreadyExistsException exception = Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> this.service.addPerson(personDto), message);

		Assertions.assertEquals(exception.getMessage(), message);
		Assertions.assertEquals(exception.getStatus(), HttpStatus.CONFLICT);
	}

	@Test
	public void savePersonShouldReturnAPersonDto() throws BadResourceException, ResourceAlreadyExistsException {
		Mockito.when(this.mapper.toPerson(this.personDto)).thenReturn(this.person);
		Mockito.when(this.repository.savePerson(this.person)).thenReturn(this.person);
		Mockito.when(this.mapper.toPersonDto(this.person)).thenReturn(this.personDto);

		PersonDto personDtoToCompare = this.service.savePerson(this.personDto);

		Assertions.assertEquals(this.personDto, personDtoToCompare);
	}

	@Test
	public void savePersonShouldThrowBadResourceException () {
		String message = "No person provided";

		BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.service.savePerson(null), message);

		Assertions.assertEquals(exception.getMessage(), message);
		Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void savePersonShouldThrowResourceAlreadyExists() throws BadResourceException {
		String message = "This person already exists";

		Mockito.when(this.repository.getAllPersons()).thenReturn(personList);
		Mockito.when(this.mapper.toPersonDtoList(this.personList)).thenReturn(this.personDtoList);
		ResourceAlreadyExistsException exception = Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> this.service.savePerson(personDto), message);

		Assertions.assertEquals(exception.getMessage(), message);
		Assertions.assertEquals(exception.getStatus(), HttpStatus.CONFLICT);
	}

}
