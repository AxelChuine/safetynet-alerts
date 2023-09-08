package com.safetynetalerts.utils;

import com.safetynetalerts.models.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UtilsTest {

    @Autowired
    private Utils utils;

    @MockBean
    private Data data;

    @Test
    public void getAllPeopleTest() throws IOException {
        List<Person> persons = new ArrayList<>();
        Person person = new Person.PersonBuilder().build();
        persons.add(person);

        when(this.data.getPersons()).thenReturn(persons);
        List<Person> personsToCompare = this.utils.getAllPeople();

        assertEquals(persons, personsToCompare);
    }
}
