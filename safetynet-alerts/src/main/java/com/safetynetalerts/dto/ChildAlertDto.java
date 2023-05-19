package com.safetynetalerts.dto;

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
	 * @return a list of ChildWithFamilyDto
	 */
	private String firstName;

	private String lastName;

	private Integer age;

	private List<Person> family;

}
