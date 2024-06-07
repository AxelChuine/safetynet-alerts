package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FireStationControllerTest {


    @Autowired
    private FireStationController controller;

    @MockBean
    private IFireStationService service;

    @MockBean
    private IMedicalRecordService medicalRecordService;

    @MockBean
    private IPersonFirestationService personFirestationService;

    @MockBean
    private Utils utils;



    @Test
    public void createFireStationTest () {
        FireStationDto fireStationDto = new FireStationDto();
        ResponseEntity responseFirestation = this.controller.createFirestation(fireStationDto);
        assertEquals(HttpStatus.CREATED, responseFirestation.getStatusCode());
    }


    @Test
    public void getHeadCountByFireStationTest () throws IOException {
        String address = "14 rue Jean Moulin";
        Person p1 = new Person.PersonBuilder().address(address).build();
        Person p2 = new Person.PersonBuilder().address(address).build();
        Person p3 = new Person.PersonBuilder().address(address).build();
        List<Person> persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        StationNumberDto stationNumberDto = new StationNumberDto(persons, 4, 2);

        Map<String, Integer> mapPersons = new HashMap<>();
        mapPersons.put("mineurs", 4);
        mapPersons.put("majeurs", 2);


        when(this.medicalRecordService.countAllPersons(persons)).thenReturn(mapPersons);
        when(this.personFirestationService.getAllPersonsByFireStation("4")).thenReturn(persons);
        when(this.personFirestationService.getHeadCountByFirestation("4")).thenReturn(stationNumberDto);
        ResponseEntity<StationNumberDto> responseHeadCount = this.controller.getHeadCountByFirestation("4");


        assertEquals(HttpStatus.OK, responseHeadCount.getStatusCode());
    }

    @Test
    public void getCellNumbersTest () throws IOException {
        String stationNumber = "4";
        PhoneAlertDto cellNumbers = new PhoneAlertDto();

        when(this.personFirestationService.getCellNumbers(stationNumber)).thenReturn(cellNumbers);
        ResponseEntity responseCellNumbers = this.controller.getCellNumbers(stationNumber);


        assertEquals(HttpStatus.OK, responseCellNumbers.getStatusCode());
    }

    @Test
    public void getAllPersonsAndMedicalRecordByFirestationTest () throws IOException, ResourceNotFoundException {
        String station1 = "1";
        List<String> fireStations = new ArrayList<>();
        fireStations.add(station1);
        ResponseEntity responsePersonMedicalRecord = this.controller.getAllPersonsAndMedicalRecordByFirestation(fireStations);
        assertEquals(HttpStatus.OK, responsePersonMedicalRecord.getStatusCode());
    }


}
