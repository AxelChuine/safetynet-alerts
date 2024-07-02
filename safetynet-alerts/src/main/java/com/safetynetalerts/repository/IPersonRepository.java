package com.safetynetalerts.repository;

import com.safetynetalerts.models.Person;

import java.util.List;


public interface IPersonRepository {

    List<Person> getAllPersons();

    Person updateAddressOfPerson(Person person, Person newPerson);
}
