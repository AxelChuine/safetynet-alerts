package com.safetynetalerts.services;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.utils.Utils;

@SpringBootTest
public class MedicalRecordServiceImplTest {

	@MockBean
	private Utils utils;

	@Test
	void getAllMedicalRecordsTest() {
		List<MedicalRecord> expectedMedicalRecords = utils.getAllMedicalRecords();
		List<MedicalRecord> medicalRecordToReturn = this.service.getAllMedicalRecords();
		assertEquals(expectedMedicalRecords, medicalRecordToReturn);
	}
}
