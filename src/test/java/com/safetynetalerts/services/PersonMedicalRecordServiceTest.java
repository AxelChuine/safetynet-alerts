package com.safetynetalerts.services;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.FireStationServiceImpl;
import com.safetynetalerts.service.MedicalRecordServiceImpl;
import com.safetynetalerts.service.PersonMedicalRecordsServiceImpl;
import com.safetynetalerts.service.PersonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class PersonMedicalRecordServiceTest {
    @InjectMocks
    private PersonMedicalRecordsServiceImpl service;

    @Mock
    private PersonServiceImpl personService;

    @Mock
    private MedicalRecordServiceImpl medicalRecordService;

    @Mock
    private FireStationServiceImpl fireStationService;

    private String firstName = "John";

    private String lastName = "Smith";

    private String birthDate = "01/01/2010";

    private List<String> allergies;

    private List<String> medications;

    String address = "45 rue jean moulin";

    private Person person;

    private PersonDto personDto;

    private MedicalRecord medicalRecord;

    private MedicalRecordDto medicalRecordDto;

    private List<Person> persons;

    private List<PersonDto> personDtoList = new ArrayList<>();

    private List<MedicalRecord> medicalRecordList;

    private List<MedicalRecordDto> medicalRecordDtoList;

    String stationNumber = "17";

    Set<String> addresses;

    FireStationDto fireStationDto;

    String email = "test@test.com";

    @BeforeEach
    public void setUp() {
        this.person = new Person.PersonBuilder().firstName(firstName).lastName(lastName).email(email).address(address).build();
        this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).email(email).address(address).build();
        this.persons = List.of(person);
        this.personDtoList = List.of(personDto);
        this.medicalRecord = new MedicalRecord();
        this.medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
        this.medicalRecordList = List.of(medicalRecord);
        this.medicalRecordDtoList = List.of(medicalRecordDto);
        this.fireStationDto = new FireStationDto(addresses, stationNumber);
    }

    @Test
    public void getAllConcernedPersonsAndTheirInfosByFireShouldReturnAFireDtoObject () throws ResourceNotFoundException, IOException, BadResourceException {
        Integer age = 0;
        PersonByFireDto personByFireDto = new PersonByFireDto();
        personByFireDto.setFirstName(firstName);
        personByFireDto.setLastName(lastName);
        personByFireDto.setAge(age);
        FireDto fireDto = new FireDto(this.stationNumber, List.of(personByFireDto));
        List<Person> persons = List.of(person);

        Mockito.when(this.personService.getPersonsByAddress(address)).thenReturn(this.personDtoList);
        Mockito.when(this.medicalRecordService.getAllMedicalRecordByListOfPersons(this.personDtoList)).thenReturn(this.medicalRecordDtoList);
        Mockito.when(this.fireStationService.getStationNumberByAddress(address)).thenReturn(stationNumber);
        FireDto fireToCompare = this.service.getAllConcernedPersonsAndTheirInfosByFire(address);

        Assertions.assertEquals(fireDto, fireToCompare);
    }

    @Test
    public void convertListOfPersonsAndMedicalRecordsToPersonsByFireDtosShouldReturnAListOfPersonByFireDtoObject () throws ResourceNotFoundException, IOException, BadResourceException {
        Integer age = 1;
        PersonByFireDto personByFireDto = new PersonByFireDto();
        personByFireDto.setFirstName(firstName);
        personByFireDto.setLastName(lastName);
        personByFireDto.setAge(age);
        List<PersonByFireDto> personByFireDtoList = List.of(personByFireDto);
        List<PersonDto> personDtoList = List.of(personDto);
        List<MedicalRecordDto> medicalRecordDtoList = List.of(medicalRecordDto);

        Mockito.when(this.medicalRecordService.getAgeOfPerson(firstName, lastName)).thenReturn(1);
        List<PersonByFireDto> personByFireDtoToCompare = this.service.convertToPersonByFireDtoList(personDtoList, medicalRecordDtoList);

        Assertions.assertEquals(personByFireDtoList, personByFireDtoToCompare);
    }

    @Test
    public void getAllPersonInfoShouldReturnSomePersonInfos () throws ResourceNotFoundException, IOException, BadResourceException {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setFirstName(firstName);
        personInfo.setLastName(lastName);
        personInfo.setAge(0);
        personInfo.setMedications(medications);
        personInfo.setAllergies(allergies);
        personInfo.setEmail(email);
        List<PersonInfo> personInfos = new ArrayList<>(List.of(personInfo));

        Mockito.when(this.personService.getPersonByLastName(this.lastName)).thenReturn(this.personDtoList);
        Mockito.when(this.medicalRecordService.getAllMedicalRecordByListOfPersons(this.personDtoList)).thenReturn(this.medicalRecordDtoList);
        List<PersonInfo> personInfoToCompare = this.service.getAllPersonInformations(this.lastName);

        Assertions.assertEquals(personInfos, personInfoToCompare);
    }
}
