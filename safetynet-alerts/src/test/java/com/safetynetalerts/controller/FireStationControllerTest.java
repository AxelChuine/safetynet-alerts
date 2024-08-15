package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
import com.safetynetalerts.service.IPersonMedicalRecordsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FireStationController.class)
@AutoConfigureMockMvc
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPersonFirestationService personFirestationService;

    @MockBean
    private IFireStationService service;

    @MockBean
    private IPersonMedicalRecordsService personMedicalRecordsService;

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

   private FireStation fireStation;

   private List<FireStationDto> fireStationDtos;

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
       this.fireStation = new FireStation(addresses, stationNumber);
       this.fireStationDtos = new ArrayList<>(List.of(this.fireStationDto));
   }



    @Test
    public void createFireStationTest () throws ResourceNotFoundException, ResourceAlreadyExistsException, IOException {

    }


    @Test
    public void getHeadCountByFireStationShouldReturnHttpStatusOk() throws Exception {
       StationNumberDto stationNumberDto = new StationNumberDto();
       Mockito.when(this.personFirestationService.getHeadCountByFirestation("4")).thenReturn(stationNumberDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/firestation")
                .param("stationNumber", "4"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getCellNumbersTest () throws Exception {
        String stationNumber = "4";
        PhoneAlertDto cellNumbers = new PhoneAlertDto();

        when(this.personFirestationService.getCellNumbers(stationNumber)).thenReturn(cellNumbers);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/phone-alert")
                .param("stationNumber", "4"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllPersonsAndMedicalRecordByFirestationTest () throws Exception {
        String station1 = "1";
        List<String> fireStations = new ArrayList<>();
        fireStations.add(station1);
        List<PersonMedicalRecordDto> personMedicalRecordDtos = new ArrayList<>();
        Mockito.when(this.personFirestationService.getPersonsAndMedicalRecordsByFirestation(fireStations)).thenReturn(personMedicalRecordDtos);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flood/stations")
                .param("stations", String.valueOf(fireStations)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllPersonsAndTheirInfosShouldReturnStatusOkAndAListOfPersons () throws Exception {
       FireDto fireDto = new FireDto();
        Mockito.when(this.personMedicalRecordsService.getAllConcernedPersonsAndTheirInfosByFire(this.address)).thenReturn(fireDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/fire")
                .param("address", address))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteFirestationShouldDeleteFireStation() throws IOException, ResourceNotFoundException {
       /*ResponseEntity responseEntity = this.controller.deleteFirestation(this.fireStationDto);

       Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
       Mockito.verify(this.service).deleteFirestation(this.fireStationDto);*/
    }
}
