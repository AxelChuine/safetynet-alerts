package com.safetynetalerts.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.impl.FireStationServiceImpl;
import com.safetynetalerts.service.impl.PersonServiceImpl;
import com.safetynetalerts.utils.Utils;

@SpringBootTest
class PersonServiceTest {

	// FIXME: this.utils is null

	@MockBean
	private Utils utils;

	@Autowired
	private PersonServiceImpl service;

	@MockBean
	private FireStationServiceImpl firestationService;

	@MockBean
	private MedicalRecordServiceImplTest medicalRecordService;

	@Test
	void getAllPersonsByFireStationTest() throws IOException {
		List<Person> personsToCompare = utils.getAllPersons();
		List<FireStation> firestations = this.firestationService.getAllFireStations();
		List<Person> personsToReturn = new ArrayList<>();
		for (Person person : personsToCompare) {
			for (FireStation firestation : firestations) {
				// if (person.address.equals(firestation.getAddress())) {
				personsToReturn.add(person);
			}
		}

		assertEquals(service.getAllPersonsByFireStation("4"), personsToReturn);
	}

	@Test
	void getAllPersonsUnder18ByFireStationTest() {

	}

}
