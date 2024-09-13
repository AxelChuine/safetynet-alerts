package com.safetynetalerts.controller;

import com.safetynetalerts.dto.FireDto;
import com.safetynetalerts.service.impl.PersonMedicalRecordsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = FireController.class)
@AutoConfigureMockMvc
public class FireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonMedicalRecordsServiceImpl service;

    private String address = "48 rue Jean Moulin";

    @Test
    public void fireEndpointShouldReturnHttpStatusOkIfExists() throws Exception {
        FireDto fireDto = new FireDto();
        Mockito.when(this.service.getAllConcernedPersonsAndTheirInfosByFire(this.address)).thenReturn(fireDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/fire")
                        .param("address", address))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
