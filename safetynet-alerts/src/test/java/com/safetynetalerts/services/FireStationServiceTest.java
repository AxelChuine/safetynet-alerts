package com.safetynetalerts.services;

import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IPersonFirestationService;
import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
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
	private IPersonFirestationService personFirestationService;

	@MockBean
	private Utils utils;

	@MockBean
	private Data data;

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

		when(this.data.getFirestations()).thenReturn(expectedFireStations);
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
		when(this.data.getFirestations()).thenReturn(firestations);
		this.service.createFirestation(fireStationDto);
		// THEN
		assertEquals(firestations.get(0).getStationNumber(), "17");
	}

	@Test
	public void convertToPersonMedicalRecordDtoTest() throws IOException {
		// ARRANGE
		List<String> allergies = new ArrayList<>();
		allergies.add("Chat");
		List<String> medications = new ArrayList<>();
		medications.add("paracétamol");
		Person person = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("18 rue Jean Moulin").city("Lille").zip("62000").phone("04").email("test").build();
		MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName("Jean").lastName("Dubois").birthDate("05/11/2000").allergies(allergies).medications(medications).build();
		PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto();
		personMedicalRecordDto.setFirstName(person.firstName);
		personMedicalRecordDto.setLastName(person.lastName);
		personMedicalRecordDto.setAge(23);
		personMedicalRecordDto.setPhone(person.phone);
		personMedicalRecordDto.setMedications(medications);
		personMedicalRecordDto.setAllergies(allergies);

		List<Person> persons = new ArrayList<>();
		persons.add(person);
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);
		// ACT
		when(this.data.getPersons()).thenReturn(persons);
		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		PersonMedicalRecordDto personToCompare = this.service.convertToPersonMedicalRecord(person, medicalRecord);

		// ASSERT
		assertEquals(personMedicalRecordDto.getFirstName(), personToCompare.getFirstName());
		assertEquals(personMedicalRecordDto.getLastName(), personToCompare.getLastName());

	}

	@Test
	public void createSimplePersonDtoTest () throws IOException {
		Person person = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("47 rue du général de Gaulle").phone("04").build();
		com.safetynetalerts.dto.SimplePersonDto simplePerson = new com.safetynetalerts.dto.SimplePersonDto(person.firstName, person.lastName, person.address, person.phone);
		List<Person> persons = new ArrayList<>();
		persons.add(person);

		when(this.data.getPersons()).thenReturn(persons);
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


		when(this.data.getFirestations()).thenReturn(firestations);
		FireStation fireStationToCompare = this.service.getFireStationsByStationNumber(stationNumber);

		assertEquals(fireStation, fireStationToCompare);
	}
}
