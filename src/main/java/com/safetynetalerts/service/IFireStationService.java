package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;

public interface IFireStationService {

	List<FireStationDto> getAllFireStations() throws IOException;

	FireStationDto createFirestation(FireStationDto pFirestation) throws ResourceNotFoundException, IOException, ResourceAlreadyExistsException;

	PersonMedicalRecordDto convertToPersonMedicalRecord(Person pPerson, MedicalRecord pMedicalRecord) throws IOException, ResourceNotFoundException;

	FireStation getFireStationsByStationNumber(String stationNumber) throws IOException;

	String getStationNumberByAddress(String address) throws IOException;

	FireStationDto convertToDto(FireStation fireStation) throws ResourceNotFoundException;

	FireStation save(FireStation oldFirestation, FireStation newFirestation) throws ResourceNotFoundException;

	FireStationDto updateFireStationByAddress(FireStationDto fireStationDto) throws ResourceNotFoundException, IOException, BadResourceException;

	FireStationDto updateAddressesByFireStation(FireStationDto fireStationDto, String address);

	FireStation convertDtoToModel(FireStationDto fireStationDto);

	FireStationDto deleteAddressOfFireStation(FireStationDto fireStationDto, String address) throws ResourceNotFoundException;

	void deleteFirestation(FireStationDto fireStationDto) throws IOException, ResourceNotFoundException;
}
