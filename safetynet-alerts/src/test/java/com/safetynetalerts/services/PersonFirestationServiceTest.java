package com.safetynetalerts.services;


import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IFireStationRepository;
import com.safetynetalerts.repository.IPersonRepository;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonFirestationServiceTest {

    @Autowired
    private IPersonFirestationService service;

    @MockBean
    private IFireStationService fireStationService;

    @MockBean
    private IMedicalRecordService medicalRecordService;

    @MockBean
    private IPersonRepository personRepository;

    @MockBean
    private IFireStationRepository fireStationRepository;


    @Test
    public void getHeadCountByFirestationTest () throws IOException {
        // mocking the firestationService side
        StationNumberDto stationNumberDto = new StationNumberDto();
        String address = "94 rue jean moulin";
        List<Person> persons = new ArrayList<>();
        Person p1 = new Person.PersonBuilder().firstName("prenom1").lastName("nom1").address(address).build();
        Person p2 = new Person.PersonBuilder().firstName("prenom2").lastName("nom2").address(address).build();
        Person p3 = new Person.PersonBuilder().firstName("prenom3").lastName("nom3").address(address).build();
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        stationNumberDto.setPersons(persons);
        stationNumberDto.setUnderaged(2);
        stationNumberDto.setAdult(1);

        //mocking the medicalRecordService side
        Map<String, Integer> mapPersons = new HashMap<>();
        mapPersons.put("majeurs", 1);
        mapPersons.put("mineurs", 2);

        //mocking a firestation
        FireStation fireStation = new FireStation();
        Set<String> addresses = new HashSet<>();
        addresses.add(address);
        fireStation.setStationNumber("4");
        fireStation.setAddresses(addresses);

        //mocking a list of medical record
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord m1 = new MedicalRecord();
        MedicalRecord m2 = new MedicalRecord();
        MedicalRecord m3 = new MedicalRecord();
        medicalRecords.add(m1);
        medicalRecords.add(m2);
        medicalRecords.add(m3);


        when(this.personRepository.getAllPersons()).thenReturn(persons);
        when(this.fireStationService.getFireStationsByStationNumber("4")).thenReturn(fireStation);
        when(this.medicalRecordService.countAllPersons(persons)).thenReturn(mapPersons);
        StationNumberDto stationNumberToCompare = this.service.getHeadCountByFirestation("4");

        assertEquals(stationNumberDto.getPersons(), stationNumberToCompare.getPersons());
        assertEquals(stationNumberDto.getUnderaged(), stationNumberToCompare.getUnderaged());
        assertEquals(stationNumberDto.getPersons(), stationNumberToCompare.getPersons());
    }

    @Test
    public void getCellNumbersTest() throws IOException {
        PhoneAlertDto cellNumbers = new PhoneAlertDto();
        String phone = "04";
        cellNumbers.getCellNumbers().add(phone);
        List<Person> persons = new ArrayList<>();
        String address = "95 rue jean moulin";
        Person person = new Person.PersonBuilder().address(address).phone(phone).build();
        persons.add(person);
        Set<String> addresses = new HashSet<>();
        addresses.add(address);
        String stationNumber = "4";
        FireStation fireStation = new FireStation(addresses, stationNumber);

        when(this.fireStationService.getFireStationsByStationNumber("4")).thenReturn(fireStation);
        when(this.personRepository.getAllPersons()).thenReturn(persons);
        PhoneAlertDto cellNumbersToCompare = this.service.getCellNumbers("4");

        assertEquals(cellNumbers.getCellNumbers(), cellNumbersToCompare.getCellNumbers());
    }

    @Test
    public void getPersonsAndMedicalRecordsByFirestationTest () throws IOException, ResourceNotFoundException {
        String address = "101 rue jean moulin";
        List<String> medications = new ArrayList<>();
        String medication = "paracétamol";
        medications.add(medication);
        String allergie = "lactose";
        List<String> allergies = new ArrayList<>();
        allergies.add(allergie);
        List<PersonMedicalRecordDto> personMedicalRecordDtos = new ArrayList<>();
        String firstName = "Jean";
        String lastName = "Dubois";
        PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto(firstName, lastName, "04", 0, medications, allergies);
        personMedicalRecordDtos.add(personMedicalRecordDto);

        List<Person> persons = new ArrayList<>();
        Person p1 = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address(address).phone("04").build();
        persons.add(p1);
        MedicalRecord m1 = new MedicalRecord.MedicalRecordBuilder().firstName("Jean").lastName("Dubois").medications(medications).allergies(allergies).birthDate("09/29/1998").build();

        List<FireStation> firestations = new ArrayList<>();
        String stationNumber = "4";
        Set<String> addresses = new HashSet<>();
        addresses.add(address);
        FireStation firestation = new FireStation(addresses, stationNumber);
        List<String> stations = new ArrayList<>();
        stations.add(firestation.getStationNumber());
        firestations.add(firestation);

        String birthDate = "01/01/2001";
        MedicalRecordDto medicalRecord = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).allergies(allergies).medications(medications).build();


        when(this.fireStationRepository.getAllFireStations()).thenReturn(firestations);
        when(this.personRepository.getAllPersons()).thenReturn(persons);
        when(this.medicalRecordService.getMedicalRecordByFullName(firstName, lastName)).thenReturn(medicalRecord);
        when(this.fireStationService.getFireStationsByStationNumber(stationNumber)).thenReturn(firestation);
        List<PersonMedicalRecordDto> personMedicalRecordDtosToCompare = this.service.getPersonsAndMedicalRecordsByFirestation(stations);

        assertEquals(personMedicalRecordDtos.get(0).getFirstName(), personMedicalRecordDtosToCompare.get(0).getFirstName());
        assertEquals(personMedicalRecordDtos.get(0).getLastName(), personMedicalRecordDtosToCompare.get(0).getLastName());
        assertEquals(personMedicalRecordDtos.get(0).getAge(), personMedicalRecordDtosToCompare.get(0).getAge());
    }
}
