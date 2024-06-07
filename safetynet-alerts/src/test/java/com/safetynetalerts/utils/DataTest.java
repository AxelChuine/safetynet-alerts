package com.safetynetalerts.utils;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

    @Test
    public void getAllMedicalRecordsShouldReturnAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = this.data.getAllMedicalRecords();

        Assertions.assertNotNull(medicalRecords);
    }

    @Test
    public void createMedicalRecordShouldCreateMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord();

        MedicalRecord medicalRecordToCompare = this.data.createMedicalRecord(medicalRecord);

        Assertions.assertNotNull(medicalRecordToCompare);
    }

    @Test
    public void getAllPersonsShouldReturnAllPersons() {
        List<Person> persons = this.data.getAllPersons();

        Assertions.assertNotNull(persons);
    }

    @Test
    public void createPersonShouldCreatePerson() {
        Person person = new Person.PersonBuilder().build();
        Person personToCreate = this.data.createPerson(person);

        Assertions.assertNotNull(personToCreate);
    }
}
