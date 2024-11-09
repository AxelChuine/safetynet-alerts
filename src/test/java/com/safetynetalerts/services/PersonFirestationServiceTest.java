package com.safetynetalerts.services;
import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.FireStationServiceImpl;
import com.safetynetalerts.service.MedicalRecordServiceImpl;
import com.safetynetalerts.service.PersonFirestationServiceImpl;
import com.safetynetalerts.service.PersonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonFirestationServiceTest {

    @InjectMocks
    private PersonFirestationServiceImpl service;

    @Mock
    private FireStationServiceImpl fireStationService;

    @Mock
    private MedicalRecordServiceImpl medicalRecordService;

    @Mock
    private PersonServiceImpl personService;

    private String address = "101 rue du puit";

    private String firstName = "John";

    private String lastName = "Smith";

    private Person person;

    private List<Person> persons = new ArrayList<>();

    private Set<String> addresses = new HashSet<>(List.of(address));

    private String stationNumber = "4";

    private List<String> stationNumbers = new ArrayList<>();

    private FireStation fireStation;

    private List<FireStation> fireStations = new ArrayList<>();

    private MedicalRecord medicalRecord;

    private String birthDate = "05/05/2000";

    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        persons.add(person);
        this.fireStation = new FireStation(addresses, stationNumber);
        medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
        medicalRecords.add(medicalRecord);
        fireStations.add(fireStation);
        stationNumbers.add(stationNumber);
    }


    @Test
    public void getHeadCountByFirestationTest () throws IOException, BadResourceException {
        // mocking the firestationService side
        StationNumberDto stationNumberDto = new StationNumberDto();
        String address = "94 rue jean moulin";
        List<PersonDto> personDtoList = new ArrayList<>();
        PersonDto p1 = new PersonDto.PersonDtoBuilder().firstName("prenom1").lastName("nom1").address(address).build();
        PersonDto p2 = new PersonDto.PersonDtoBuilder().firstName("prenom2").lastName("nom2").address(address).build();
        PersonDto p3 = new PersonDto.PersonDtoBuilder().firstName("prenom3").lastName("nom3").address(address).build();
        personDtoList.add(p1);
        personDtoList.add(p2);
        personDtoList.add(p3);
        stationNumberDto.setPersons(personDtoList);
        stationNumberDto.setUnderaged(2);
        stationNumberDto.setAdult(1);

        //mocking the medicalRecordService side
        Map<String, Integer> mapPersons = new HashMap<>();
        mapPersons.put("majeurs", 1);
        mapPersons.put("mineurs", 2);

        //mocking a firestation
        FireStation fireStation = new FireStation();
        Set<String> addresses = new HashSet<>();
        addresses.add(address);
        fireStation.setStationNumber("4");
        fireStation.setAddresses(addresses);

        //mocking a list of medical record
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord m1 = new MedicalRecord.MedicalRecordBuilder().build();
        MedicalRecord m2 = new MedicalRecord.MedicalRecordBuilder().build();
        MedicalRecord m3 = new MedicalRecord.MedicalRecordBuilder().build();
        medicalRecords.add(m1);
        medicalRecords.add(m2);
        medicalRecords.add(m3);


        when(this.personService.getAllPersons()).thenReturn(personDtoList);
        when(this.fireStationService.getFireStationsByStationNumber("4")).thenReturn(fireStation);
        when(this.medicalRecordService.countAllPersons(personDtoList)).thenReturn(mapPersons);
        StationNumberDto stationNumberToCompare = this.service.getHeadCountByFirestation("4");

        assertEquals(stationNumberDto.getPersons(), stationNumberToCompare.getPersons());
        assertEquals(stationNumberDto.getUnderaged(), stationNumberToCompare.getUnderaged());
        assertEquals(stationNumberDto.getPersons(), stationNumberToCompare.getPersons());
    }

    @Test
    public void getCellNumbersTest() throws IOException, BadResourceException {
        PhoneAlertDto cellNumbers = new PhoneAlertDto();
        String phone = "04";
        cellNumbers.getCellNumbers().add(phone);
        List<PersonDto> personDtoList = new ArrayList<>();
        String address = "95 rue jean moulin";
        PersonDto personDto = new PersonDto.PersonDtoBuilder().address(address).phone(phone).build();
        personDtoList.add(personDto);
        Set<String> addresses = new HashSet<>();
        addresses.add(address);
        String stationNumber = "4";
        FireStation fireStation = new FireStation(addresses, stationNumber);

        when(this.fireStationService.getFireStationsByStationNumber("4")).thenReturn(fireStation);
        when(this.personService.getAllPersons()).thenReturn(personDtoList);
        PhoneAlertDto cellNumbersToCompare = this.service.getCellNumbers("4");

        assertEquals(cellNumbers.getCellNumbers(), cellNumbersToCompare.getCellNumbers());
    }

    // FIXME: test qui échoue
    @Test
    public void getPersonsAndMedicalRecordsByFirestationTest () throws IOException, ResourceNotFoundException, BadResourceException {
        List<String> medications = new ArrayList<>();
        String medication = "paracétamol";
        medications.add(medication);
        String allergie = "lactose";
        List<String> allergies = new ArrayList<>();
        allergies.add(allergie);
        List<PersonMedicalRecordDto> personMedicalRecordDtos = new ArrayList<>();
        String firstName = "Jean";
        String lastName = "Dubois";
        PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto(firstName, lastName, "04", 0, medications, allergies);
        personMedicalRecordDtos.add(personMedicalRecordDto);

        List<PersonDto> personDtoList = new ArrayList<>();
        PersonDto p1 = new PersonDto.PersonDtoBuilder().firstName("Jean").lastName("Dubois").address(address).phone("04").build();
        personDtoList.add(p1);
        MedicalRecord m1 = new MedicalRecord.MedicalRecordBuilder().firstName("Jean").lastName("Dubois").medications(medications).allergies(allergies).birthDate("09/29/1998").build();

        String birthDate = "01/01/2001";
        MedicalRecordDto medicalRecord = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).allergies(allergies).medications(medications).build();


        when(this.personService.getAllPersons()).thenReturn(personDtoList);
        when(this.fireStationService.getFireStationsByStationNumber(stationNumber)).thenReturn(fireStation);
        when(this.medicalRecordService.getMedicalRecordByFullName(firstName, lastName)).thenReturn(medicalRecord);
        List<PersonMedicalRecordDto> personMedicalRecordDtosToCompare = this.service.getPersonsAndMedicalRecordsByFirestation(this.stationNumbers);

        assertEquals(personMedicalRecordDtos, personMedicalRecordDtosToCompare);
    }

    @Test
    public void getPersonsAndMedicalRecordsByFirestationShouldThrowBadRequestIfTheParameterIsNullOrVoid () {
        BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.service.getPersonsAndMedicalRecordsByFirestation(List.of()));

        Assertions.assertEquals(exception.getMessage(), "No firestation(s) provided");
        Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }
}
