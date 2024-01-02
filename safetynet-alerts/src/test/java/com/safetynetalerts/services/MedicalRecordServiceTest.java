package com.safetynetalerts.services;

import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MedicalRecordServiceTest {

	@MockBean
	private Utils utils;

	@Autowired
	private IMedicalRecordService service;

	@MockBean
	private Data data;

    // FIXME: test  à relancer
	@Test
	void getMedicalRecordByFullNameTest() throws IOException {
		String vFirstName = "John";
		String vLastName = "Boyd";
		List<String> medications = new ArrayList<>();
		medications.add("paracétamol");
		List<String> allergies = new ArrayList<>();
		allergies.add("lactose");
		MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(vFirstName).lastName(vLastName).birthDate("01/01/2001").medications(medications).allergies(allergies).build();
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);

		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		MedicalRecord medicalRecordsToCompare = this.service.getMedicalRecordByFullName(vFirstName, vLastName);

		assertEquals(medicalRecord.getFirstName(), medicalRecordsToCompare.getFirstName());
		assertEquals(medicalRecord.getLastName(), medicalRecordsToCompare.getLastName());
	}

	@Test
	void getAgeOfPersonTest() throws IOException {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName("John");
		medicalRecord.setLastName("Dubois");
		medicalRecord.setBirthDate("01/01/2000");
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);

		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		Integer ageToCompare = this.service.getAgeOfPerson(medicalRecord.getFirstName(), medicalRecord.getLastName());
		assertEquals(24, ageToCompare);
	}

	@Test
	void getAllMedicalRecordsTest() throws IOException {
		MedicalRecord medicalRecord = new MedicalRecord();
		MedicalRecord medicalRecord2 = new MedicalRecord();
		MedicalRecord medicalRecord3 = new MedicalRecord();
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);
		medicalRecords.add(medicalRecord2);
		medicalRecords.add(medicalRecord3);

		List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
		for (MedicalRecord m : medicalRecords) {
			MedicalRecordDto medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(m.getFirstName()).lastName(m.getLastName()).birthDate(m.getBirthDate()).allergies(m.getAllergies()).medications(m.getMedications()).build();
			medicalRecordDtos.add(medicalRecordDto);
		}

		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		List<MedicalRecordDto> medicalRecordsToCompare = this.service.getAllMedicalRecords();
		assertEquals(medicalRecordDtos.get(0).getFirstName(), medicalRecordsToCompare.get(0).getFirstName());
		assertEquals(medicalRecordDtos.get(0).getLastName(), medicalRecordsToCompare.get(0).getLastName());
	}

	@Test
	public void getMedicalRecordByUnderageTest() throws IOException {
		MedicalRecord m = new MedicalRecord();
		m.setFirstName("Jean");
		m.setLastName("Dubois");
		m.setBirthDate("05/05/2023");
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(m);

		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		MedicalRecord mToCompare = this.service.getMedicalRecordByUnderage(m.getFirstName(), m.getLastName());
		assertEquals(m, mToCompare);
	}

	@Test
	public void countAllPersonsTest () throws IOException {
		String address = "108 allée des pins";
		// création de ma map
		Map<String, Integer> mapPersons = new HashMap<>();
		mapPersons.put("mineurs", 1);
		mapPersons.put("majeurs", 1);

		// création d'une liste de medicalRecords
		List<MedicalRecord> medicalRecords = new ArrayList<>();

		//création de medical record et ajout à la liste
		MedicalRecord m1 = new MedicalRecord();
		m1.setBirthDate("09/12/2021");
		m1.setFirstName("Marc");
		m1.setLastName("Dubois");
		MedicalRecord m2 = new MedicalRecord();
		m2.setBirthDate("09/12/2001");
		m2.setFirstName("Jean");
		m2.setLastName("Dubois");
		medicalRecords.add(m1);
		medicalRecords.add(m2);

		// création de personnes et ajout à une liste de personnes
		Person p1 = new Person.PersonBuilder().firstName("Marc").lastName("Dubois").address(address).build();
		Person p2 = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address(address).build();
		List<Person> persons = new ArrayList<>();
		persons.add(p1);
		persons.add(p2);


		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		Map<String, Integer> mapPersonsToCompare = this.service.countAllPersons(persons);

		assertEquals(mapPersons, mapPersonsToCompare);
	}

	@Test
	public void isUnderagedTest () throws IOException {
		boolean isUnderaged = true;
		String firstName = "Jean";
		String lastName = "Dubois";
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setLastName(lastName);
		medicalRecord.setFirstName(firstName);
		medicalRecord.setBirthDate("08/12/2020");
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);


		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		boolean isUnderagedToCompare = this.service.isUnderaged(firstName, lastName);

		assertEquals(isUnderaged, isUnderagedToCompare);
	}

	@Test
	public void createMedicalRecordTest () throws IOException {
		MedicalRecordDto medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
		MedicalRecord medicalRecord = new MedicalRecord();
		List<MedicalRecord> medicalRecords = this.data.getMedicalRecords();
		medicalRecords.add(medicalRecord);

		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		this.service.createMedicalRecord(medicalRecordDto);
		List<MedicalRecordDto> medicalRecordsToCompare = this.service.getAllMedicalRecords();

		assertEquals(medicalRecords.get(0).getFirstName(), medicalRecordsToCompare.get(0).getFirstName());
	}

	@Test
	public void updateMedicalRecordTest () throws IOException {
		String allergie = "lactose";
		List<String> allergies = new ArrayList<>();
		allergies.add(allergie);
		List<String> medications = new ArrayList<>();
		medications.add("paracétamol");
		MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName("Jean").lastName("Dubois").birthDate("01/01/2001").medications(medications).allergies(allergies).build();
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);

		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		MedicalRecord medicalRecordToCompare = new MedicalRecord.MedicalRecordBuilder().firstName("Jean").lastName("Dubois").birthDate("01/01/2001").medications(medications).allergies(allergies).build();
		this.service.updateMedicalRecord(medicalRecordToCompare.getFirstName(), medicalRecordToCompare.getLastName(), allergie);

		assertEquals(medicalRecord.getFirstName(), medicalRecordToCompare.getFirstName());
		assertEquals(medicalRecord.getLastName(), medicalRecordToCompare.getLastName());
		assertEquals(medicalRecord.getAllergies(), medicalRecordToCompare.getAllergies());
	}


	//FIXME: test qui passe avant que je ne code la fonction
	@Test
	public void deleteMedicalRecordTest() {
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		String firstName = "Jean";
		String lastName = "Dubois";
		List<String> allergies = new ArrayList<>();
		allergies.add("lactose");
		List<String> medications = new ArrayList<>();
		medications.add("paracétamol");
		String birthDate = "01/01/2001";
		MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).medications(medications).allergies(allergies).build();
		medicalRecords.add(medicalRecord);

		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		List<MedicalRecord> medicalRecordsToCompare = this.data.getMedicalRecords();
		this.service.deleteMedicalRecordByFullName(firstName, lastName);

		assertEquals(medicalRecords, medicalRecordsToCompare);
	}

}
