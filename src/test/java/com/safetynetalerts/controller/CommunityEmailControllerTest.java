package com.safetynetalerts.controller;

import com.safetynetalerts.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CommunityEmailController.class)
@AutoConfigureMockMvc
public class CommunityEmailControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonServiceImpl service;

    private String address = "18 rue du moulin";

    private String city = "Culver";

    @Test
    public void getAllEmailAddressesByCityShouldReturnHttpStatusOk() throws Exception {
        List<String> emailAddresses = new ArrayList<>();
        emailAddresses.add("test1@gmail.com");
        emailAddresses.add("test2@gmail.com");
        emailAddresses.add("test3@gmail.com");

        when(this.service.getAllEmailAddressesByCity(address)).thenReturn(emailAddresses);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/community-email")
                        .param("city", city))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
