package com.safetynetalerts.controller;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IPersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PersonController controller;

    @MockBean
    private IPersonService service;

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
        Person p1 = new Person.PersonBuilder().build();
        Person p2 = new Person.PersonBuilder().build();
        Person p3 = new Person.PersonBuilder().build();
        List<Person> persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);

        when(this.service.getAllPersons()).thenReturn(persons);
        ResponseEntity<List<Person>> personsResponse = this.controller.getAllPersons();

        assertEquals(HttpStatus.OK, personsResponse.getStatusCode());
        assertEquals(persons, personsResponse.getBody());
    }


    // FIXME: problème de type: apparemment, la fonction ne prend que du personDto et je lui envoie une void.
    @Test
    public void createPersonTest() throws IOException {
        PersonDto personDto = new PersonDto("Jean", "Dubois", "47 rue du Jambon", "Lilles", "62400", "04", "test@gmail.com");
        List<Person> persons = new ArrayList<>();
        Person person = new Person.PersonBuilder().firstName(personDto.getFirstName()).lastName(personDto.getLastName()).address(personDto.getAddress()).city(personDto.getCity()).zip(personDto.getZip()).phone(personDto.getPhone()).email(personDto.getEmail()).build();

        ResponseEntity responsePerson = this.controller.createPerson(personDto);
        assertEquals(HttpStatus.CREATED, responsePerson.getStatusCode());
    }

    @Test
    public void updatePersonTest() throws Exception {
        String address = "47 rue du Jambon";
        String firstName = "Jean";
        String lastName = "Dubois";
        Person person = new Person.PersonBuilder().build();

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(person);
        ResponseEntity responsePerson = this.controller.updatePerson(address, firstName, lastName);
        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
    }

    @Test
    public void updatePersonNotFoundTest() throws Exception {
        String address = "47 rue du Jambon";
        String firstName = "Jean";
        String lastName = "Dubois";

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(null);
        ResponseEntity responsePerson = this.controller.updatePerson(address, firstName, lastName);
        assertEquals(HttpStatus.NOT_FOUND, responsePerson.getStatusCode());
    }

    @Test
    public void deletePersonTest() throws IOException {
        String firstName = "Jean";
        String lastName = "Dubois";
        Person person = new Person.PersonBuilder().build();

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(person);
        ResponseEntity responsePerson = this.controller.deletePerson(firstName, lastName);
        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
    }

    @Test
    public void deletePersonNotExistingTest () throws IOException {
        String firstName = "Jean";
        String lastName = "Duboid";

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(null);
        ResponseEntity responsePerson = this.controller.deletePerson(firstName, lastName);
        assertEquals(HttpStatus.NOT_FOUND, responsePerson.getStatusCode());
    }
}
