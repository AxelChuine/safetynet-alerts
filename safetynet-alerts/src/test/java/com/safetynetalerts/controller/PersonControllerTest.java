package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonControllerTest {

    @InjectMocks
    private PersonController controller;

    @Mock
    private IPersonService service;

    @MockBean
    private Utils utils;

    @MockBean
    private Data data;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getAllEmailAddressesTest() throws Exception {
        String address = "67 rue Jean moulin";
        List<String> emailAddresses = new ArrayList<>();
        emailAddresses.add("test1@gmail.com");
        emailAddresses.add("test2@gmail.com");
        emailAddresses.add("test3@gmail.com");

        when(this.service.getAllEmailAddressesByCity(address)).thenReturn(emailAddresses);
        ResponseEntity<List<String>> addressesToCompare = this.controller.getAllEmailAddresses(address);

        assertEquals(HttpStatus.OK, addressesToCompare.getStatusCode());
        assertEquals(emailAddresses, addressesToCompare.getBody());
    }

    @Test
    public void getChildAddressTest() throws IOException {
        String address = "67 rue Jean moulin";
        Person p1 = new Person.PersonBuilder().build();
        Person p2 = new Person.PersonBuilder().build();
        Person p3 = new Person.PersonBuilder().build();
        List<Person> family = new ArrayList<>();
        family.add(p1);
        family.add(p2);
        family.add(p3);
        List<ChildAlertDto> childAlertDtos = new ArrayList<>();
        ChildAlertDto child1 = new ChildAlertDto("Jean", "Dubois", 17, family);
        childAlertDtos.add(child1);

        when(this.service.getChildByAddress(address)).thenReturn(childAlertDtos);
        ResponseEntity<List<ChildAlertDto>> childAlertDtoResponse = this.controller.getChildByAddress(address);


        assertEquals(HttpStatus.OK, childAlertDtoResponse.getStatusCode());
        assertEquals(childAlertDtos, childAlertDtoResponse.getBody());
    }

    @Test
    public void getAllPersonsTest() throws IOException {
        List<PersonDto> persons = List.of(new PersonDto(), new PersonDto(), new PersonDto());

        when(this.service.getAllPersons()).thenReturn(persons);
        ResponseEntity<List<PersonDto>> personsResponse = this.controller.getAllPersons();

        assertEquals(HttpStatus.OK, personsResponse.getStatusCode());
        assertEquals(persons, personsResponse.getBody());
    }


    @Test
    public void createPersonTest() throws ResourceAlreadyExistsException {
        PersonDto personDto = new PersonDto("Jean", "Dubois", "47 rue du Jambon", "Lilles", "62400", "04", "test@gmail.com");

        when(this.service.addPerson(personDto)).thenReturn(personDto);
        ResponseEntity<PersonDto> responsePerson = this.controller.createPerson(personDto);

        assertEquals(HttpStatus.CREATED, responsePerson.getStatusCode());
    }

    @Test
    public void updatePersonTest() throws Exception {
        String address = "47 rue du Jambon";
        String firstName = "Jean";
        String lastName = "Dubois";
        PersonDto personDto = new PersonDto();
        personDto.setFirstName(firstName);
        personDto.setLastName(lastName);
        personDto.setAddress(address);

        when(this.service.updatePerson(firstName, lastName, address)).thenReturn(personDto);
        ResponseEntity<PersonDto> responsePerson = this.controller.updatePerson(address, firstName, lastName);

        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
    }

    @Test
    public void deletePersonTest() throws Exception {
        String firstName = "Jean";
        String lastName = "Dubois";
        PersonDto personDto = new PersonDto();

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(personDto);
        ResponseEntity responsePerson = this.controller.deletePerson(firstName, lastName);

        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
    }

    @Test
    public void deletePersonNotExistingTest () throws Exception {
        String firstName = "Jean";
        String lastName = "Duboid";

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(null);
        ResponseEntity responsePerson = this.controller.deletePerson(firstName, lastName);

        assertEquals(HttpStatus.NOT_FOUND, responsePerson.getStatusCode());
    }

    @Test
    public void getPersonByFullNameShouldReturnCode200 () throws Exception {
        String firstName = "Jean";
        String lastName = "Dubois";
        PersonDto person = new PersonDto(firstName, lastName, null, null, null, null, null);

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(person);
        ResponseEntity responsePerson = this.controller.getPersonByFullName(firstName, lastName);
        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
    }
}
