package com.safetynetalerts.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.service.impl.FireStationServiceImpl;
import com.safetynetalerts.utils.Utils;

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
		when(utils.getFireStations()).thenReturn(expectedFireStations);
		List<FireStation> stationsToCompare = this.service.getAllFireStations();
		assertEquals(expectedFireStations, stationsToCompare);
	}

}
