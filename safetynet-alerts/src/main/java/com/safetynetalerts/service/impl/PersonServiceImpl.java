package com.safetynetalerts.service.impl;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.utils.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.event.ListDataEvent;
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


    @Override
    public List<Person> getAllPersonsByCity(String pCity) throws Exception {
        List<Person> persons = data.getPersons();
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
        List<Person> persons = data.getPersons();
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
        List<Person> personsByAddress = data.getPersons();
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
    public List<Person> getAllPersons() {
        List<Person> persons = this.data.getPersons();
        return persons;
    }

    @Override
    public PersonDto addPerson(PersonDto pPerson) {
        PersonDto personDto = pPerson;
        if (this.data.getPersons().stream().anyMatch(p -> Objects.equals(p.firstName, pPerson.getFirstName()) && Objects.equals(p.lastName, pPerson.getLastName()))) {
            return null;
        }
        Person person = new Person.PersonBuilder().firstName(pPerson.getFirstName()).lastName(pPerson.getLastName()).address(pPerson.getAddress()).city(pPerson.getCity()).zip(pPerson.getZip()).phone(pPerson.getPhone()).email(pPerson.getEmail()).build();
        this.data.getPersons().add(person);
        return personDto;
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
        for (Person p : data.getPersons()) {
            if (Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName)) {
                index = data.getPersons().indexOf(p);
            }
        }
        this.data.getPersons().remove(data.getPersons().get(index));
        data.getPersons().add(modifiedPerson);

        return personDto;
    }


    @Override
    public void deletePerson(String firstName, String lastName) {
        List<Person> persons = data.getPersons();
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
