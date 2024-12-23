package com.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.service.FireStationServiceImpl;
import com.safetynetalerts.service.PersonFirestationServiceImpl;
import com.safetynetalerts.service.PersonMedicalRecordsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebMvcTest(controllers = FireStationController.class)
@AutoConfigureMockMvc
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonFirestationServiceImpl personFirestationService;

    @MockBean
    private FireStationServiceImpl service;

    @MockBean
    private PersonMedicalRecordsServiceImpl personMedicalRecordsService;

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
    public void createFireStationShouldReturnHttpStatusCreated() throws Exception {
       Mockito.when(this.service.createFirestation(this.fireStationDto)).thenReturn(this.fireStationDto);
       this.mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                .content(new ObjectMapper().writeValueAsString(fireStationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    public void getHeadCountByFireStationShouldReturnHttpStatusOk() throws Exception {
       StationNumberDto stationNumberDto = new StationNumberDto();
       Mockito.when(this.personFirestationService.getHeadCountByFirestation("4")).thenReturn(stationNumberDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/firestation")
                .param("station-number", "4"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteFirestationShouldDeleteFireStation() throws Exception {
       this.mockMvc.perform(MockMvcRequestBuilders.delete("/firestation")
               .contentType(MediaType.APPLICATION_JSON)
               .content(String.valueOf(new ObjectMapper().writeValueAsString(this.fireStationDto))))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllFirestationsShouldReturnHttpStatusOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/firestation/all")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateFirestationShouldReturnHttpStatusOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                .content(new ObjectMapper().writeValueAsString(this.fireStationDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getHeadCountByFirestationShouldReturnHttpStatusNotFound() throws Exception {
       Mockito.when(this.personFirestationService.getHeadCountByFirestation("4")).thenReturn(null);
       this.mockMvc.perform(MockMvcRequestBuilders.get("/firestation")
               .param("station-number", "4"))
               .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}
