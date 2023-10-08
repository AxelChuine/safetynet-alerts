package com.safetynetalerts.dto;

import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PersonDtoTest {
    @MockBean
    private Utils utils;

    PersonDto personDto = new PersonDto("Jean", "Dubois", "89 rue Jean Racine", "Lilles", "62400", "04", "test@email.fr");


    @Test
    public void setFirstNameTest () {
        PersonDto personDto1 = new PersonDto();
        personDto1.setFirstName(this.personDto.getFirstName());
        personDto1.setLastName(this.personDto.getLastName());
        personDto1.setAddress(this.personDto.getAddress());
        personDto1.setCity(this.personDto.getCity());
        personDto1.setZip(this.personDto.getZip());
        personDto1.setPhone(this.personDto.getPhone());
        personDto1.setEmail(this.personDto.getEmail());

        assertEquals(this.personDto.getFirstName(), personDto1.getFirstName());
        assertEquals(this.personDto.getLastName(), personDto1.getLastName());
        assertEquals(this.personDto.getAddress(), personDto1.getAddress());
        assertEquals(this.personDto.getCity(), personDto1.getCity());
        assertEquals(this.personDto.getZip(), personDto1.getZip());
        assertEquals(this.personDto.getPhone(), personDto1.getPhone());
        assertEquals(this.personDto.getEmail(), personDto1.getEmail());
    }
}
