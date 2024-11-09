package com.safetynetalerts.utils.mapper;

import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.models.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MapperPersonTest {
    @InjectMocks
    private MapperPerson mapper;

    private Person person;

    private PersonDto personDto;

    private final List<Person> personList = new ArrayList<>();

    private final List<PersonDto> personDtoList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        String firstName = "John";
        String lastName = "Smith";
        String email = "john.smith@gmail.com";
        String address = "18 rue jean moulin";
        String city = "Paris";
        String zipCode = "2564516";
        String phone = "2315643123";
        this.person = new Person(firstName, lastName, address, city, zipCode, phone, email);
        this.personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).email(email).address(address).phone(phone).city(city).zip(zipCode).build();
        this.personList.add(this.person);
        this.personDtoList.add(this.personDto);
    }

    @Test
    public void toPersonDtoShouldReturnAPersonDto() throws BadResourceException {
        PersonDto personDtoToCompare = this.mapper.toPersonDto(person);

        Assertions.assertEquals(personDto, personDtoToCompare);
    }

    @Test
    public void toPersonDtoShouldThrowBadResourceException() {
        String message = "No person provided";

        BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.mapper.toPersonDto(null), message);

        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void toPersonShouldReturnAPerson() throws BadResourceException {
        Person personToCompare = this.mapper.toPerson(this.personDto);

        Assertions.assertEquals(person, personToCompare);
    }

    @Test
    public void toPersonShouldThrowBadResourceException() {
        String message = "No person provided";

        BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.mapper.toPerson(null), message);

        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void toPersonDtoListShouldReturnAListOfPersonDto() throws BadResourceException {
        List<PersonDto> personDtoListToCompare = this.mapper.toPersonDtoList(this.personList);

        Assertions.assertEquals(personDtoList, personDtoListToCompare);
    }

    @Test
    public void toPersonDtoListShouldThrowBadResourceException() {
        String message = "No list of person provided";

        BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.mapper.toPersonDtoList(null), message);

        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
