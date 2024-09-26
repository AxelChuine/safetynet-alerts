package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IFireStationRepository;
import com.safetynetalerts.repository.IPersonRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonFirestationServiceImpl {

    private final FireStationServiceImpl fireStationService;

    private final MedicalRecordServiceImpl medicalRecordService;

    private final IPersonRepository personRepository;

    private final IFireStationRepository fireStationRepository;

    public PersonFirestationServiceImpl(FireStationServiceImpl fireStationService, MedicalRecordServiceImpl medicalRecordService, IPersonRepository personRepository, IFireStationRepository fireStationRepository) {
        this.fireStationService = fireStationService;
        this.medicalRecordService = medicalRecordService;
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
    }


    public List<Person> getAllPersonsByFireStation(String stationNumber) throws IOException {
        List<Person> persons = this.personRepository.getAllPersons();
        List<Person> personsByFirestation = new ArrayList<>();
        FireStation firestation = fireStationService.getFireStationsByStationNumber(stationNumber);
            for (Person person : persons) {
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
        List<Person> persons = this.getAllPersonsByFireStation(stationNumber);
        for (Person p : persons) {
            cellNumbers.getCellNumbers().add(p.getPhone());
        }
        return cellNumbers;
    }

    
    public StationNumberDto getHeadCountByFirestation(String pStationNumber) throws IOException {
        List<Person> persons = this.getAllPersonsByFireStation(pStationNumber);
        Map<String, Integer> mapPersons = this.medicalRecordService.countAllPersons(persons);
        return new StationNumberDto(persons, mapPersons.get("mineurs"), mapPersons.get("majeurs"));
    }

    
    public List<PersonMedicalRecordDto> getPersonsAndMedicalRecordsByFirestation(List<String> stations) throws IOException, ResourceNotFoundException, BadResourceException {
        if (stations.isEmpty()) {
            throw new BadResourceException("No station number given");
        }
        List<Person> persons = new ArrayList<>();
        List<MedicalRecordDto> medicalRecords = new ArrayList<>();
        List<PersonMedicalRecordDto> personMedicalRecordDtos = new ArrayList<>();
        for (String stationNumber : stations) {
            persons = this.getAllPersonsByFireStation(stationNumber);
        }
        for (Person p : persons) {
            MedicalRecordDto medicalRecordDto = this.medicalRecordService.getMedicalRecordByFullName(p.firstName, p.lastName);
            medicalRecords.add(medicalRecordDto);
            PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto(p.firstName, p.lastName, p.phone, this.medicalRecordService.getAgeOfPerson(p.firstName, p.lastName), medicalRecordDto.getMedications(), medicalRecordDto.getAllergies());
            personMedicalRecordDtos.add(personMedicalRecordDto);
        }
        return personMedicalRecordDtos;
    }
}
