package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.service.IPersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    private String firstName = "Jean";

    private String lastName = "Smith";

    private String address = "18 rue du moulin";

    private String city = "Culver";

    private PersonDto personDto;

    private List<PersonDto> personDtoList;


    private String email = "test@gmail.com";

    @BeforeEach
    public void setUp() {
        this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).address(address).city(city).build();
        this.personDtoList = new ArrayList<>();
        this.personDtoList.add(personDto);
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
        ResponseEntity<List<ChildAlertDto>> childAlertDtoResponse = this.controller.getChildByAddress(address);


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
    public void updatePersonTest() throws Exception {
        when(this.service.updateAddressOfPerson(address, firstName, lastName)).thenReturn(personDto);
        ResponseEntity<PersonDto> responsePerson = this.controller.updateAddressOfPerson(address, firstName, lastName);

        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
        assertEquals(personDto, responsePerson.getBody());
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
    }


    @Test
    public void updateCityOfPersonShouldReturnAPersonDto() throws Exception {
        Mockito.when(this.service.updateCityOfPerson(city, firstName, lastName)).thenReturn(this.personDto);
        ResponseEntity<PersonDto> responseEntity = this.controller.updateCityOfPerson(city, firstName, lastName);

        Assertions.assertEquals(this.personDto, responseEntity.getBody());
    }
}
