package com.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.PersonInfo;
import com.safetynetalerts.service.impl.PersonMedicalRecordsServiceImpl;
import com.safetynetalerts.service.impl.PersonServiceImpl;
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

import static org.mockito.Mockito.when;


@WebMvcTest(controllers = PersonController.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonServiceImpl service;

    @MockBean
    private PersonMedicalRecordsServiceImpl personMedicalRecordsService;

    private String firstName = "Jean";

    private String lastName = "Smith";

    private String address = "18 rue du moulin";

    private String city = "Culver";

    private PersonDto personDto;

    private List<PersonDto> personDtoList;

    PersonInfo personInfo;

    List<PersonInfo> personInfoList;


    private String email = "test@gmail.com";

    @BeforeEach
    public void setUp() {
        this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).city(city).email(email).build();
        this.personDtoList = new ArrayList<>();
        this.personDtoList.add(personDto);
        personInfo = new PersonInfo(this.firstName, lastName, 0, email, null, null);
        personInfoList = new ArrayList<>(List.of(personInfo));
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
        this.mockMvc.perform(MockMvcRequestBuilders.get("/person-info")
                .param("last-name", this.lastName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
/*
    @Test
    public void getChildAddressTest() throws IOException, ResourceNotFoundException {
        String address = "67 rue Jean moulin";
        PersonDto p1 = new PersonDto.PersonDtoBuilder().build();
        PersonDto p2 = new PersonDto.PersonDtoBuilder().build();
        PersonDto p3 = new PersonDto.PersonDtoBuilder().build();
        List<PersonDto> family = List.of(p1, p2, p3);
        List<ChildAlertDto> childAlertDtos = new ArrayList<>();
        ChildAlertDto child1 = new ChildAlertDto("Jean", "Dubois", 17, family);
        childAlertDtos.add(child1);

        when(this.service.getChildByAddress(address)).thenReturn(childAlertDtos);
        *//*ResponseEntity<List<ChildAlertDto>> childAlertDtoResponse = this.controller.getChildByAddress(address);*//*


        assertEquals(HttpStatus.OK, childAlertDtoResponse.getStatusCode());
        assertEquals(childAlertDtos, childAlertDtoResponse.getBody());
    }

    @Test
    public void getAllPersonsTest() throws IOException {
        List<PersonDto> persons = List.of(new PersonDto.PersonDtoBuilder().build(), new PersonDto.PersonDtoBuilder().build(), new PersonDto.PersonDtoBuilder().build());

        when(this.service.getAllPersons()).thenReturn(persons);
        ResponseEntity<List<PersonDto>> personsResponse = this.controller.getAllPersons();

        assertEquals(HttpStatus.OK, personsResponse.getStatusCode());
        assertEquals(persons, personsResponse.getBody());
    }


    @Test
    public void createPersonTest() throws ResourceAlreadyExistsException {
        PersonDto personDto = new PersonDto.
                PersonDtoBuilder()
                .firstName("Jean")
                .lastName( "Dubois")
                .address("47 rue du Jambon")
                .city( "Lilles")
                .zip( "62400")
                .phone( "04")
                .email( "test@gmail.com").build();

        when(this.service.addPerson(personDto)).thenReturn(personDto);
        ResponseEntity<PersonDto> responsePerson = this.controller.createPerson(personDto);

        assertEquals(HttpStatus.CREATED, responsePerson.getStatusCode());
    }

    @Test
    public void deletePersonTest() throws Exception {
        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(personDto);
        ResponseEntity responsePerson = this.controller.deletePerson(firstName, lastName);

        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
    }

    @Test
    public void getPersonByFullNameShouldReturnCode200 () throws Exception {
        List<PersonDto> personDtos = new ArrayList<>();
        personDtos.add(this.personDto);

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(personDto);
        ResponseEntity<List<PersonDto>> responsePerson = this.controller.getPersonByFullName(firstName, lastName);
        assertEquals(personDtos, responsePerson.getBody());
    }*/
}
