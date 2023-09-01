package com.safetynetalerts.service.impl;

import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireStationServiceImpl implements IFireStationService {

	@Autowired
	private Utils utils;

	@Autowired
	private IMedicalRecordService medicalRecordService;

	List<FireStation> fireStations = new ArrayList<>();

	public List<FireStation> getAllFireStations() throws IOException {
		this.fireStations = this.utils.getAllFirestations();
		return fireStations;
	}

	public void createFirestation(FireStation pFirestation) {
		this.fireStations.add(pFirestation);
	}

	@Override
	public List<PersonMedicalRecordDto> getPersonsAndMedicalRecordsByFirestation(List<String> stations) {
		return null;
	}

	@Override
	public PersonMedicalRecordDto convertToPersonMedicalRecord(Person pPerson, MedicalRecord pMedicalRecord) throws IOException {
		PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto();
		personMedicalRecordDto.setFirstName(pPerson.firstName);
		personMedicalRecordDto.setLastName(pPerson.lastName);
		personMedicalRecordDto.setAge(this.medicalRecordService.getAgeOfPerson(pPerson.firstName, pPerson.lastName));
		personMedicalRecordDto.setPhone(pPerson.phone);
		personMedicalRecordDto.setMedications(pMedicalRecord.getMedications());
		personMedicalRecordDto.setAllergies(pMedicalRecord.getAllergies());
		return personMedicalRecordDto;
	}

	@Override
	public List<FireStation> getFireStationsByStationNumber(String stationNumber) throws IOException {
		List<FireStation> firestations = utils.getAllFirestations().stream()
				.filter(firestation -> firestation.getStationNumber().equals(stationNumber))
				.collect(Collectors.toList());
		return firestations;
	}

}
