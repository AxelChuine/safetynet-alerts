package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.service.IPersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @BeforeEach
    public void setUp() {
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
        String address = "47 rue du Jambon";
        String firstName = "Jean";
        String lastName = "Dubois";
        PersonDto personDto = new PersonDto
                .PersonDtoBuilder()
                .firstName(firstName)
                .lastName(lastName)
                .address(address).build();

        when(this.service.updatePerson(firstName, lastName, address)).thenReturn(personDto);
        ResponseEntity<PersonDto> responsePerson = this.controller.updatePerson(address, firstName, lastName);

        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
    }

    @Test
    public void deletePersonTest() throws Exception {
        String firstName = "Jean";
        String lastName = "Dubois";
        PersonDto personDto = new PersonDto.PersonDtoBuilder().build();

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(personDto);
        ResponseEntity responsePerson = this.controller.deletePerson(firstName, lastName);

        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
    }

    @Test
    public void getPersonByFullNameShouldReturnCode200 () throws Exception {
        String firstName = "Jean";
        String lastName = "Dubois";
        PersonDto person = new PersonDto
                .PersonDtoBuilder()
                .firstName(firstName)
                        .lastName(lastName)
                                .build();

        when(this.service.getPersonByFullName(firstName, lastName)).thenReturn(person);
        ResponseEntity responsePerson = this.controller.getPersonByFullName(firstName, lastName);
        assertEquals(HttpStatus.OK, responsePerson.getStatusCode());
    }
}
