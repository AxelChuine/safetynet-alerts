package com.safetynetalerts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalerts.service.IPersonService;

@RestController
public class PersonController {

	@Autowired
	private IPersonService personService;

	@GetMapping("/communityEmail")
	public List<String> getAllEmailAddresses(@RequestParam("city") String pCity) throws Exception {
		List<String> emailAddresses = this.personService.getAllEmailAddressesByCity(pCity);
		return emailAddresses;
	}

}
