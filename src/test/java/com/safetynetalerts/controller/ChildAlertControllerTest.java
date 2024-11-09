package com.safetynetalerts.controller;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.service.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = ChildAlertController.class)
@AutoConfigureMockMvc
public class ChildAlertControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonServiceImpl service;

    private final String address = "18 rue du moulin";

    @Test
    public void childAlertShouldReturnHttpStatusOk() throws Exception {
        ChildAlertDto childAlertDto = new ChildAlertDto();
        Mockito.when(this.service.getChildByAddress(address)).thenReturn(List.of(childAlertDto));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/child-alert")
                .param("address", address))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void childAlertShouldReturnHttpStatusNotFound() throws Exception {
        Mockito.when(this.service.getChildByAddress(address)).thenThrow(new ResourceNotFoundException("No people found for address " + address));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/child-alert")
                        .param("address", address))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void childAlertShouldReturnHttpStatusBadRequest() throws Exception {
        Mockito.when(this.service.getChildByAddress(""))
                .thenThrow(new BadResourceException("address not provided exception"));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/child-alert")
                        .param("address", ""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
