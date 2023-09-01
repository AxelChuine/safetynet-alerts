package com.safetynetalerts.services;


import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.dto.StationNumberDto;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        StationNumberDto stationNumberDto = new StationNumberDto();
        List<Person> persons = new ArrayList<>();
        Person person = new Person.PersonBuilder().build();
        persons.add(person);
        SimplePersonDto simplePerson = this.fireStationService.createSimplePersonDto(person);
        List<SimplePersonDto> simplePersons = new ArrayList<>();
        simplePersons.add(simplePerson);
        Map<String, Integer> mapPersons = new HashMap<>();
        mapPersons.put("majeurs", 4);
        mapPersons.put("mineurs", 4);


        when(this.service.getAllPersonsByFireStation("4")).thenReturn(simplePersons);
        when(this.medicalRecordService.countAllPersons(simplePersons)).thenReturn(mapPersons);
        StationNumberDto stationNumberToCompare = this.service.getHeadCountByFirestation("4");

        assertEquals(stationNumberDto.getPersons(), stationNumberToCompare.getPersons());
        assertEquals(stationNumberDto.getUnderaged(), stationNumberToCompare.getUnderaged());
        assertEquals(stationNumberDto.getAdult(), stationNumberToCompare.getAdult());
    }
}
