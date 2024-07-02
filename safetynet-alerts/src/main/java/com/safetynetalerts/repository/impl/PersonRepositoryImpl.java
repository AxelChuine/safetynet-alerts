package com.safetynetalerts.repository.impl;

import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IPersonRepository;
import com.safetynetalerts.utils.Data;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepositoryImpl implements IPersonRepository {

    private final Data data;

    public PersonRepositoryImpl(Data data) {
        this.data = data;
    }

    @Override
    public List<Person> getAllPersons() {
        return this.data.getPersons();
    }

    @Override
    public Person updateAddressOfPerson(Person person, Person newPerson) {
        return this.data.savePersonWithNewAddress(person, newPerson);
    }
}
