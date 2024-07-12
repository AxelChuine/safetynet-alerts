package com.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.mockito.Mockito.when;


@WebMvcTest(controllers = PersonController.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonServiceImpl service;

    private String firstName = "Jean";

    private String lastName = "Smith";

    private String address = "18 rue du moulin";

    private String city = "Culver";

    private PersonDto personDto;

    private List<PersonDto> personDtoList;


    private String email = "test@gmail.com";

    @BeforeEach
    public void setUp() {
        this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).city(city).email(email).build();
        this.personDtoList = new ArrayList<>();
        this.personDtoList.add(personDto);
    }


    @Test
    public void getAllEmailAddressesByCityShouldReturnHttpStatusOk() throws Exception {
        List<String> emailAddresses = new ArrayList<>();
        emailAddresses.add("test1@gmail.com");
        emailAddresses.add("test2@gmail.com");
        emailAddresses.add("test3@gmail.com");

        when(this.service.getAllEmailAddressesByCity(address)).thenReturn(emailAddresses);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail").param("city", "Culver")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getChildAlertShouldReturnHttpStatusOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/childAlert")
                .param("address", this.address))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllPersonsShouldReturnHttpStatusOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/all")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createPersonShouldReturnHttpStatusCreated () throws Exception {
        StringBuilder json = new StringBuilder(new ObjectMapper().writeValueAsString(this.personDto));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void updatePersonShouldReturnHttpStatusOk() throws Exception {
        StringBuilder json = new StringBuilder(new ObjectMapper().writeValueAsString(this.personDto));
        this.mockMvc.perform(MockMvcRequestBuilders.put("/update-person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // FIXME: NullPointerException (???)
    @Test
    public void findPersonByFullNameShouldReturnHttpStatusOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/person")
                .param("firstName", this.firstName)
                .param("lastName", this.lastName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
