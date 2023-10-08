package com.safetynetalerts.controller;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
public class PersonController {

	@Autowired
	private IPersonService personService;

	@GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getAllEmailAddresses(@RequestParam("city") String pCity) throws Exception {
		List<String> emailAddresses = this.personService.getAllEmailAddressesByCity(pCity);
		return ResponseEntity.ok(emailAddresses);
	}

	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildAlertDto>> getChildByAddress(@RequestParam("address") String address) throws IOException {
		List<ChildAlertDto> childDto = this.personService.getChildByAddress(address);
		return ResponseEntity.ok(childDto);
	}

	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() throws IOException {
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
		this.personService.addPerson(pPerson);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * this allows the user to modify a person
	 */
	@PutMapping("/person")
	public ResponseEntity updatePerson(@RequestParam("address") String pAddress, @RequestParam("firstName") String pFirstName, @RequestParam("lastName") String pLastName) throws Exception {
		this.personService.updatePerson(pAddress, pFirstName, pLastName);
		if (Objects.isNull(this.personService.getPersonByFullName(pFirstName, pLastName))) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/person")
	public ResponseEntity deletePerson(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws Exception {
		this.personService.deletePerson(firstName, lastName);
		if (Objects.isNull(this.personService.getPersonByFullName(firstName, lastName))) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}


}
