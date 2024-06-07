package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MedicalRecordControllerTest {

    @Autowired
    private MedicalRecordController controller;

    @MockBean
    private IMedicalRecordService service;

    @MockBean
    private Utils utils;

    @Test
    public void createMedicalRecordTest () throws ResourceNotFoundException, ResourceAlreadyExistsException {
        MedicalRecordDto medicalRecord = new MedicalRecordDto.MedicalRecordDtoBuilder().build();

        when(this.service.createMedicalRecord(medicalRecord)).thenReturn(medicalRecord);
        ResponseEntity<MedicalRecordDto> responseMedicalRecord = this.controller.createMedicalRecord(medicalRecord);
        assertEquals(HttpStatus.CREATED, responseMedicalRecord.getStatusCode());
    }

    @Test
    public void getAllMedicalRecordsTest () throws IOException {
        MedicalRecordDto m1 = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
        MedicalRecordDto m2 = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
        MedicalRecordDto m3 = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecordDtos.add(m1);
        medicalRecordDtos.add(m2);
        medicalRecordDtos.add(m3);

        when(this.service.getAllMedicalRecords()).thenReturn(medicalRecordDtos);
        ResponseEntity<List<MedicalRecordDto>> responseMedicalRecords = this.controller.getAllMedicalRecords();

        assertEquals(HttpStatus.OK, responseMedicalRecords.getStatusCode());
    }

    @Test
    public void updateMedicalRecordTest () throws IOException {
        List<String> allergies = new ArrayList<>();
        String allergie = "gluten";
        allergies.add("lactose");
        allergies.add("mollusque");
        allergies.add("noix");
        List<String> medications = new ArrayList<>();
        medications.add("paracétamol");
        Person person = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("94 Rue jean moulin").build();
        MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(person.firstName).lastName(person.lastName).medications(medications).allergies(allergies).build();

        when(this.service.getMedicalRecordByFullName(person.firstName, person.lastName)).thenReturn(medicalRecord);
        ResponseEntity responseMedicalRecord = this.controller.updateMedicalRecord(person.firstName, person.lastName, allergie);
        assertEquals(HttpStatus.ACCEPTED, responseMedicalRecord.getStatusCode());
    }

    @Test
    public void deleteMedicalRecordTest () throws IOException {
        List<String> medications = new ArrayList<>();
        medications.add("paracétamol");
        List<String> allergies = new ArrayList<>();
        allergies.add("lactose");
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName("Jean").lastName("Dubois").birthDate("01/01/2001").medications(medications).allergies(allergies).build();
        MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(medicalRecordDto.getFirstName()).lastName(medicalRecordDto.getLastName()).birthDate(medicalRecordDto.getBirthDate()).medications(medicalRecordDto.getMedications()).allergies(medicalRecordDto.getAllergies()).build();


        when(this.service.getMedicalRecordByFullName(medicalRecordDto.getFirstName(), medicalRecordDto.getLastName())).thenReturn(medicalRecord);
        ResponseEntity responseMedicalRecord = this.controller.deleteMedicalRecord(medicalRecordDto.getFirstName(), medicalRecordDto.getLastName());
        assertEquals(HttpStatus.OK, responseMedicalRecord.getStatusCode());
    }
}
