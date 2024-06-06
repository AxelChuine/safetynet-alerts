package com.safetynetalerts.utils;

import com.safetynetalerts.models.FireStation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DataTest {

    @Autowired
    private Data data;

    private FireStation fireStation;

    @BeforeEach
    public void setUp() {
        fireStation = new FireStation();
    }

    @Test
    public void getAllFirestationsShouldReturnAllFirestations() {
        List<FireStation> fireStations = this.data.getFirestations();

        Assertions.assertNotNull(fireStations);
    }

    @Test
    public void createFireStationShouldCreateFireStation() {
        FireStation fireStationToCompare = this.data.createFireStation(this.fireStation);

        Assertions.assertNotNull(fireStationToCompare);
    }
}
