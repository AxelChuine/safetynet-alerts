package com.safetynetalerts.utils;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UtilsTest {


        @Autowired
        private Utils utils;

        @MockBean
        private Data data;

        @Test
        public void getAllMedicalRecordTest () throws IOException {
                List<MedicalRecord> medicalRecords = this.utils.getAllMedicalRecords();
                MedicalRecord medicalRecord = new MedicalRecord();
                medicalRecord.setFirstName("John");
                medicalRecord.setLastName("Boyd");

                Optional<MedicalRecord> medicalRecordToCompareOptional = medicalRecords.stream().filter(mr -> mr.equals(medicalRecord)).findFirst();

            assertTrue(medicalRecordToCompareOptional.isPresent());
            assertEquals(medicalRecord, medicalRecordToCompareOptional.get());
        }

        @Test
        public void getAllFirestationsTest () throws IOException {
                List<FireStation> fireStations = this.utils.getAllFirestations();
                FireStation fireStation = new FireStation();
                fireStation.setStationNumber("4");

                Optional<FireStation> fireStationOptional = fireStations.stream().filter(fs -> fs.equals(fireStation)).findFirst();

                assertTrue(fireStationOptional.isPresent());
                assertEquals(fireStation, fireStationOptional.get());

        }

        @Test
        public void getAllPersonsTest () throws IOException {
                List<Person> persons = this.utils.getAllPeople();
                Person person = new Person.PersonBuilder().firstName("John").lastName("Boyd").build();

                Optional<Person> personOptional = persons.stream().filter(p -> p.equals(person)).findFirst();

                assertTrue(personOptional.isPresent());
                assertEquals(person, personOptional.get());
        }

}
