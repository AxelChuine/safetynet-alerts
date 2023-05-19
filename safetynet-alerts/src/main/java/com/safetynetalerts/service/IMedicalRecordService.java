package com.safetynetalerts.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;

public interface IMedicalRecordService {
	List<MedicalRecord> getAllMedicalRecords(List<Person> pPersons) throws IOException;

	boolean isUnderaged(String birthDate);

	Map<String, Integer> countAllPersons(List<Person> pPersons) throws IOException;


	// FIXME: méthode à tester de nouveau.
	Integer getAgeOfPerson(String firstName, String lastName) throws IOException;

	List<MedicalRecord> getMedicalRecordByFullName(String pFirstName, String pLastName) throws IOException;

	boolean isUnderaged(String pFirstName, String pLastName) throws IOException;

	MedicalRecord getMedicalRecordByUnderage(String pFirstName, String pLastName) throws IOException;

	List<MedicalRecord> getAllMedicalRecords() throws IOException;

}
