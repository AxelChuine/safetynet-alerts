package com.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.service.MedicalRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MedicalRecordController.class)
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordServiceImpl service;

    private List<MedicalRecordDto> medicalRecords;

    private MedicalRecordDto medicalRecordDto;

    private String firstName = "Jean";

    private String lastName = "Smith";

    private String birthDate = "01/01/2000";

    @BeforeEach
    public void setUp() {
        this.medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder()
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate).build();
        this.medicalRecords = new ArrayList<>(List.of(medicalRecordDto));
    }


    @Test
    public void createMedicalRecordTest () throws Exception {
        MedicalRecordDto medicalRecord = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
        StringBuilder json = new StringBuilder(new ObjectMapper().writeValueAsString(medicalRecord));

        when(this.service.createMedicalRecord(medicalRecord)).thenReturn(medicalRecord);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/medical-record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void getAllMedicalRecordsShouldReturnHttpStatusOk () throws Exception {
        Mockito.when(this.service.getAllMedicalRecords()).thenReturn(this.medicalRecords);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/medical-records"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getMedicalRecordByFullNameShouldReturnHttpStatusOk () throws Exception {
        Mockito.when(this.service.getMedicalRecordByFullName(firstName, lastName)).thenReturn(this.medicalRecordDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/by-full-name")
                .param("first-name", firstName)
                .param("last-name", lastName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateMedicalRecordShouldReturnHttpStatusOk () throws Exception {
        StringBuilder json = new StringBuilder(new ObjectMapper().writeValueAsString(medicalRecordDto));

        Mockito.when(this.service.updateMedicalRecord(medicalRecordDto)).thenReturn(this.medicalRecordDto);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/medical-record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    public void deleteMedicalRecordShouldReturnHttpStatusOk () throws Exception {
        StringBuilder json = new StringBuilder(new ObjectMapper().writeValueAsString(medicalRecordDto));

        Mockito.when(this.service.getMedicalRecordByFullName(firstName, lastName)).thenReturn(this.medicalRecordDto);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/medical-record")
                .param("first-name", firstName)
                .param("last-name", lastName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
