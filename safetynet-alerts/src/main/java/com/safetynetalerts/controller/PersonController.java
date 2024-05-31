package com.safetynetalerts.controller;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
		return new ResponseEntity<>(this.personService.getChildByAddress(address), HttpStatus.OK);
	}

	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() throws IOException {
		logger.info("launch retrieval of every persons");
		return new ResponseEntity<>(this.personService.getAllPersons(), HttpStatus.OK);
	}

	/**
	 * this allows the user to create a new person
	 *
	 * @param pPerson
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/person")
	public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto pPerson) throws IOException {
		logger.info("launch of creation of a person");
		return new ResponseEntity<>(this.personService.addPerson(pPerson), HttpStatus.CREATED);
	}

	/**
	 * this allows the user to modify a person
	 */

	@PutMapping("/person")
	public ResponseEntity<PersonDto> updatePerson(@RequestParam("address") String pAddress, @RequestParam("firstName") String pFirstName, @RequestParam("lastName") String pLastName) throws Exception {
		logger.info("update person");
		return new ResponseEntity<>(this.personService.updatePerson(pAddress, pFirstName, pLastName), HttpStatus.OK);
	}

	@DeleteMapping("/person")
	public ResponseEntity deletePerson(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws Exception {
		logger.info("delete person");
		if (Objects.isNull(this.personService.getPersonByFullName(firstName, lastName))) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		this.personService.deletePerson(firstName, lastName);
		return new ResponseEntity(HttpStatus.OK);
	}


}
