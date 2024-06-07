package com.safetynetalerts.repositories;

import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.repository.impl.MedicalRecordRepository;
import com.safetynetalerts.utils.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class MedicalRecordRepositoryTest {
    @InjectMocks
    private MedicalRecordRepository repository;

    @Mock
    private Data data;

    private MedicalRecord medicalRecord;

    private List<MedicalRecord> medicalRecords;

    @BeforeEach
    public void setUp() {
        String firstName = "John";
        String lastName = "Doe";
        String birthDate = "07/05/2000";
        medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
        medicalRecords = List.of(medicalRecord);
    }

    @Test
    public void getAllMedicalRecordsShouldReturnAllMedicalRecords() {
        Mockito.when(this.data.getAllMedicalRecords()).thenReturn(this.medicalRecords);
        List<MedicalRecord> allMedicalRecords = repository.getAllMedicalRecords();

        Assertions.assertEquals(this.medicalRecords, allMedicalRecords);
    }

    @Test
    public void createMedicalRecordShouldCreateMedicalRecord() {
        Mockito.when(this.data.createMedicalRecord(medicalRecord)).thenReturn(medicalRecord);
        MedicalRecord medicalRecordToCompare = this.repository.createMedicalRecord(medicalRecord);

        Assertions.assertEquals(medicalRecord, medicalRecordToCompare);
    }
}
