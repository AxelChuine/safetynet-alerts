package com.safetynetalerts.service.impl;

import com.safetynetalerts.dto.FireDto;
import com.safetynetalerts.repository.IMedicalRecordRepository;
import com.safetynetalerts.repository.IPersonRepository;
import com.safetynetalerts.service.IPersonMedicalRecordsService;
import org.springframework.stereotype.Service;

@Service
public class PersonMedicalRecordsServiceImpl implements IPersonMedicalRecordsService {

    private final IMedicalRecordRepository medicalRecordRepository;

    private final IPersonRepository personRepository;

    public PersonMedicalRecordsServiceImpl(IMedicalRecordRepository medicalRecordRepository, IPersonRepository personRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.personRepository = personRepository;
    }

    @Override
    public FireDto getAllConcernedPersonsAndTheirInfosByFire() {
        return null;
    }
}
