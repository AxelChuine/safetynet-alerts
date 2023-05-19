package com.safetynetalerts.utils;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.safetynetalerts.models.Person;
import com.safetynetalerts.models.Person.PersonBuilder;

@SpringBootTest
public class UtilsTest {

	@Autowired
	private Utils utils;

	@Test
	void getAllPersonsTest() throws IOException {
		List<Person> persons = this.utils.getAllPeople();
		PersonBuilder p = new PersonBuilder();
		p.firstName("Jean");
		p.lastName("Dubois");
		p.address("94 Allée des fourrés");
		// (Object) persons.add(p);
		when(this.utils.getAllPeople()).thenReturn(persons);
	}

}
