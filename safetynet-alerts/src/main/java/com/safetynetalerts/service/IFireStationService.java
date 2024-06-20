package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;

public interface IFireStationService {

	List<FireStationDto> getAllFireStations() throws IOException;

	void createFirestation(FireStationDto pFirestation);

	PersonMedicalRecordDto convertToPersonMedicalRecord(Person pPerson, MedicalRecord pMedicalRecord) throws IOException, ResourceNotFoundException;

	FireStation getFireStationsByStationNumber(String stationNumber) throws IOException;

	SimplePersonDto createSimplePersonDto(Person pPerson);

	String getStationNumberByAddress(String address) throws IOException;

	FireStationDto updateFireStation(String newStationNumber, String oldStationNumber) throws ResourceNotFoundException;

	FireStationDto convertToDto(FireStation fireStation) throws ResourceNotFoundException;

	FireStation save(FireStation oldFirestation, FireStation newFirestation) throws ResourceNotFoundException;
}
