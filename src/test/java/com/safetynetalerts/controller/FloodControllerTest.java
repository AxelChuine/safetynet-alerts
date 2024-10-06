package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.service.MedicalRecordServiceImpl;
import com.safetynetalerts.service.PersonFirestationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = FloodController.class)
public class FloodControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonFirestationServiceImpl service;

    @Mock
    private MedicalRecordServiceImpl medicalRecordService;

    private String badRequest = "No firestation(s) provided";
    private String station1 = "4";
    private String station2 = "2";
    private String firstName = "John";
    private String lastName = "Doe";
    private String phone = "04";
    private Integer age = 5;
    private List<String> fireStations = List.of(station1, station2);
    PersonMedicalRecordDto personMedicalRecordDto;
    private List<PersonMedicalRecordDto> personMedicalRecordDtos = new ArrayList<>();

    @BeforeEach
    public void setUp() throws Exception {
        personMedicalRecordDto = new PersonMedicalRecordDto(this.firstName, this.lastName, this.phone, this.age, new ArrayList<>(), new ArrayList<>());
        personMedicalRecordDtos.add(personMedicalRecordDto);
    }



    @Test
    public void getAllPersonsAndMedicalRecordByFirestationTest () throws Exception {
        Mockito.when(this.service.getPersonsAndMedicalRecordsByFirestation(this.fireStations)).thenReturn(personMedicalRecordDtos);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flood/station")
                        .param("stations", "4,2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllPersonsAndMedicalRecordShouldReturnBadRequestIfNoFirestationsAreProvided ()throws Exception {
        Mockito.when(this.service.getPersonsAndMedicalRecordsByFirestation(List.of())).thenThrow(new BadResourceException());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flood/station")
                        .param("stations", ""))
                .andExpect(MockMvcResultMatchers.status().isPreconditionFailed());
    }

    @Test
    public void getAllPersonsAndMedicalRecordsShouldReturnNotFoundWhenNothingIsFound () throws Exception {
        Mockito.when(this.service.getPersonsAndMedicalRecordsByFirestation(List.of())).thenThrow(new ResourceNotFoundException());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flood/station")
                        .param("stations", "4,2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

