package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PersonMedicalRecordsServiceImpl {

    private final MedicalRecordServiceImpl medicalRecordService;

    private final PersonServiceImpl personService;

    private final FireStationServiceImpl fireStationService;

    public PersonMedicalRecordsServiceImpl(MedicalRecordServiceImpl medicalRecordService, PersonServiceImpl personService, FireStationServiceImpl fireStationService) {
        this.medicalRecordService = medicalRecordService;
        this.personService = personService;
        this.fireStationService = fireStationService;
    }


    public FireDto getAllConcernedPersonsAndTheirInfosByFire(String address) throws ResourceNotFoundException, IOException, BadResourceException {
        List<PersonDto> persons = this.personService.getPersonsByAddress(address);
        List<MedicalRecordDto> medicalRecordDtos = this.medicalRecordService.getAllMedicalRecordByListOfPersons(persons);
        List<PersonByFireDto> personByFireDtos = this.convertToPersonByFireDtoList(persons, medicalRecordDtos);
        String stationNumber = this.fireStationService.getStationNumberByAddress(address);
        FireDto fireDto = new FireDto(stationNumber, personByFireDtos);
        return fireDto;
    }

    
    public List<PersonInfo> getAllPersonInformations(String lastName) throws ResourceNotFoundException, IOException {
        List<PersonInfo> personInfos = new ArrayList<>();
        List<PersonDto> personByLastName = this.personService.getPersonByLastName(lastName);
        List<MedicalRecordDto> medicalRecordDtosByListOfPerson = this.medicalRecordService.getAllMedicalRecordByListOfPersons(personByLastName);
        for (PersonDto personDto : personByLastName) {
            for (MedicalRecordDto medicalDto : medicalRecordDtosByListOfPerson) {
                PersonInfo personInfo = new PersonInfo();
                personInfo.setFirstName(personDto.getFirstName());
                personInfo.setLastName(personDto.getLastName());
                personInfo.setEmail(personDto.getEmail());
                personInfo.setAge(this.medicalRecordService.getAgeOfPerson(personDto.firstName, personDto.lastName));
                personInfo.setMedications(medicalDto.getMedications());
                personInfo.setAllergies(medicalDto.getAllergies());
                personInfos.add(personInfo);
            }
        }
        return personInfos;
    }

    
    public List<PersonByFireDto> convertToPersonByFireDtoList(List<PersonDto> pPersons, List<MedicalRecordDto> pMedicalRecords) throws IOException, ResourceNotFoundException {
        List<MedicalRecordDto> medicalRecordDtos = pMedicalRecords;
        List<PersonByFireDto> personByFireDtos = new ArrayList<>();
        for (MedicalRecordDto medicalRecordDto : medicalRecordDtos) {
            for (PersonDto personDto : pPersons) {
                if (Objects.equals(personDto.firstName, medicalRecordDto.getFirstName()) && Objects.equals(personDto.lastName, medicalRecordDto.getLastName())) {
                    PersonByFireDto personByFireDto = new PersonByFireDto(personDto.firstName,
                            personDto.lastName,
                            personDto.getPhone(),
                            this.medicalRecordService.getAgeOfPerson(medicalRecordDto.getFirstName(), medicalRecordDto.getLastName()),
                            medicalRecordDto.getMedications(),
                            medicalRecordDto.getAllergies());
                    personByFireDtos.add(personByFireDto);
                }
            }
        }
        if (personByFireDtos.isEmpty()) {
            throw new ResourceNotFoundException("No person found");
        }
        return personByFireDtos;
    }
}
