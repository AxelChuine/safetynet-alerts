package com.safetynetalerts.service.mapper;

import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.models.FireStation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IFireStationMapper {
    IFireStationMapper INSTANCE = Mappers.getMapper(IFireStationMapper.class);

    FireStation firestationDtoToFireStation(FireStationDto fireStationDto);
    FireStationDto fireStationToFireStationDto(FireStation fireStation);
    List<FireStation> fireStationDtoListToFireStationList(List<FireStationDto> fireStationDtoList);
    List<FireStationDto> fireStationListToFireStationDtoList(List<FireStation> fireStationList);
}
