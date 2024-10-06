package com.safetynetalerts.repository;

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
        return this.data.getPersons();
    }

    
    public Person savePerson(Person person, Person newPerson) {
        return this.data.savePerson(person, newPerson);
    }

}
