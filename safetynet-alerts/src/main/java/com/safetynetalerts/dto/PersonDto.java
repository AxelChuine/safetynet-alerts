package com.safetynetalerts.dto;

import java.util.ArrayList;
import java.util.List;

import com.safetynetalerts.models.Person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

	private List<Person> persons = new ArrayList<>();

	private Integer underaged;

	private Integer adult;
}
