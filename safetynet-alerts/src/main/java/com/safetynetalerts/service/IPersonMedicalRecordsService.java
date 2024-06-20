package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireDto;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonByFireDto;
import com.safetynetalerts.dto.PersonDto;

import java.io.IOException;
import java.util.List;

public interface IPersonMedicalRecordsService {
    FireDto getAllConcernedPersonsAndTheirInfosByFire(String address) throws ResourceNotFoundException, IOException;

    List<PersonByFireDto> convertToPersonByFireDtoList(List<PersonDto> pPersons, List<MedicalRecordDto> pMedicalRecords) throws IOException, ResourceNotFoundException;
}
