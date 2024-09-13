package com.safetynetalerts.controller;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.service.impl.PersonServiceImpl;
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

    private String address = "18 rue du moulin";

    @Test
    public void childAlertShouldReturnHttpStatusOk() throws Exception {
        ChildAlertDto childAlertDto = new ChildAlertDto();
        Mockito.when(this.service.getChildByAddress(address)).thenReturn(List.of(childAlertDto));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/child-alert")
                .param("address", address))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
