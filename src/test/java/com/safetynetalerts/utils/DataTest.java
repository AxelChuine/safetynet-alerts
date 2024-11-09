package com.safetynetalerts.utils;

import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class DataTest {

    @InjectMocks
    private Data data;

    @Mock
    private Data mockData;


    private String firstName;

    private String lastName;

    private Person person;

    private FireStation fireStation;

    private final String address = "17 rue du moulin";

    private Set<String> addresses;

    private MedicalRecord medicalRecord;


    @BeforeEach
    public void setUp() {
        this.addresses = new HashSet<>();
        this.addresses.add(address);
        String stationNumber = "17";
        fireStation = new FireStation(this.addresses, stationNumber);
        this.firstName = "Jean";
        this.lastName = "Melbourne";
        this.person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);
        String birthDate = "03/05/2000";
        this.medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
    }

    @Test
    public void getAllFirestationsShouldReturnAllFirestations() {
        List<FireStation> fireStations = this.data.getAllFireStations().stream().toList();

        Assertions.assertNotNull(fireStations);
    }

    @Test
    public void deleteFireStationShouldDeleteFireStation() {
        Integer count = this.data.getAllFireStations().size();
        this.data.deleteFireStation(this.fireStation);

        Assertions.assertEquals(count, data.getAllFireStations().size());
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
        MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().build();

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
        Person personToCreate = this.data.createPerson(person);

        Assertions.assertNotNull(personToCreate);
    }

    @Test
    public void saveFirestationShouldSaveFireStation() {
        FireStation oldFireStation = new FireStation(this.addresses, "4");

        FireStation firestationToCompare = this.data.saveFirestation(oldFireStation, this.fireStation);

        Assertions.assertEquals("17", firestationToCompare.getStationNumber());
    }

    @Test
    public void deleteFireStationShouldCallTheMethod () {
        this.mockData.deleteFireStation(this.fireStation);

        Mockito.verify(this.mockData).deleteFireStation(this.fireStation);
    }

    @Test
    public void updateAddressShouldReturnAPersonWithAnUpdatedAddress() {
        String addressToBeUpdated = "28 rue du moulin";
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);

        Person personToCompare = this.data.savePerson(this.person, person);

        Assertions.assertNotNull(personToCompare);
    }

    @Test
    public void deleteMedicalRecordShouldCallDeleteMedicalRecord() {
        Integer count = this.data.getAllMedicalRecords().size();

        this.data.deleteMedicalRecord(this.medicalRecord);

        Assertions.assertEquals(count, data.getAllMedicalRecords().size());
    }

    @Test
    public void saveMedicalRecordShouldSaveAMedicalRecord() {
        Integer count = this.data.getAllMedicalRecords().size();

        MedicalRecord newMedicalRecord = new MedicalRecord.MedicalRecordBuilder().build();
        this.data.saveMedicalRecord(this.medicalRecord, newMedicalRecord);

        Assertions.assertEquals(count + 1, data.getAllMedicalRecords().size());
    }

    @Test
    public void saveListOfPerPersonsListOfPersonShouldReturnTheListOfPerson() {
        List<Person> personList = List.of(person);

        List<Person> personListToCompare = this.data.saveListOfPerPersons(personList);

        Assertions.assertNotNull(personListToCompare);
        Assertions.assertEquals(personList, personListToCompare);
    }

    @Test
    public void createPersonShouldReturnAPerson() throws BadResourceException {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);

        Person personToCompare = this.data.savePerson(person);

        Assertions.assertEquals(person, personToCompare);
    }

    @Test
    public void savePersonShouldThrowBadResourceException() throws BadResourceException {
        String message = "No person provided";

        BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.data.savePerson(null), message);

        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
