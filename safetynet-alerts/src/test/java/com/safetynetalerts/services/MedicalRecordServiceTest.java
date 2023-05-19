package com.safetynetalerts.services;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
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

	@Test
	void getAllMedicalRecordsTest() throws IOException {
		List<MedicalRecord> expectedMedicalRecords = utils.getAllMedicalRecords();
		List<Person> persons = utils.getAllPeople();
		List<MedicalRecord> medicalRecordToReturn = this.service.getAllMedicalRecords(persons);
		assertEquals(expectedMedicalRecords, medicalRecordToReturn);
	}

	@Test
	void isUnderagedTest() {
		MedicalRecord m1 = new MedicalRecord();
		m1.setFirstName("Jean");
		m1.setLastName("Dubois");
		m1.setBirthDate("01/01/2021");
		m1.setAllergies(null);
		m1.setMedications(null);
		assertEquals(true, this.service.isUnderaged(m1.getBirthDate()));
	}

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
	void getAllMedicalRecordsTest2() throws IOException {
		List<MedicalRecord> medicalRecords = this.service.getAllMedicalRecords();
		List<MedicalRecord> medicalRecordsToCompare = this.utils.getAllMedicalRecords();
		assertEquals(medicalRecordsToCompare, medicalRecords);
	}

	@Test
	void getMedicalRecordByUnderageTest() throws IOException {
		MedicalRecord m = new MedicalRecord();
		m.setFirstName("Jean");
		m.setLastName("Dubois");
		m.setBirthDate("05/05/2023");
		when(this.service.getMedicalRecordByUnderage(m.getFirstName(), m.getLastName())).thenReturn(m);
		MedicalRecord mToCompare = this.service.getMedicalRecordByUnderage(m.getFirstName(), m.getLastName());
		assertEquals(mToCompare, m);
	}

}
