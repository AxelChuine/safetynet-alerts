package com.safetynetalerts.services;


import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
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
    private Data data;

    @MockBean
    private Utils utils;


    @Test
    public void getHeadCountByFirestationTest () throws IOException {
        // mocking the firestationService side
        StationNumberDto stationNumberDto = new StationNumberDto();
        List<Person> persons = new ArrayList<>();
        Person p1 = new Person.PersonBuilder().build();
        Person p2 = new Person.PersonBuilder().build();
        Person p3 = new Person.PersonBuilder().build();
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
        addresses.add("94 rue jean moulin");
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


        when(this.data.getPersons()).thenReturn(persons);
        when(this.fireStationService.getFireStationsByStationNumber("4")).thenReturn(fireStation);
        when(this.medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecords);
        when(this.medicalRecordService.countAllPersons(persons)).thenReturn(mapPersons);
        StationNumberDto stationNumberToCompare = this.service.getHeadCountByFirestation("4");

        assertEquals(stationNumberDto.getPersons(), stationNumberToCompare.getPersons());
        assertEquals(stationNumberDto.getUnderaged(), stationNumberToCompare.getUnderaged());
        assertEquals(stationNumberDto.getPersons(), stationNumberToCompare.getPersons());
    }
}
