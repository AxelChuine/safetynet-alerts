package com.safetynetalerts.repositories;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IPersonRepository;
import com.safetynetalerts.utils.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonRepositoryTest {
    @Autowired
    private IPersonRepository repository;

    @Mock
    private Data data;

    private String firstName = "Jean";

    private String lastName = "Smith";

    private String address = "158 rue de la place";

    private String email = "jean.smith@gmail.com";

    private Person person;

    @BeforeEach
    public void setUp() {
        this.person = new Person.PersonBuilder().firstName(firstName).lastName(lastName).address(address).email(email).build();
    }

    @Test
    public void updateAddressOfPersonShouldReturnAPersonWithUpdatedAddress() throws ResourceAlreadyExistsException {
        String updatedAddress = "123 rue du Bonheur";
        Person updatedPerson = new Person.PersonBuilder().firstName(firstName).lastName(lastName).address(updatedAddress).email(email).build();

        Mockito.when(this.data.savePerson(this.person, updatedPerson)).thenReturn(updatedPerson);
        Person personToCompare = this.repository.savePerson(this.person, updatedPerson);

        Assertions.assertEquals(updatedPerson, personToCompare);
    }
}
