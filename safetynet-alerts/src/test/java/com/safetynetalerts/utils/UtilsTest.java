package com.safetynetalerts.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UtilsTest {

    @Autowired
    private Utils utils;

    @MockBean
    private Data data;

    /*@Test
    public void getAllPeopleTest() throws IOException {
        List<Person> persons = new ArrayList<>();
        Person person = new Person.PersonBuilder().build();
        persons.add(person);

        this.data.setPersons(persons);
        doNothing().when(this.utils.);
        List<Person> personsToCompare = this.utils.getAllPeople();

        assertEquals(persons, personsToCompare);
    }*/
}
