package com.safetynetalerts.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// liste de numéros de téléphones des foyers désservis par la caserne
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhoneAlertDto {

	List<String> cellNumbers = new ArrayList<>();
}
