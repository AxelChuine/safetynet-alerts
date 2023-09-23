package com.safetynetalerts.dto;

import com.safetynetalerts.models.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationNumberDto {

	private List<Person> persons = new ArrayList<>();

	private Integer underaged;

	private Integer adult;
}
