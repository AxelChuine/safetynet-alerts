package com.safetynetalerts.service;

import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.MedicalRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMedicalRecordService {

	boolean isUnderaged(String birthDate);

	Map<String, Integer> countAllPersons(List<SimplePersonDto> pPersons) throws IOException;


	// FIXME: méthode à tester de nouveau.
	Integer getAgeOfPerson(String firstName, String lastName) throws IOException;

	List<MedicalRecord> getMedicalRecordByFullName(String pFirstName, String pLastName) throws IOException;

	boolean isUnderaged(String pFirstName, String pLastName) throws IOException;

	MedicalRecord getMedicalRecordByUnderage(String pFirstName, String pLastName) throws IOException;

	List<MedicalRecord> getAllMedicalRecords() throws IOException;


	void updateMedicalRecord (MedicalRecord medicalRecord, String allergie);

}
