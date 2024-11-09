package com.safetynetalerts.services;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceNotFoundException;
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
import org.springframework.http.HttpStatus;

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

    private final String firstName = "John";

    private final String lastName = "Smith";

    private final String birthDate = "01/01/2010";

    private List<String> allergies;

    private List<String> medications;

    String address = "45 rue jean moulin";

    private Person person;

    private PersonDto personDto;

    private MedicalRecord medicalRecord;

    private MedicalRecordDto medicalRecordDto;

    private List<PersonDto> personDtoList = new ArrayList<>();

    private List<MedicalRecordDto> medicalRecordDtoList;

    private final String stationNumber = "17";

    private Set<String> addresses;

    private final String phone = "15465132";

    private final String email = "test@test.com";

    @BeforeEach
    public void setUp() {
        String city = "Culver";
        String zip = "123456";
        this.person = new Person(firstName, lastName, address, city, zip, phone, email);
        this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).email(email).address(address).phone(phone).build();
        List<Person> persons = List.of(person);
        this.personDtoList = List.of(personDto);
        this.medicalRecord = new MedicalRecord.MedicalRecordBuilder().build();
        this.medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
        List<MedicalRecord> medicalRecordList = List.of(medicalRecord);
        this.medicalRecordDtoList = List.of(medicalRecordDto);
        FireStationDto fireStationDto = new FireStationDto(addresses, stationNumber);
    }

    @Test
    public void getAllConcernedPersonsAndTheirInfosByFireShouldReturnAFireDtoObject () throws ResourceNotFoundException, IOException, BadResourceException {
        Integer age = 0;
        PersonByFireDto personByFireDto = new PersonByFireDto();
        personByFireDto.setFirstName(firstName);
        personByFireDto.setLastName(lastName);
        personByFireDto.setAge(age);
        personByFireDto.setCellNumber(phone);
        FireDto fireDto = new FireDto(this.stationNumber, List.of(personByFireDto));
        List<Person> persons = List.of(person);

        Mockito.when(this.personService.getPersonsByAddress(address)).thenReturn(this.personDtoList);
        Mockito.when(this.medicalRecordService.getAllMedicalRecordByListOfPersons(this.personDtoList)).thenReturn(this.medicalRecordDtoList);
        Mockito.when(this.fireStationService.getStationNumberByAddress(address)).thenReturn(stationNumber);
        FireDto fireToCompare = this.service.getAllConcernedPersonsAndTheirInfosByFire(address);

        Assertions.assertEquals(fireDto, fireToCompare);
    }

    @Test
    public void toPersonByFireDtoShouldReturnAPersonByFireDto() throws BadResourceException, ResourceNotFoundException {
        PersonByFireDto personByFireDto = new PersonByFireDto(this.person.getFirstName(), this.person.getLastName(), this.person.getPhone(), 0, this.medicalRecord.getMedications(), this.medicalRecord.getAllergies());

        PersonByFireDto personByFireToCompare = this.service.toPersonByFireDto(this.personDto, this.medicalRecordDto);

        Assertions.assertEquals(personByFireDto, personByFireToCompare);
    }

    @Test
    public void convertListOfPersonsAndMedicalRecordsToPersonsByFireDtosShouldReturnAListOfPersonByFireDtoObject () throws ResourceNotFoundException, IOException, BadResourceException {
        Integer age = 1;
        PersonByFireDto personByFireDto = new PersonByFireDto();
        personByFireDto.setFirstName(firstName);
        personByFireDto.setLastName(lastName);
        personByFireDto.setAge(age);
        personByFireDto.setCellNumber(phone);
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

    @Test
    public void getAllPersonsInformationsShouldThrowBadResourceException () throws BadResourceException {
        String message = "The last name is null";

        BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.service.getAllPersonInformations(null), message);

        Assertions.assertEquals(exception.getMessage(), message);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void convertToPersonsByFireDtoListShouldThrowResourceNotFoundException () throws ResourceNotFoundException, BadResourceException {
        String message = "No person found";
        MedicalRecordDto medicalRecordDto1 = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
        List<MedicalRecordDto> medicalRecordDtoList = List.of(medicalRecordDto1);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.convertToPersonByFireDtoList(List.of(), medicalRecordDtoList), message);

        Assertions.assertEquals(exception.getMessage(), message);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
