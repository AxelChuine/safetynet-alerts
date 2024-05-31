package com.safetynetalerts.utils;

import com.safetynetalerts.models.FireStation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    public void createFireStationShouldCreateFireStation() {
        FireStation fireStationToCompare = this.data.createFireStation(this.fireStation);

        Assertions.assertEquals(this.fireStation, fireStationToCompare);
    }
}
