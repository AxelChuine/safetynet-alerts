package com.safetynetalerts.repositories;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceAlreadyExistsException;
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
        this.person = new Person();
        this.person.setFirstName(firstName);
        this.person.setLastName(lastName);
        this.person.setEmail(email);
        this.personList = List.of(person);
    }

    @Test
    public void updateAddressOfPersonShouldReturnAPersonWithUpdatedAddress() throws ResourceAlreadyExistsException {
        String updatedAddress = "123 rue du Bonheur";
        Person updatedPerson = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(updatedAddress);

        Mockito.when(this.data.savePerson(this.person, updatedPerson)).thenReturn(updatedPerson);
        Person personToCompare = this.repository.savePerson(this.person, updatedPerson);

        Assertions.assertEquals(updatedPerson, personToCompare);
        Assertions.assertEquals(updatedPerson.hashCode(), personToCompare.hashCode());
        Assertions.assertEquals(updatedPerson.toString(), personToCompare.toString());
    }

    @Test
    public void getAllPersonShouldReturnAListOfPerson() {
        Mockito.when(this.data.getAllPersons()).thenReturn(this.personList);
        List<Person> personListToCompare = this.repository.getAllPersons();

        Assertions.assertEquals(personList, personListToCompare);
    }

    @Test
    public void savePersonListShouldReturnAListOfPerson() {
        Mockito.when(this.data.saveListOfPerPersons(this.personList)).thenReturn(this.personList);
        List<Person> personListToCompare = this.repository.save(this.personList);

        Assertions.assertEquals(personList, personListToCompare);
    }

    @Test
    public void savePersonShouldReturnAPerson() throws BadResourceException {
        Mockito.when(this.data.savePerson(this.person)).thenReturn(this.person);
        Person personToCompare = this.repository.savePerson(this.person);

        Assertions.assertEquals(this.person, personToCompare);
        Assertions.assertEquals(this.person.hashCode(), personToCompare.hashCode());
        Assertions.assertEquals(this.person.toString(), personToCompare.toString());
    }
}
