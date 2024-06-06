package com.safetynetalerts.service.impl;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IPersonRepository;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.utils.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements IPersonService {

    @Autowired
    private Data data;

    @Autowired
    private IMedicalRecordService medicalRecordService;

    private final IPersonRepository repository;

    public PersonServiceImpl (IPersonRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Person> getAllPersonsByCity(String pCity) throws Exception {
        List<Person> persons = this.repository.getAllPersons();
        List<Person> personsToReturn = new ArrayList<>();
        for (Person p : persons) {
            if (p.city.equals(pCity)) {
                personsToReturn.add(p);
            }
        }
        return personsToReturn;
    }

    @Override
    public List<String> getAllEmailAddressesByCity(String pCity) throws Exception {
        List<Person> persons = this.getAllPersonsByCity(pCity);
        List<String> emailAddresses = new ArrayList<>();
        for (Person p : persons) {
            if (Objects.equals(p.city, pCity)) {
                Optional<String> optionalEmail = emailAddresses.stream().filter(e -> Objects.equals(e, p.email)).findFirst();
                if (!optionalEmail.isPresent()) {
                    emailAddresses.add(p.email);
                }
            }
        }
        return emailAddresses;
    }

    public PersonDto getPersonByFullName(String pFirstName, String pLastName) throws Exception {
        Person person;
        List<Person> persons = this.repository.getAllPersons();
        PersonDto personDto = new PersonDto();
        Optional<Person> personOptional = persons.stream().filter(p -> Objects.equals(p.getFirstName(), pFirstName) && Objects.equals(p.getLastName(), pLastName)).findFirst();
        if (personOptional.isPresent()) {
            person = personOptional.get();
            personDto.setFirstName(person.firstName);
            personDto.setLastName(person.lastName);
            personDto.setAddress(person.address);
            personDto.setCity(person.city);
            personDto.setZip(person.zip);
            personDto.setPhone(person.phone);
            personDto.setEmail(person.email);
        } else {
            return null;
        }
        return personDto;
    }

    public List<Person> getPersonsByAddress(String pAddress) {
        List<Person> personsByAddress = this.repository.getAllPersons();
        personsByAddress = personsByAddress.stream().filter(p -> Objects.equals(p.address, pAddress))
                .collect(Collectors.toList());
        return personsByAddress;
    }

    /**
     * @param pAddress
     * @return
     * @throws IOException
     */
    @Override
    public List<ChildAlertDto> getChildByAddress(String pAddress) throws IOException {
        List<ChildAlertDto> childrenAlertDto = new ArrayList<>();
        List<Person> peopleByAddress = this.getPersonsByAddress(pAddress);
        for (Person p : peopleByAddress) {
            if (this.medicalRecordService.isUnderaged(p.firstName, p.lastName)) {
                ChildAlertDto childAlertDto = new ChildAlertDto(p.firstName, p.lastName, this.medicalRecordService.getAgeOfPerson(p.firstName, p.lastName), this.getFamilyMembers(peopleByAddress, p.lastName));
                childrenAlertDto.add(childAlertDto);
            }
        }
        return childrenAlertDto;
    }

    public List<Person> getFamilyMembers(List<Person> pFamilyMember, String pLastName) {
        List<Person> familyMember = new ArrayList<>();
        familyMember = pFamilyMember.stream().filter(p -> Objects.equals(p.lastName, pLastName)).collect(Collectors.toList());
        return familyMember;
    }

    @Override
    public List<PersonDto> getAllPersons() {
        List<Person> persons = this.repository.getAllPersons();
        List<PersonDto> personsToReturn = new ArrayList<>();
        for (Person p : persons) {
            PersonDto personDto = new PersonDto(p.firstName, p.lastName, p.address, p.city, p.zip, p.phone, p.email);
            personsToReturn.add(personDto);
        }
        return personsToReturn;
    }

    @Override
    public PersonDto addPerson(PersonDto pPerson) {
        if (this.repository.getAllPersons().stream().anyMatch(p -> Objects.equals(p.firstName, pPerson.getFirstName()) && Objects.equals(p.lastName, pPerson.getLastName()))) {
            return null;
        }
        Person person = new Person.PersonBuilder().firstName(pPerson.getFirstName()).lastName(pPerson.getLastName()).address(pPerson.getAddress()).city(pPerson.getCity()).zip(pPerson.getZip()).phone(pPerson.getPhone()).email(pPerson.getEmail()).build();
        this.repository.getAllPersons().add(person);
        return pPerson;
    }

    @Override
    public PersonDto updatePerson(String pAddress, String pFirstName, String pLastName) throws Exception {
        PersonDto personDto = this.getPersonByFullName(pFirstName, pLastName);
        if (Objects.isNull(personDto.getFirstName()) || Objects.isNull(personDto.getLastName())) {
            String resource = "person" + " " + pFirstName + " " + pLastName;
            throw new ResourceNotFoundException(resource);
        }
        Person modifiedPerson = new Person.PersonBuilder().firstName(personDto.getFirstName()).lastName(personDto.getLastName()).address(pAddress).city(personDto.getCity()).
                zip(personDto.getZip()).phone(personDto.getPhone()).email(personDto.getEmail()).build();
        Integer index = 0;
        for (Person p : repository.getAllPersons()) {
            if (Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName)) {
                index = repository.getAllPersons().indexOf(p);
            }
        }
        this.repository.getAllPersons().remove(repository.getAllPersons().get(index));
        repository.getAllPersons().add(modifiedPerson);

        return personDto;
    }


    @Override
    public void deletePerson(String firstName, String lastName) {
        List<Person> persons = repository.getAllPersons();
        Person person = null;
        for (Person p : persons) {
            person = p;
        }
        persons.remove(person);
        data.setPersons(persons);
    }

    @Override
    public SimplePersonDto convertToSimplePersonDto(Person pPerson) {
        return new SimplePersonDto(pPerson.firstName, pPerson.lastName, pPerson.address, pPerson.phone);
    }

    @Override
    public List<SimplePersonDto> convertToDtoList(List<Person> pPersons) {
        List<SimplePersonDto> simplePersonDtos = new ArrayList<>();
        for (Person p : pPersons) {
            simplePersonDtos.add(this.convertToSimplePersonDto(p));
        }
        return simplePersonDtos;
    }

}
