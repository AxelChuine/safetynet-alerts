package com.safetynetalerts.service.impl;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IFireStationRepository;
import com.safetynetalerts.repository.IPersonRepository;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
import com.safetynetalerts.utils.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonFirestationServiceImpl implements IPersonFirestationService {

    @Autowired
    private IFireStationService fireStationService;

    @Autowired
    private Data data;

    @Autowired
    private IMedicalRecordService medicalRecordService;

    private final IPersonRepository personRepository;

    private final IFireStationRepository fireStationRepository;

    public PersonFirestationServiceImpl(IPersonRepository personRepository, IFireStationRepository fireStationRepository) {
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

    @Override
    public PhoneAlertDto getCellNumbers(String stationNumber) throws IOException {
        PhoneAlertDto cellNumbers = new PhoneAlertDto();
        List<Person> persons = this.getAllPersonsByFireStation(stationNumber);
        for (Person p : persons) {
            cellNumbers.getCellNumbers().add(p.getPhone());
        }
        return cellNumbers;
    }

    @Override
    public StationNumberDto getHeadCountByFirestation(String pStationNumber) throws IOException {
        List<Person> persons = this.getAllPersonsByFireStation(pStationNumber);
        Map<String, Integer> mapPersons = this.medicalRecordService.countAllPersons(persons);
        return new StationNumberDto(persons, mapPersons.get("mineurs"), mapPersons.get("majeurs"));
    }

    @Override
    public List<PersonMedicalRecordDto> getPersonsAndMedicalRecordsByFirestation(List<String> stations) throws IOException, ResourceNotFoundException {
        List<FireStation> firestations = this.fireStationRepository.getAllFireStations();
        List<Person> persons = new ArrayList<>();
        List<MedicalRecordDto> medicalRecords = new ArrayList<>();
        List<PersonMedicalRecordDto> personMedicalRecordDtos = new ArrayList<>();
        for (FireStation firestation : firestations) {
            persons = this.getAllPersonsByFireStation(firestation.getStationNumber());
        }
        for (Person p : persons) {
            medicalRecords.add(this.medicalRecordService.getMedicalRecordByFullName(p.firstName, p.lastName));
        }
        for (Person p : persons) {
            for (MedicalRecordDto m : medicalRecords) {
                PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto(p.firstName, p.lastName, p.phone, this.medicalRecordService.getAgeOfPerson(p.firstName, p.lastName), m.getMedications(), m.getAllergies());
                personMedicalRecordDtos.add(personMedicalRecordDto);
            }
        }
        return personMedicalRecordDtos;
    }
}
