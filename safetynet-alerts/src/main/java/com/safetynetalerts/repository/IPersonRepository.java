package com.safetynetalerts.repository;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.models.Person;

import java.util.List;


public interface IPersonRepository {

    List<Person> getAllPersons();

    Person savePerson(Person person, Person newPerson) throws ResourceAlreadyExistsException;
}
