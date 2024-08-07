package com.safetynetalerts.utils;

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

    private String address = "17 rue du moulin";

    private Set<String> addresses;

    private String stationNumber = "17";

    private String birthDate = "03/05/2000";

    private MedicalRecord medicalRecord;

    @BeforeEach
    public void setUp() {
        this.addresses = new HashSet<>();
        this.addresses.add(address);
        fireStation = new FireStation(this.addresses, stationNumber);
        this.firstName = "Jean";
        this.lastName = "Melbourne";
        this.person = new Person.PersonBuilder().firstName(firstName).lastName(lastName).address(address).build();
        this.medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
    }

    @Test
    public void getAllFirestationsShouldReturnAllFirestations() {
        List<FireStation> fireStations = this.data.getFirestations();

        Assertions.assertNotNull(fireStations);
    }

    @Test
    public void deleteFireStationShouldDeleteFireStation() {
        this.mockData.deleteFireStation(this.fireStation);

        Mockito.verify(this.mockData).deleteFireStation(this.fireStation);
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
        Person person = new Person.PersonBuilder().firstName(firstName).lastName(lastName).address(address).build();

        Person personToCompare = this.data.savePerson(this.person, person);

        Assertions.assertNotNull(personToCompare);
    }

    @Test
    public void deleteMedicalRecordShouldCallDeleteMedicalRecord() {
        this.mockData.deleteMedicalRecord(this.medicalRecord);

        Mockito.verify(this.mockData).deleteMedicalRecord(medicalRecord);
    }

    @Test
    public void saveMedicalRecordShouldSaveAMedicalRecord() {
        MedicalRecord newMedicalRecord = new MedicalRecord();
        this.mockData.saveMedicalRecord(this.medicalRecord, newMedicalRecord);

        Mockito.verify(this.mockData).saveMedicalRecord(this.medicalRecord, newMedicalRecord);
    }
}
