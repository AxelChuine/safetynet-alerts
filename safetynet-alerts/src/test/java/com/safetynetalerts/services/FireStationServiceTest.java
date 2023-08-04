package com.safetynetalerts.services;

import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.impl.FireStationServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FireStationServiceTest {

	@Autowired
	private FireStationServiceImpl service;

	@MockBean
	private Utils utils;
	@MockBean
	private Data data;

	@Test
	public void getAllFireStationsTest() throws IOException {
		List<FireStation> expectedFireStations = new ArrayList<>();
		FireStation f1 = new FireStation();
		f1.setStationNumber("2");
		f1.setAddresses(new HashSet<>());
		f1.addAddress("1509 Culver St");
		expectedFireStations.add(f1);
		when(utils.getAllFirestations()).thenReturn(expectedFireStations);
		List<FireStation> stationsToCompare = this.service.getAllFireStations();
		assertEquals(expectedFireStations, stationsToCompare);
	}

	@Test
	public void createFirestationTest() {
		// GIVEN
		FireStation firestation = new FireStation();
		firestation.setStationNumber("17");
		List<FireStation> firestations = new ArrayList<>();
		firestations.add(firestation);

		// WHEN
		when(this.data.getFirestations()).thenReturn(firestations);
		this.service.createFirestation(firestation);
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

}
