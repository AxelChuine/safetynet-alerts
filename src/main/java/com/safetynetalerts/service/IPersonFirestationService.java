package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;

public interface IPersonFirestationService {
    List<Person> getAllPersonsByFireStation(String stationNumber) throws IOException;

    PhoneAlertDto getCellNumbers (String pStationNumber) throws IOException;

    StationNumberDto getHeadCountByFirestation(String pStationNumber) throws IOException;

    List<PersonMedicalRecordDto> getPersonsAndMedicalRecordsByFirestation(List<String> stations) throws IOException, ResourceNotFoundException, BadResourceException;
}
