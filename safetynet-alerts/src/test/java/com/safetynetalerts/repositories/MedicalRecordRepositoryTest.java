package com.safetynetalerts.repositories;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.repository.impl.MedicalRecordRepository;
import com.safetynetalerts.utils.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MedicalRecordRepositoryTest {
    @InjectMocks
    private MedicalRecordRepository medicalRecordRepository;

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
        List<MedicalRecord> allMedicalRecords = medicalRecordRepository.getAllMedicalRecords();

        Assertions.assertEquals(this.medicalRecords, allMedicalRecords);
    }
}
