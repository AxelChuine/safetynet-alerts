package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.service.MedicalRecordServiceImpl;
import com.safetynetalerts.service.PersonFirestationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = { FloodController.class })
@AutoConfigureMockMvc
public class FloodControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonFirestationServiceImpl service;

    @Mock
    private MedicalRecordServiceImpl medicalRecordService;


    String badRequest = "No firestation(s) provided";

    @Test
    public void getAllPersonsAndMedicalRecordByFirestationTest () throws Exception {
        String station1 = "4";
        String station2 = "2";
        List<String> fireStations = List.of(station1, station2);
        List<PersonMedicalRecordDto> personMedicalRecordDtos = new ArrayList<>();
        Mockito.when(this.service.getPersonsAndMedicalRecordsByFirestation(fireStations)).thenReturn(personMedicalRecordDtos);
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
