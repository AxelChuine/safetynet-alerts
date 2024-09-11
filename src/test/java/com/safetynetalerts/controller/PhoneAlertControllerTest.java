package com.safetynetalerts.controller;

import com.safetynetalerts.dto.PhoneAlertDto;
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

@WebMvcTest(controllers = PhoneAlertController.class)
@AutoConfigureMockMvc
public class PhoneAlertControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonFirestationServiceImpl service;

    @Test
    public void phoneAlertShouldReturnHttpStatusOk() throws Exception {
        String stationNumber = "4";
        PhoneAlertDto cellNumbers = new PhoneAlertDto();

        Mockito.when(this.service.getCellNumbers(stationNumber)).thenReturn(cellNumbers);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/phone-alert")
                .param("station-number", "4"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
