package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.service.impl.PersonFirestationServiceImpl;
import org.junit.jupiter.api.Test;
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

    String badRequest = "No firestation(s) provided";

    @Test
    public void getAllPersonsAndMedicalRecordByFirestationTest () throws Exception {
        String station1 = "1";
        List<String> fireStations = new ArrayList<>();
        fireStations.add(station1);
        List<PersonMedicalRecordDto> personMedicalRecordDtos = new ArrayList<>();
        Mockito.when(this.service.getPersonsAndMedicalRecordsByFirestation(fireStations)).thenReturn(personMedicalRecordDtos);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flood/station")
                        .param("stations", String.valueOf(fireStations)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // FIXME: erreur lance un 200 au lieu du 400
    @Test
    public void getAllPersonsAndMedicalRecordShouldReturnBadRequestIfNoFirestationsAreProvided ()throws Exception {
        Mockito.when(this.service.getPersonsAndMedicalRecordsByFirestation(List.of())).thenThrow(new BadResourceException());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flood/station")
                .param("stations", ""))
                .andExpect(MockMvcResultMatchers.status().isPreconditionFailed());
    }
}
