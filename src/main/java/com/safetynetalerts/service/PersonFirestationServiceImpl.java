package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.repository.FireStationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonFirestationServiceImpl {

    private final FireStationServiceImpl fireStationService;

    private final MedicalRecordServiceImpl medicalRecordService;

    private final PersonServiceImpl personService;

    private final FireStationRepository fireStationRepository;

    public PersonFirestationServiceImpl(FireStationServiceImpl fireStationService, MedicalRecordServiceImpl medicalRecordService, PersonServiceImpl personService, FireStationRepository fireStationRepository) {
        this.fireStationService = fireStationService;
        this.medicalRecordService = medicalRecordService;
        this.personService = personService;
        this.fireStationRepository = fireStationRepository;
    }


    public List<PersonDto> getAllPersonsByFireStation(String stationNumber) throws IOException {
        List<PersonDto> persons = this.personService.getAllPersons();
        List<PersonDto> personsByFirestation = new ArrayList<>();
        FireStation firestation = fireStationService.getFireStationsByStationNumber(stationNumber);
            for (PersonDto person : persons) {
                if (firestation.getAddresses().contains(person.getAddress())) {
                    if (!personsByFirestation.contains(person)) {
                            personsByFirestation.add(person);
                    }
                }
            }
        return personsByFirestation;
    }

    
    public PhoneAlertDto getCellNumbers(String stationNumber) throws IOException {
        PhoneAlertDto cellNumbers = new PhoneAlertDto();
        List<PersonDto> persons = this.getAllPersonsByFireStation(stationNumber);
        for (PersonDto p : persons) {
            cellNumbers.getCellNumbers().add(p.getPhone());
        }
        return cellNumbers;
    }

    
    public StationNumberDto getHeadCountByFirestation(String pStationNumber) throws IOException {
        List<PersonDto> persons = this.getAllPersonsByFireStation(pStationNumber);
        Map<String, Integer> mapPersons = this.medicalRecordService.countAllPersons(persons);
        return new StationNumberDto(persons, mapPersons.get("mineurs"), mapPersons.get("majeurs"));
    }

    
    public List<PersonMedicalRecordDto> getPersonsAndMedicalRecordsByFirestation(List<String> stations) throws IOException, ResourceNotFoundException, BadResourceException {
        if (stations.isEmpty()) {
            throw new BadResourceException("No firestation(s) provided");
        }
        List<PersonDto> personDtoList = new ArrayList<>();
        List<MedicalRecordDto> medicalRecords = new ArrayList<>();
        List<PersonMedicalRecordDto> personMedicalRecordDtos = new ArrayList<>();
        for (String stationNumber : stations) {
            personDtoList = this.getAllPersonsByFireStation(stationNumber);
        }
        for (PersonDto p : personDtoList) {
            MedicalRecordDto medicalRecordDto = this.medicalRecordService.getMedicalRecordByFullName(p.firstName, p.lastName);
            medicalRecords.add(medicalRecordDto);
            PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto(p.firstName, p.lastName, p.phone, this.medicalRecordService.getAgeOfPerson(p.firstName, p.lastName), medicalRecordDto.getMedications(), medicalRecordDto.getAllergies());
            personMedicalRecordDtos.add(personMedicalRecordDto);
        }
        return personMedicalRecordDtos;
    }
}
