package com.safetynetalerts.services;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.service.impl.FireStationServiceImpl;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FireStationServiceTest {

	@Autowired
	private FireStationServiceImpl service;

	@MockBean
	private Utils utils;

	@Test
	public void getAllFireStationsTest() throws IOException {
		List<FireStation> expectedFireStations = new ArrayList<>();
		FireStation f1 = new FireStation();
		f1.setStationNumber("2");
		f1.setAddresses(new HashSet<>());
		f1.addAddress("1509 Culver St");
		expectedFireStations.add(f1);
		when(utils.getAllFirestations()).thenReturn(expectedFireStations);
		List<FireStation> stationsToCompare = this.service.getAllFireStations();
		assertEquals(expectedFireStations, stationsToCompare);
	}

/*	@Test
	public void createFirestationTest() throws IOException {
		List<FireStation> stations = new ArrayList<>();
		when(this.utils.getFireStations()).thenReturn(stations);
		List<FireStation> fireStations = this.utils.getFireStations();
		List<FireStation> firestationToCompare = this.service.getAllFireStations();
		Set<String> addressesToAdd = new LinkedHashSet<>();
		addressesToAdd.add("17 rue Jean Moulin");
		FireStation firestationToAdd = new FireStation(addressesToAdd, "17");
		fireStations.add(firestationToAdd);
		this.service.createFirestation(firestationToAdd);
		assertEquals(fireStations, firestationToCompare);
	}*/

	@Test
	public void createFirestationTest() {
		// GIVEN
		FireStation firestation = new FireStation();
		firestation.setStationNumber("17");
		List<FireStation> firestations = new ArrayList<>();
		when(this.utils.getFireStations()).thenReturn(firestations);
		// WHEN

		this.service.createFirestation(firestation);
		// THEN
		verify(this.utils, times(1)).getFireStations();
		assertEquals(firestations.get(0).getStationNumber(), "17");
	}

}
