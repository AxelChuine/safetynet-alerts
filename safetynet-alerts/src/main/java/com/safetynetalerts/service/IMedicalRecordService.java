package com.safetynetalerts.service;

import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMedicalRecordService {

	boolean isUnderaged(String birthDate);

	Map<String, Integer> countAllPersons(List<Person> pPersons) throws IOException;


	Integer getAgeOfPerson(String firstName, String lastName) throws IOException;

	MedicalRecord getMedicalRecordByFullName(String pFirstName, String pLastName) throws IOException;

	boolean isUnderaged(String pFirstName, String pLastName) throws IOException;

	MedicalRecord getMedicalRecordByUnderage(String pFirstName, String pLastName) throws IOException;

	List<MedicalRecordDto> getAllMedicalRecords() throws IOException;


	void updateMedicalRecord (String firstName, String lastName, String allergie) throws IOException;

	void createMedicalRecord(MedicalRecordDto pMedicalRecord);

	void deleteMedicalRecordByFullName(String firstName, String lastName);
}
