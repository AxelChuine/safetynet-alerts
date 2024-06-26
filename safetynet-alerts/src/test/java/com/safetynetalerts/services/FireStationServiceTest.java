package com.safetynetalerts.services;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IFireStationRepository;
import com.safetynetalerts.repository.IPersonRepository;
import com.safetynetalerts.service.IFireStationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FireStationServiceTest {

	@Autowired
	private IFireStationService service;

    @MockBean
	private IPersonRepository personRepository;

	@MockBean
	private IFireStationRepository repository;

	private String address = "18 rue jean moulin";

	private String stationNumber = "17";

	private Set<String> addresses;

	private FireStation fireStation;

	private FireStationDto fireStationDto;

	private List<FireStation> fireStations;



	@BeforeEach
	void setUp() {
		this.addresses = new HashSet<>();
		this.addresses.add(address);
		fireStation = new FireStation(this.addresses, stationNumber);
		this.fireStationDto = new FireStationDto(this.addresses, stationNumber);
		this.fireStations = new ArrayList<>();
		this.fireStations.add(fireStation);
	}

    @Test
	public void getAllFireStationsTest() throws IOException {
		String address = "1509 Culver St";
		List<FireStation> expectedFireStations = new ArrayList<>();
		FireStation f1 = new FireStation();
		f1.setStationNumber("2");
		Set<String> addresses = new HashSet<>();
		addresses.add(address);
		f1.setAddresses(addresses);
		expectedFireStations.add(f1);
		List<FireStationDto> expectedFirestationsToCompare = new ArrayList<>();

		for (FireStation firestation : expectedFireStations) {
			FireStationDto fireStationDto = new FireStationDto(new HashSet<>(firestation.getAddresses()), firestation.getStationNumber());
			expectedFirestationsToCompare.add(fireStationDto);
		}

		when(this.repository.getAllFireStations()).thenReturn(expectedFireStations);
		List<FireStationDto> stationsToCompare = this.service.getAllFireStations();
		assertEquals(expectedFirestationsToCompare, stationsToCompare);
	}

	@Test
	public void createFirestationTest() {
		// GIVEN
		FireStationDto fireStationDto = new FireStationDto();
		fireStationDto.setStationNumber("17");
		Set<String> addresses = new HashSet<>();
		String address = "17 rue Général de gaulle";
		addresses.add(address);
		fireStationDto.setAddresses(addresses);
		FireStation fireStation = new FireStation(new HashSet<>(fireStationDto.getAddresses()), fireStationDto.getStationNumber());
		List<FireStation> firestations = new ArrayList<>();
		firestations.add(fireStation);

		// WHEN
		when(this.repository.getAllFireStations()).thenReturn(firestations);
		this.service.createFirestation(fireStationDto);
		// THEN
		assertEquals(firestations.get(0).getStationNumber(), "17");
	}


	@Test
	public void createSimplePersonDtoTest () throws IOException {
		Person person = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("47 rue du général de Gaulle").phone("04").build();
		com.safetynetalerts.dto.SimplePersonDto simplePerson = new com.safetynetalerts.dto.SimplePersonDto(person.firstName, person.lastName, person.address, person.phone);
		List<Person> persons = new ArrayList<>();
		persons.add(person);

		when(this.personRepository.getAllPersons()).thenReturn(persons);
		com.safetynetalerts.dto.SimplePersonDto simplePersonToCompare = this.service.createSimplePersonDto(person);
		assertEquals(simplePerson, simplePersonToCompare);
	}

	@Test
	public void getFireStationsByStationNumberTest () throws IOException {
		String stationNumber = "4";
		Set<String> addresses = new HashSet<>();
		String address = "47 rue du puit";
		addresses.add(address);
		FireStation fireStation = new FireStation(addresses, stationNumber);
		List<FireStation> firestations = new ArrayList<>();
		firestations.add(fireStation);


		when(this.repository.getAllFireStations()).thenReturn(firestations);
		FireStation fireStationToCompare = this.service.getFireStationsByStationNumber(stationNumber);

		assertEquals(fireStation, fireStationToCompare);
	}

	@Test
	public void getStationNumberByAddressShouldReturnANumberIfExists () throws IOException {
		String stationNUmber = "1";
		String address = "78 rue des lapins";
		Set<String> addresses = new HashSet<>();
		addresses.add(address);
		FireStation fireStation = new FireStation(addresses, stationNUmber);
		List<FireStation> firestations = new ArrayList<>();
		firestations.add(fireStation);

		Mockito.when(this.repository.getAllFireStations()).thenReturn(firestations);
		String stationNumberToCompare = this.service.getStationNumberByAddress(address);

		Assertions.assertEquals(stationNUmber, stationNumberToCompare);
	}

	@Test
	public void updateFireStationNumberForAddressShouldReturnAFireStation() throws ResourceNotFoundException, IOException {
		Mockito.when(this.repository.getAllFireStations()).thenReturn(this.fireStations);
		FireStationDto fireStationDtoToCompare = this.service.updateFireStationByAddress(address, stationNumber);

		Assertions.assertEquals(this.fireStationDto, fireStationDtoToCompare);
	}

	@Test
	public void convertToDtoShouldConvertObjectToDto() throws ResourceNotFoundException {
		Mockito.when(this.repository.getAllFireStations()).thenReturn(this.fireStations);
		FireStationDto fireStationDtoToCompare = this.service.convertToDto(this.fireStation);

		Assertions.assertEquals(this.fireStationDto, fireStationDtoToCompare);
	}

	@Test
	public void saveFireStationShouldCreateNewFireStationIfNotExistsOrUpdateExistingOne () throws ResourceNotFoundException {
		FireStation oldFirestation = new FireStation(this.addresses, "4");

		Mockito.when(this.repository.save(oldFirestation, this.fireStation)).thenReturn(this.fireStation);
		FireStation fireStationToCompare = this.service.save(oldFirestation, this.fireStation);

		Assertions.assertEquals(this.fireStation, fireStationToCompare);
	}

	@Test
	public void updateAddressesByFireStationShouldReturnAFireStation() throws ResourceNotFoundException {
		Mockito.when(this.repository.getAllFireStations()).thenReturn(this.fireStations);
		FireStationDto fireStationToCompare = this.service.updateAddressesByFireStation(this.fireStationDto, this.address);

		Assertions.assertEquals(this.fireStationDto, fireStationToCompare);
	}

	@Test
	public void convertDtoToModelShouldReturnAModelObject() {
		FireStation fireStationToCompare = this.service.convertDtoToModel(this.fireStationDto);

		Assertions.assertEquals(this.fireStation, fireStationToCompare);
	}

	@Test
	public void deleteAnAddressInFireStationAddressesShouldReturnAFireStationWithOneLessAddress() throws ResourceNotFoundException {
		this.addresses.clear();
		FireStation newFireStation = new FireStation(this.addresses, "4");
		FireStationDto newFireStationDto = new FireStationDto(this.addresses, "4");

		Mockito.when(this.repository.save(this.fireStation, newFireStation)).thenReturn(newFireStation);
		FireStationDto fireStationToCompare = this.service.deleteAddressOfFireStation(this.fireStationDto, this.address);

		Assertions.assertEquals(newFireStationDto, fireStationToCompare);
	}
}
