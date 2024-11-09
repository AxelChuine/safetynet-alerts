package com.safetynetalerts.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChildAlertDto {
	private String firstName;

	private String lastName;

	private Integer age;

	private List<PersonDto> family;
}
