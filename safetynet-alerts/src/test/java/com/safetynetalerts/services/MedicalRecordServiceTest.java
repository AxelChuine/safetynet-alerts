package com.safetynetalerts.services;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


	@Test
	void getMedicalRecordByFullName() throws IOException {
		List<MedicalRecord> medicalRecords = utils.getAllMedicalRecords();
		List<MedicalRecord> expectedMedicalRecord = new ArrayList<>();
		String vFirstName = "John";
		String vLastName = "Boyd";
		Integer count = 0;
		List<MedicalRecord> medicalRecordsToCompare = this.service.getMedicalRecordByFullName(vFirstName, vLastName);
		for (MedicalRecord m : medicalRecords) {
			if (m.getFirstName().equals(vFirstName) && m.getLastName().equals(vLastName)) {
				expectedMedicalRecord.add(m);
			}
			count++;
		}
		assertEquals(expectedMedicalRecord, medicalRecordsToCompare);
	}

	/*@Test
	void getAgeOfPersonTest() throws IOException {
		MedicalRecord m = new MedicalRecord();
		m.setFirstName("John");
		m.setLastName("Dubois");
		m.setBirthDate("1/01/2000");
		List<Integer> ages = new ArrayList<>();
		ages.add(23);
		when(this.service.getAgeOfPerson(m.getFirstName(), m.getLastName())).thenReturn(ages);
		List<Integer> agesToCompare = this.service.getAgeOfPerson(m.getFirstName(), m.getLastName());
		assertEquals(23, agesToCompare.get(0));
	}*/

	@Test
	void getAllMedicalRecordsTest() throws IOException {
		MedicalRecord medicalRecord = new MedicalRecord();
		MedicalRecord medicalRecord2 = new MedicalRecord();
		MedicalRecord medicalRecord3 = new MedicalRecord();
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);
		medicalRecords.add(medicalRecord2);
		medicalRecords.add(medicalRecord3);

		when(this.utils.getAllMedicalRecords()).thenReturn(medicalRecords);
		List<MedicalRecord> medicalRecordsToCompare = this.service.getAllMedicalRecords();
		assertEquals(medicalRecords, medicalRecordsToCompare);
	}

	@Test
	public void getMedicalRecordByUnderageTest() throws IOException {
		MedicalRecord m = new MedicalRecord();
		m.setFirstName("Jean");
		m.setLastName("Dubois");
		m.setBirthDate("05/05/2023");
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(m);

		when(this.utils.getAllMedicalRecords()).thenReturn(medicalRecords);
		MedicalRecord mToCompare = this.service.getMedicalRecordByUnderage(m.getFirstName(), m.getLastName());
		assertEquals(m, mToCompare);
	}

	// FIXME: test à fixer
	/*@Test
	public void countAllPersonsTest () throws IOException {
		Map<String, Integer> mapPersons = new HashMap<>();
		mapPersons.put("mineurs", 1);
		mapPersons.put("majeurs", 1);
		List<MedicalRecord> medicalRecords = new ArrayList<>();
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
		Person p1 = new Person.PersonBuilder().firstName("Marc").lastName("Dubois").build();
		Person p2 = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").build();
		List<Person> persons = new ArrayList<>();
		persons.add(p1);
		persons.add(p2);


		when(this.data.getMedicalRecords()).thenReturn(medicalRecords);
		Map<String, Integer> mapPersonsToCompare = this.service.countAllPersons(persons);

		assertEquals(mapPersons, mapPersonsToCompare);
	}*/

	@Test
	public void isUnderagedTest () throws IOException {
		boolean isUnderaged = true;
		String firstName = "Jean";
		String lastName = "Dubois";
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setLastName(lastName);
		medicalRecord.setFirstName(firstName);
		medicalRecord.setBirthDate("08/12/2020");


		when(this.service.getMedicalRecordByUnderage(firstName, lastName)).thenReturn(medicalRecord);
		boolean isUnderagedToCompare = this.service.isUnderaged(firstName, lastName);

		assertEquals(isUnderaged, isUnderagedToCompare);
	}


}
