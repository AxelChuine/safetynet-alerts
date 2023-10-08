package com.safetynetalerts.dto;

import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class FireStationDtoTest {

    @MockBean
    private Utils utils;


    @Test
    public void equalsBetweenTwoObjectsTest () {
        Set<String> addresses = new TreeSet<>();
        FireStationDto fireStationDto1 = new FireStationDto(addresses, "2");
        FireStationDto fireStationDto2 = new FireStationDto(addresses, "2");
        assertEquals(fireStationDto1, fireStationDto2);
    }

    @Test
    public void twoObjectsNotEqualsTest() {
        Set<String> addresses = new TreeSet<>();
        FireStationDto fireStationDto1 = new FireStationDto(addresses, "1");
        FireStationDto fireStationDto2 = new FireStationDto(addresses, "2");
        assertNotEquals(fireStationDto1, fireStationDto2);
    }

    @Test
    public void hashCodeFireStationDtoTest () {
        String address = "89 rue Jean Racine";
        Set<String> addresses = new HashSet<>();
        addresses.add(address);
        String stationNumber = "04";
        FireStationDto fireStationDto = new FireStationDto(addresses, stationNumber);
        FireStationDto fireStationDto1 = fireStationDto;

        assertEquals(fireStationDto.hashCode(), fireStationDto1.hashCode());
    }

}
