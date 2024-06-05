package com.safetynetalerts.service.mapper;

import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.models.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IMedicalRecordMapper {
    IMedicalRecordMapper INSTANCE = Mappers.getMapper(IMedicalRecordMapper.class);
    MedicalRecord medicalRecordDtoToMedicalRecord(MedicalRecordDto medicalRecordDto);
    MedicalRecordDto medicalRecordToMedicalRecordDto(MedicalRecord medicalRecord);
    List<MedicalRecord> medicalRecordsDtoToMedicalRecordList(List<MedicalRecordDto> medicalRecordDtoList);
    List<MedicalRecordDto> medicalRecordListToMedicalRecordsDtoList(List<MedicalRecord> medicalRecordDtoList);
}
