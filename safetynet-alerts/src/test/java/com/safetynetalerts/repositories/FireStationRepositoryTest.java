package com.safetynetalerts.repositories;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.repository.impl.FireStationRepository;
import com.safetynetalerts.utils.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FireStationRepositoryTest {
    @Autowired
    private FireStationRepository repository;

    @Mock
    private Data data;

    private FireStation fireStation;

    @BeforeEach
    public void setUp() {
        fireStation = new FireStation();
    }

    @Test
    public void getAllFireStationsShouldReturnAllFireStations() {
        List<FireStation> fireStations = List.of(fireStation);

        Mockito.when(this.repository.getAllFireStations()).thenReturn(fireStations);
        List<FireStation> fireStationsToCompare = repository.getAllFireStations();

        Assertions.assertEquals(fireStations, fireStationsToCompare);
    }

    @Test
    public void createFireStationShouldReturnFireStation() {
        Mockito.when(this.data.createFireStation(fireStation)).thenReturn(fireStation);
        FireStation firestationToCompare = this.repository.createFireStation(fireStation);

        Assertions.assertEquals(this.fireStation, firestationToCompare);
    }
}
