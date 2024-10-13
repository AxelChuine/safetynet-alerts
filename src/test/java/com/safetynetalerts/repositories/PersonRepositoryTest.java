package com.safetynetalerts.repositories;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.PersonRepositoryImpl;
import com.safetynetalerts.utils.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTest {
    @InjectMocks
    private PersonRepositoryImpl repository;

    @Mock
    private Data data;

    private final String firstName = "Jean";

    private final String lastName = "Smith";

    private final String email = "jean.smith@gmail.com";

    private Person person;

    private List<Person> personList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        String address = "158 rue de la place";
        this.person = new Person.PersonBuilder().firstName(firstName).lastName(lastName).address(address).email(email).build();
        this.personList = List.of(person);
    }

    @Test
    public void updateAddressOfPersonShouldReturnAPersonWithUpdatedAddress() throws ResourceAlreadyExistsException {
        String updatedAddress = "123 rue du Bonheur";
        Person updatedPerson = new Person.PersonBuilder().firstName(firstName).lastName(lastName).address(updatedAddress).email(email).build();

        Mockito.when(this.data.savePerson(this.person, updatedPerson)).thenReturn(updatedPerson);
        Person personToCompare = this.repository.savePerson(this.person, updatedPerson);

        Assertions.assertEquals(updatedPerson, personToCompare);
    }

    @Test
    public void getAllPersonShouldReturnAListOfPerson() {
        Mockito.when(this.data.getAllPersons()).thenReturn(this.personList);
        List<Person> personListToCompare = this.repository.getAllPersons();

        Assertions.assertEquals(personList, personListToCompare);
    }
}
