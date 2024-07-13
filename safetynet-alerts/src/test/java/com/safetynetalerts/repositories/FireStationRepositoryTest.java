package com.safetynetalerts.repositories;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.repository.impl.FireStationRepository;
import com.safetynetalerts.utils.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class FireStationRepositoryTest {
    @InjectMocks
    private FireStationRepository repository;

    @Mock
    private Data data;

    private FireStation fireStation;

    private List<FireStation> fireStations;

    private String stationNumber = "17";

    private String address = "17 rue jean moulin";

    private Set<String> addresses;

    @BeforeEach
    public void setUp() {
        this.addresses = new HashSet<>();
        this.addresses.add(address);
        fireStation = new FireStation(addresses, stationNumber);
        this.fireStations = new ArrayList<>();
        fireStations.add(fireStation);
    }

    @Test
    public void getAllFireStationsShouldReturnAllFireStations() {
        List<FireStation> fireStations = List.of(fireStation);

        Mockito.when(this.data.getAllFireStations()).thenReturn(fireStations);
        List<FireStation> fireStationsToCompare = repository.getAllFireStations();

        Assertions.assertEquals(fireStations, fireStationsToCompare);
    }

    @Test
    public void createFireStationShouldReturnFireStation() {
        Mockito.when(this.data.createFireStation(fireStation)).thenReturn(fireStation);
        FireStation firestationToCompare = this.repository.createFireStation(fireStation);

        Assertions.assertEquals(this.fireStation, firestationToCompare);
    }

    @Test
    public void updateFireStationShouldReturnFireStation() {
        FireStation oldFirestation = new FireStation(this.addresses, "4");

        Mockito.when(this.data.saveFirestation(oldFirestation, this.fireStation)).thenReturn(this.fireStation);
        FireStation firestationToCompare = this.repository.save(oldFirestation, fireStation);

        Assertions.assertEquals(this.fireStation, firestationToCompare);
    }

    @Test
    public void deleteFireStationShouldCallTheDataMethod () {
        this.repository.deleteFireStation(this.fireStation);

        Mockito.verify(this.data).deleteFireStation(this.fireStation);
    }
}
