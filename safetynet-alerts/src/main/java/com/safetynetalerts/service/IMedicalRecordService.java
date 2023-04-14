package com.safetynetalerts.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;

public interface IMedicalRecordService {
	List<MedicalRecord> getAllMedicalRecords(List<Person> pPersons) throws IOException;

	boolean isUnderaged(String birthDate);

	Map<String, Integer> countAllPersons(List<Person> pPersons);

	List<Integer> getAgeOfPerson(String firstName, String lastName) throws IOException;

	List<MedicalRecord> getMedicalRecordByFullName(String pFirstName, String pLastName);

}
