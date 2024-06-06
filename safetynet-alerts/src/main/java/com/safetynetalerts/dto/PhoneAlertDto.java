package com.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// liste de numéros de téléphones des foyers désservis par la caserne
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhoneAlertDto {

	List<String> cellNumbers = new ArrayList<>();
}
