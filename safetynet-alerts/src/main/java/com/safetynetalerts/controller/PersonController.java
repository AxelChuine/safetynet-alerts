package com.safetynetalerts.controller;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class PersonController {

	@Autowired
	private IPersonService personService;

	@GetMapping("/communityEmail")
	public List<String> getAllEmailAddresses(@RequestParam("city") String pCity) throws Exception {
		List<String> emailAddresses = this.personService.getAllEmailAddressesByCity(pCity);
		return emailAddresses;
	}

	@GetMapping("/childAlert")
	public List<ChildAlertDto> getChildByAddress(@RequestParam("address") String address) throws IOException {
		List<ChildAlertDto> childDto = this.personService.getChildByAddress(address);
		return childDto;
	}

	@GetMapping("/person")
	public List<Person> getAllPersons() throws IOException {
		return this.personService.getAllPersons();
	}

	@PostMapping("/person")
	public void createPerson(@RequestBody PersonDto pPerson) throws IOException {
		this.personService.addPerson(pPerson);
	}


}
