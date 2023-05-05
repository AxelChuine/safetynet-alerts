package com.safetynetalerts.dto;

import java.util.ArrayList;
import java.util.List;

import com.safetynetalerts.models.Person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChildAlertDto {
	/**
	 * @return a list of child or children living at an address
	 * @return first name and last name of each child, his/her age
	 * @return a list of the other member of the family
	 */

	List<Person> children = new ArrayList<>();

	String firstName;

	String lastName;

	Integer age;

	List<Person> family = new ArrayList<>();

}
