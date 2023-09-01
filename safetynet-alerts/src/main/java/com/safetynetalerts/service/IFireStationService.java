package com.safetynetalerts.service;

import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;

public interface IFireStationService {

	List<FireStation> getAllFireStations() throws IOException;

	void createFirestation(FireStation pFirestation);

    List<PersonMedicalRecordDto> getPersonsAndMedicalRecordsByFirestation(List<String> stations);

	PersonMedicalRecordDto convertToPersonMedicalRecord(Person pPerson, MedicalRecord pMedicalRecord) throws IOException;

	List<FireStation> getFireStationsByStationNumber(String stationNumber) throws IOException;

	SimplePersonDto createSimplePersonDto(Person pPerson);

	StationNumberDto createStationNumberDto (List<Person> persons) throws IOException;


}
