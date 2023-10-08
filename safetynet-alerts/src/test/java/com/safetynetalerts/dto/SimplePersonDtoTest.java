package com.safetynetalerts.dto;

import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SimplePersonDtoTest {

    @MockBean
    private Data data;

    @MockBean
    private Utils utils;

    SimplePersonDto simplePersonDto = new SimplePersonDto("Jean", "Dubois", "89 rue Jean Racine", "04");


    @Test
    public void setFirstNameTest () {
        SimplePersonDto simplePersonDto = new SimplePersonDto("Jean", "Dubois", "89 rue du puit", "04");
        SimplePersonDto person = new SimplePersonDto();
        person.setFirstName("Jean");

        assertEquals(simplePersonDto.getFirstName(), person.getFirstName());
    }

    @Test
    public void setLastNameTest () {
        SimplePersonDto person = new SimplePersonDto();
        person.setLastName("Dubois");

        assertEquals(this.simplePersonDto.getLastName(), person.getLastName());
    }

    @Test
    public void setAddressTest () {
        SimplePersonDto personDto = new SimplePersonDto();
        personDto.setAddress("89 rue Jean Racine");

        assertEquals(this.simplePersonDto.getAddress(), personDto.getAddress());
    }

    @Test
    public void setPhoneTest () {
        SimplePersonDto person = new SimplePersonDto();
        person.setPhone("04");

        assertEquals(this.simplePersonDto.getPhone(), person.getPhone());
    }

    @Test
    public void hashCodeSimplePersonDtoTest () {
        SimplePersonDto personDto = this.simplePersonDto;

        assertEquals(personDto.hashCode(), personDto.hashCode());
    }
}
