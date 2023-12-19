package com.safetynetalerts.controller;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
public class PersonController {

	@Autowired
	private IPersonService personService;

	private Logger logger = LoggerFactory.getLogger(PersonController.class);

	@GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getAllEmailAddresses(@RequestParam("city") String pCity) throws Exception {
		logger.info("launch of retrieval of every email addresses by city");
		List<String> emailAddresses = this.personService.getAllEmailAddressesByCity(pCity);
		return ResponseEntity.ok(emailAddresses);
	}

	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildAlertDto>> getChildByAddress(@RequestParam("address") String address) throws IOException {
		logger.info("launch of retrieval of every child by address");
		List<ChildAlertDto> childDto = this.personService.getChildByAddress(address);
		return ResponseEntity.ok(childDto);
	}

	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() throws IOException {
		logger.info("launch retrieval of every persons");
		List<Person> persons = this.personService.getAllPersons();
		return ResponseEntity.ok(persons);
	}

	/**
	 * this allows the user to create a new person
	 *
	 * @param pPerson
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/person")
	public ResponseEntity createPerson(@RequestBody PersonDto pPerson) throws IOException {
		logger.info("launch of creation of a person");
		Optional<Person> optionalPerson = this.personService.getAllPersons().stream().filter(person -> Objects.equals(person.firstName, pPerson.getFirstName()) && Objects.equals(person.lastName, pPerson.getLastName())).findFirst();
		if (optionalPerson.isPresent()) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		this.personService.addPerson(pPerson);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * this allows the user to modify a person
	 */
	@PutMapping("/person")
	public ResponseEntity updatePerson(@RequestParam("address") String pAddress, @RequestParam("firstName") String pFirstName, @RequestParam("lastName") String pLastName) throws Exception {
		logger.info("update person");
		this.personService.updatePerson(pAddress, pFirstName, pLastName);
		if (Objects.isNull(this.personService.getPersonByFullName(pFirstName, pLastName))) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/person")
	public ResponseEntity deletePerson(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws Exception {
		logger.info("delete person");
		this.personService.deletePerson(firstName, lastName);
		if (Objects.isNull(this.personService.getPersonByFullName(firstName, lastName))) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}


}
