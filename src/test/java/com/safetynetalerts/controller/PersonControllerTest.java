package com.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.PersonInfo;
import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.service.PersonMedicalRecordsServiceImpl;
import com.safetynetalerts.service.PersonServiceImpl;
import com.safetynetalerts.utils.mapper.MapperPerson;
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


@WebMvcTest(controllers = PersonController.class)
/*@AutoConfigureMockMvc*/
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonServiceImpl service;

    @MockBean
    private PersonMedicalRecordsServiceImpl personMedicalRecordsService;

    @MockBean
    private MapperPerson mapperPerson;

    private String firstName = "John";

    private String lastName = "Boyd";

    private String address = "1509 Culver St";

    private String city = "Culver";

    private String zip = "97451";

    private String phone = "841-874-6512";

    private String email = "jaboyd@email.com";

    private PersonDto personDto;

    private List<PersonDto> personDtoList;

    PersonInfo personInfo;

    List<PersonInfo> personInfoList;


    @BeforeEach
    public void setUp() {
        this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).city(city).zip(zip).phone(phone).email(email).build();
        this.personDtoList = new ArrayList<>();
        this.personDtoList.add(personDto);
        personInfo = new PersonInfo(this.firstName, lastName, 0, email, null, null);
        personInfoList = new ArrayList<>(List.of(personInfo));
    }

    @Test
    public void getAllPersonsShouldReturnHttpStatusOk() throws Exception {
        Mockito.when(this.service.getAllPersons()).thenReturn(personDtoList);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/person/all")).andExpect(MockMvcResultMatchers.status().isOk());
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
    public void createPersonShouldReturnHttpStatusBadRequest () throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createPersonShouldThrowHttpStatusConflict() throws Exception {
        StringBuilder json = new StringBuilder(new ObjectMapper().writeValueAsString(this.personDto));

        Mockito.when(this.service.savePerson(this.personDto)).thenThrow(new ResourceAlreadyExistsException());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void createPersonShouldThrowHttpStatusBadRequest() throws Exception {
        StringBuilder json = new StringBuilder(new ObjectMapper().writeValueAsString(this.personDto));

        Mockito.when(this.service.addPerson(this.personDto)).thenThrow(new BadResourceException());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/person"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updatePersonShouldReturnHttpStatusOk() throws Exception {
        Mockito.when(this.service.updatePerson(this.personDto)).thenReturn(this.personDto);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.personDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findPersonByFullNameShouldReturnHttpStatusOk() throws Exception {
        Mockito.when(this.service.getPersonByFullName(this.firstName, this.lastName)).thenReturn(this.personDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/person")
                .param("firstName", this.firstName)
                .param("lastName", this.lastName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getPersonInfoShouldReturnHttpStatusOk() throws Exception {
        Mockito.when(this.personMedicalRecordsService.getAllPersonInformations(lastName)).thenReturn(personInfoList);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/person/person-info")
                .param("last-name", this.lastName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deletePersonShouldReturnHttpStatusOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .param("firstName", this.firstName)
                .param("lastName", this.lastName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
