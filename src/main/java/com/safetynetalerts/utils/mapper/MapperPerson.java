package com.safetynetalerts.utils.mapper;

import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.models.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MapperPerson {
    public PersonDto toPersonDto(Person person) throws BadResourceException {
        if (Objects.isNull(person)) {
            throw new BadResourceException("No person provided");
        }
        return new PersonDto.PersonDtoBuilder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .address(person.getAddress())
                .city(person.getCity())
                .zip(person.getZip())
                .phone(person.getPhone())
                .email(person.getEmail())
                .build();
    }

    public Person toPerson(PersonDto personDto) throws BadResourceException {
        if (Objects.isNull(personDto)) {
            throw new BadResourceException("No person provided");
        }
        return new Person(personDto.firstName,
                personDto.lastName,
                personDto.address,
                personDto.city,
                personDto.zip,
                personDto.phone,
                personDto.email);
    }

    public List<PersonDto> toPersonDtoList(List<Person> allPersons) throws BadResourceException {
        if (Objects.isNull(allPersons)) {
            throw new BadResourceException("No list of person provided");
        }
        List<PersonDto> personDtoList = new ArrayList<>();
        for (Person person : allPersons) {
            PersonDto personDto = this.toPersonDto(person);
            personDtoList.add(personDto);
        }
        return personDtoList;
    }
}
