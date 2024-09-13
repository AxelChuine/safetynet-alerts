package com.safetynetalerts.controller;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.PersonInfo;
import com.safetynetalerts.service.IPersonMedicalRecordsService;
import com.safetynetalerts.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {

	private final IPersonService personService;

	private final IPersonMedicalRecordsService personMedicalRecordsService;

	private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    public PersonController(IPersonService personService, IPersonMedicalRecordsService personMedicalRecordsService) {
        this.personService = personService;
        this.personMedicalRecordsService = personMedicalRecordsService;
    }

	@GetMapping("/all")
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
	@PostMapping
	public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto pPerson) throws ResourceAlreadyExistsException {
		logger.info("launch of creation of a person");
		try {
			PersonDto person = this.personService.addPerson(pPerson);
			return new ResponseEntity<>(person, HttpStatus.CREATED);
		} catch (ResourceAlreadyExistsException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping
	public ResponseEntity<PersonDto> updatePerson(@RequestBody PersonDto personDto) throws BadResourceException, ResourceNotFoundException, ResourceAlreadyExistsException {
		logger.info("update person");
		return new ResponseEntity<>(this.personService.updatePerson(personDto), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity deletePerson(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws ResourceNotFoundException {
		logger.info("delete person");
		this.personService.deletePerson(firstName, lastName);
		return new ResponseEntity(HttpStatus.OK);
	}
  
  	@GetMapping
    public ResponseEntity<List<PersonDto>> getPersonByFullName(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws Exception {
		logger.info("get a person by his full name");
		List<PersonDto> personDtos = new ArrayList<>(List.of(this.personService.getPersonByFullName(firstName, lastName)));
		return new ResponseEntity<>(personDtos, HttpStatus.OK);
    }

	@GetMapping("/person-info")
	public ResponseEntity<List<PersonInfo>> getAllPersonInfos(@RequestParam("last-name") String lastName) throws Exception {
		logger.info("get all person infos");
		return new ResponseEntity<>(this.personMedicalRecordsService.getAllPersonInformations(lastName), HttpStatus.OK);
	}
}
