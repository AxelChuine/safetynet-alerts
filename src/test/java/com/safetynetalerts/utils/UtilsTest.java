package com.safetynetalerts.utils;

import com.jsoniter.any.Any;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UtilsTest {


    @Autowired
    private Utils utils;

    @MockBean
    private Data data;

    @Test
    public void getAllMedicalRecordTest() throws IOException {
        List<MedicalRecord> medicalRecords = this.utils.getAllMedicalRecords();

        Assertions.assertNotNull(medicalRecords);
        Assertions.assertNotNull(medicalRecords.get(0));
    }

    @Test
    public void getAllFirestationsTest() throws IOException {
        List<FireStation> fireStations = this.utils.getAllFirestations();

        Assertions.assertNotNull(fireStations);
        Assertions.assertNotNull(fireStations.get(0));
    }

    @Test
    public void getAllPersonsTest() throws IOException {
        List<Person> persons = this.utils.getAllPeople();

        Assertions.assertNotNull(persons);
        Assertions.assertNotNull(persons.get(0));
    }

    @Test
    public void readFileShouldReturnAFile() throws IOException {
        String path = "src/main/resources/data/data.json";
        Any any = this.utils.readFile(path);

        Assertions.assertNotNull(any);
    }

}
