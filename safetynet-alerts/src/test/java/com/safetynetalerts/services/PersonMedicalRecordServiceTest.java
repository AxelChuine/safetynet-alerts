package com.safetynetalerts.services;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireDto;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonByFireDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonMedicalRecordsService;
import com.safetynetalerts.service.IPersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@SpringBootTest
public class PersonMedicalRecordServiceTest {
    @Autowired
    private IPersonMedicalRecordsService service;

    @MockBean
    private IPersonService personService;

    @MockBean
    private IMedicalRecordService medicalRecordService;

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

    private List<PersonDto> personDtoList;

    private List<MedicalRecord> medicalRecordList;

    private List<MedicalRecordDto> medicalRecordDtoList;

    @BeforeEach
    public void setUp() {
        this.person = new Person.PersonBuilder().firstName(firstName).lastName(lastName).address(address).build();
        this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).build();
        this.persons = List.of(person);
        this.personDtoList = List.of(personDto);
        this.medicalRecord = new MedicalRecord();
        this.medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
        this.medicalRecordList = List.of(medicalRecord);
        this.medicalRecordDtoList = List.of(medicalRecordDto);
    }

    // TODO: test à faire
    @Test
    public void getAllConcernedPersonsAndTheirInfosByFireShouldReturnAFireDtoObject () throws ResourceNotFoundException {
        String firstName = "John";
        String lastName = "Smith";
        PersonByFireDto personByFireDto = new PersonByFireDto();
        personByFireDto.setFirstName(firstName);
        personByFireDto.setLastName(lastName);
        FireDto fireDto = new FireDto(17, List.of(personByFireDto));

        Mockito.when(this.personService.getPersonsByAddress(address)).thenReturn(this.personDtoList);
        Mockito.when(this.medicalRecordService.getAllMedicalRecordByListOfPersons(this.personDtoList)).thenReturn(this.medicalRecordDtoList);
        FireDto fireToCompare = this.service.getAllConcernedPersonsAndTheirInfosByFire();

        Assertions.assertEquals(fireDto, fireToCompare);
    }
}
