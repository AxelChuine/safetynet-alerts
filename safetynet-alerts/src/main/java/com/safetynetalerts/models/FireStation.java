package com.safetynetalerts.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties
public class FireStation {
	private Set<String> addresses;
	private String stationNumber;

	public FireStation(String stationNumber) {
		this.addresses = new HashSet<>();
		this.stationNumber = stationNumber;
	}

	public FireStation addAddress(String address) {
		addresses.add(address);
		return this;
	}

	public String getStationNumber() {
		return stationNumber;
	}

	public List<String> getAddresses() {
		return addresses.stream().collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return stationNumber.concat(": ") + String.join(", ", addresses);
	}

}
