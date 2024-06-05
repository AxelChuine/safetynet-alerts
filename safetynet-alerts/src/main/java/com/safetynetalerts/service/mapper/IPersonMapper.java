package com.safetynetalerts.service.mapper;

import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IPersonMapper {
    IPersonMapper INSTANCE = Mappers.getMapper(IPersonMapper.class);

    Person personDtoToPerson(PersonDto personDto);
    PersonDto personToPersonDto(Person person);
    List<Person> personDtoToPersonList(List<PersonDto> personDtoList);
    List<PersonDto> personListToPersonDtoList(List<Person> personList);
}
