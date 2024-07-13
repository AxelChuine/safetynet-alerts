package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMedicalRecordService {

	boolean isUnderaged(String birthDate);

	Map<String, Integer> countAllPersons(List<Person> pPersons) throws IOException;


	Integer getAgeOfPerson(String firstName, String lastName) throws IOException, ResourceNotFoundException;

	MedicalRecordDto getMedicalRecordByFullName(String pFirstName, String pLastName) throws ResourceNotFoundException;

	boolean isUnderaged(String pFirstName, String pLastName) throws IOException;

	MedicalRecord getMedicalRecordByUnderage(String pFirstName, String pLastName) throws IOException;

	List<MedicalRecordDto> getAllMedicalRecords() throws IOException;


	MedicalRecordDto updateMedicalRecord (String firstName, String lastName, String allergie) throws ResourceNotFoundException;

	MedicalRecordDto createMedicalRecord(MedicalRecordDto pMedicalRecord) throws ResourceNotFoundException, ResourceAlreadyExistsException;

	void deleteMedicalRecordByFullName(String firstName, String lastName) throws ResourceNotFoundException;

	List<MedicalRecordDto> getAllMedicalRecordByListOfPersons(List<PersonDto> personDtoList) throws ResourceNotFoundException;

	MedicalRecord convertDtoToModel(MedicalRecordDto medicalRecordByFullName);
}
