package com.safetynetalerts.models;

import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FireStationTest {

    @MockBean
    private Utils utils;

    private String stationNumber = "04";
    private Set<String> addresses;

    private String address = "89 rue Jean Racine";
    private FireStation fireStation = new FireStation();

    @BeforeEach
    public void setFireStation () {
        this.fireStation.setStationNumber(stationNumber);
        this.addresses = new HashSet<>();
        this.addresses.add(this.address);
        this.fireStation.setAddresses(addresses);
    }

    @Test
    public void addAddressTest () {
        FireStation fireStation1 = new FireStation();
        Set<String> addressesToCompare = new HashSet<>();
        fireStation1.setAddresses(addressesToCompare);
        fireStation1.addAddress(this.address);

        assertEquals(this.fireStation.getAddresses(), fireStation1.getAddresses());
    }

    @Test
    public void hashCodeFireStationTest () {
        FireStation fireStation1 = this.fireStation;

        assertEquals(this.fireStation.hashCode(), fireStation1.hashCode());
    }


}
