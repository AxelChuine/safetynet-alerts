package com.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

	private List<PersonDto> family;

}
