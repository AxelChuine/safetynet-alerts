package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class PersonController {

	private final IPersonService personService;

	private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    public PersonController(IPersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getAllEmailAddresses(@RequestParam("city") String pCity) throws Exception {
		logger.info("launch of retrieval of every email addresses by city");
		return new ResponseEntity<>(this.personService.getAllEmailAddressesByCity(pCity), HttpStatus.OK);
	}

	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildAlertDto>> getChildByAddress(@RequestParam("address") String address) throws IOException, ResourceNotFoundException {
		logger.info("launch of retrieval of every child by address");
		return new ResponseEntity<>(this.personService.getChildByAddress(address), HttpStatus.OK);
	}

	@GetMapping("/persons")
	public ResponseEntity<List<PersonDto>> getAllPersons() throws IOException {
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
	public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto pPerson) throws ResourceAlreadyExistsException {
		logger.info("launch of creation of a person");
		return new ResponseEntity<>(this.personService.addPerson(pPerson), HttpStatus.CREATED);
	}


	@PutMapping("/person")
	public ResponseEntity<PersonDto> updateAddressOfPerson(@RequestParam("address") String pAddress, @RequestParam("firstName") String pFirstName, @RequestParam("lastName") String pLastName) throws Exception {
		logger.info("update address of person");
		return new ResponseEntity<>(this.personService.updateAddressOfPerson(pAddress, pFirstName, pLastName), HttpStatus.OK);
	}

	@PutMapping("/city")
	public ResponseEntity<PersonDto> updateCityOfPerson(@RequestParam("city") String city, @RequestParam("first-name") String firstName, @RequestParam("last-name") String lastName) throws Exception {
		logger.info("update city of person");
		return new ResponseEntity<>(this.personService.updateCityOfPerson(city, firstName, lastName), HttpStatus.OK);
	}

	@DeleteMapping("/person")
	public ResponseEntity deletePerson(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws ResourceNotFoundException {
		logger.info("delete person");
		this.personService.deletePerson(firstName, lastName);
		return new ResponseEntity(HttpStatus.OK);
	}
  
  	@GetMapping("/person")
    public ResponseEntity<List<PersonDto>> getPersonByFullName(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws Exception {
		logger.info("get a person by his full name");
		return new ResponseEntity<>(List.of(this.personService.getPersonByFullName(firstName, lastName)), HttpStatus.OK);
    }
}
