package com.safetynetalerts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MedicalRecordController.class)
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMedicalRecordService service;

    private List<MedicalRecordDto> medicalRecords;

    private MedicalRecordDto medicalRecordDto;

    private String firstName = "Jean";

    private String lastName = "Smith";

    private String birthDate = "01/01/2000";

    @BeforeEach
    public void setUp() {
        this.medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder()
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate).build();
        this.medicalRecords = new ArrayList<>(List.of(medicalRecordDto));
    }


    @Test
    public void createMedicalRecordTest () throws Exception {
        MedicalRecordDto medicalRecord = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
        StringBuilder json = new StringBuilder(new ObjectMapper().writeValueAsString(medicalRecord));

        when(this.service.createMedicalRecord(medicalRecord)).thenReturn(medicalRecord);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/medical-record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void getAllMedicalRecordsShouldReturnHttpStatusOk () throws Exception {
        Mockito.when(this.service.getAllMedicalRecords()).thenReturn(this.medicalRecords);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/medical-records"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getMedicalRecordByFullNameShouldReturnHttpStatusOk () throws Exception {
        Mockito.when(this.service.getMedicalRecordByFullName(firstName, lastName)).thenReturn(this.medicalRecordDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/by-full-name")
                .param("first-name", firstName)
                .param("last-name", lastName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateMedicalRecordShouldReturnHttpStatusOk () throws Exception {
        StringBuilder json = new StringBuilder(new ObjectMapper().writeValueAsString(medicalRecordDto));

        Mockito.when(this.service.updateMedicalRecord(medicalRecordDto)).thenReturn(this.medicalRecordDto);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/medical-record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    /*@TestordsTest
    public void getAllMedicalRec() throws IOException {
        MedicalRecordDto m1 = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
        MedicalRecordDto m2 = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
        MedicalRecordDto m3 = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecordDtos.add(m1);
        medicalRecordDtos.add(m2);
        medicalRecordDtos.add(m3);

        when(this.service.getAllMedicalRecords()).thenReturn(medicalRecordDtos);
        ResponseEntity<List<MedicalRecordDto>> responseMedicalRecords = this.controller.getAllMedicalRecords();

        assertEquals(HttpStatus.OK, responseMedicalRecords.getStatusCode());
    }

    @Test
    public void updateMedicalRecordTest () throws IOException, ResourceNotFoundException {
        List<String> allergies = new ArrayList<>();
        String allergie = "gluten";
        allergies.add("lactose");
        allergies.add("mollusque");
        allergies.add("noix");
        List<String> medications = new ArrayList<>();
        medications.add("paracétamol");
        Person person = new Person.PersonBuilder().firstName("Jean").lastName("Dubois").address("94 Rue jean moulin").build();
        MedicalRecordDto medicalRecord = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(person.firstName).lastName(person.lastName).medications(medications).allergies(allergies).build();

        when(this.service.getMedicalRecordByFullName(person.firstName, person.lastName)).thenReturn(medicalRecord);
        ResponseEntity responseMedicalRecord = this.controller.updateMedicalRecord(person.firstName, person.lastName, allergie);
        assertEquals(HttpStatus.ACCEPTED, responseMedicalRecord.getStatusCode());
    }

    @Test
    public void deleteMedicalRecordTest () throws IOException, ResourceNotFoundException {
        List<String> medications = new ArrayList<>();
        medications.add("paracétamol");
        List<String> allergies = new ArrayList<>();
        allergies.add("lactose");
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName("Jean").lastName("Dubois").birthDate("01/01/2001").medications(medications).allergies(allergies).build();
        MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(medicalRecordDto.getFirstName()).lastName(medicalRecordDto.getLastName()).birthDate(medicalRecordDto.getBirthDate()).medications(medicalRecordDto.getMedications()).allergies(medicalRecordDto.getAllergies()).build();


        when(this.service.getMedicalRecordByFullName(medicalRecordDto.getFirstName(), medicalRecordDto.getLastName())).thenReturn(medicalRecordDto);
        ResponseEntity responseMedicalRecord = this.controller.deleteMedicalRecord(medicalRecordDto.getFirstName(), medicalRecordDto.getLastName());
        assertEquals(HttpStatus.OK, responseMedicalRecord.getStatusCode());
    }*/
}
