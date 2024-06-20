package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireDto;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
import com.safetynetalerts.service.IPersonMedicalRecordsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FireStationControllerTest {


    @Autowired
    private FireStationController controller;

    @MockBean
    private IPersonMedicalRecordsService personMedicalRecordsService;

    @MockBean
    private IMedicalRecordService medicalRecordService;

    @MockBean
    private IPersonFirestationService personFirestationService;

   private String address = "48 rue Jean Moulin";

   private String stationNumber = "17";

   private Set<String> addresses = new HashSet<>();

   private String firstName = "Jean";

   private String lastName = "Moulin";

   private String birthDate = "01/01/2000";

   private Integer age = 0;

   private List<String> allergies;

   private List<String> medications;

   private String allergie;

   private String medication;

   private MedicalRecord medicalRecord;

   private FireStationDto fireStationDto;

   @BeforeEach
   void setUp() {
       allergies = new ArrayList<>();
       allergies.add(allergie);
       medications = new ArrayList<>();
       medications.add(medication);
       this.medicalRecord = new MedicalRecord.MedicalRecordBuilder()
               .firstName(firstName)
               .lastName(lastName)
               .birthDate(birthDate)
               .allergies(allergies)
               .medications(medications)
               .build();
       this.fireStationDto = new FireStationDto(addresses, stationNumber);
   }



    @Test
    public void createFireStationTest () {
        FireStationDto fireStationDto = new FireStationDto();
        ResponseEntity responseFirestation = this.controller.createFirestation(fireStationDto);
        assertEquals(HttpStatus.CREATED, responseFirestation.getStatusCode());
    }


    @Test
    public void getHeadCountByFireStationTest () throws IOException {
        String address = "14 rue Jean Moulin";
        Person p1 = new Person.PersonBuilder().address(address).build();
        Person p2 = new Person.PersonBuilder().address(address).build();
        Person p3 = new Person.PersonBuilder().address(address).build();
        List<Person> persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        StationNumberDto stationNumberDto = new StationNumberDto(persons, 4, 2);

        Map<String, Integer> mapPersons = new HashMap<>();
        mapPersons.put("mineurs", 4);
        mapPersons.put("majeurs", 2);


        when(this.medicalRecordService.countAllPersons(persons)).thenReturn(mapPersons);
        when(this.personFirestationService.getAllPersonsByFireStation("4")).thenReturn(persons);
        when(this.personFirestationService.getHeadCountByFirestation("4")).thenReturn(stationNumberDto);
        ResponseEntity<StationNumberDto> responseHeadCount = this.controller.getHeadCountByFirestation("4");


        assertEquals(HttpStatus.OK, responseHeadCount.getStatusCode());
    }

    @Test
    public void getCellNumbersTest () throws IOException {
        String stationNumber = "4";
        PhoneAlertDto cellNumbers = new PhoneAlertDto();

        when(this.personFirestationService.getCellNumbers(stationNumber)).thenReturn(cellNumbers);
        ResponseEntity responseCellNumbers = this.controller.getCellNumbers(stationNumber);


        assertEquals(HttpStatus.OK, responseCellNumbers.getStatusCode());
    }

    @Test
    public void getAllPersonsAndMedicalRecordByFirestationTest () throws IOException, ResourceNotFoundException {
        String station1 = "1";
        List<String> fireStations = new ArrayList<>();
        fireStations.add(station1);
        ResponseEntity responsePersonMedicalRecord = this.controller.getAllPersonsAndMedicalRecordByFirestation(fireStations);
        assertEquals(HttpStatus.OK, responsePersonMedicalRecord.getStatusCode());
    }

    @Test
    public void getAllPersonsAndTheirInfosShouldReturnStatusOkAndAListOfPersons () throws IOException, ResourceNotFoundException {
       FireDto fireDto = new FireDto();

        Mockito.when(this.personMedicalRecordsService.getAllConcernedPersonsAndTheirInfosByFire(address)).thenReturn(fireDto);
        ResponseEntity<FireDto> responseFire = this.controller.getAllPersonsAndTheirInfosByAddress(address);

        assertEquals(HttpStatus.OK, responseFire.getStatusCode());
        Assertions.assertEquals(fireDto, responseFire.getBody());
    }


}
