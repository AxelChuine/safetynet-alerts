package com.safetynetalerts.repository;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.utils.Data;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepositoryImpl {

    private final Data data;

    public PersonRepositoryImpl(Data data) {
        this.data = data;
    }

    
    public List<Person> getAllPersons() {
        return this.data.getAllPersons();
    }

    
    public Person savePerson(Person person, Person newPerson) throws ResourceAlreadyExistsException {
        return this.data.savePerson(person, newPerson);
    }

    public Person savePerson(Person person) throws BadResourceException {
        return this.data.savePerson(person);
    }

    public List<Person> save(List<Person> persons) {
        return this.data.saveListOfPerPersons(persons);
    }
}
