package com.safetynetalerts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonIgnoreProperties
public class Person {

	public final String firstName;
	public final String lastName;
	public final String phone;
	public final String zip;
	public final String address;
	public final String city;
	public final String email;

	public static class PersonBuilder {
		private String firstName;
		private String lastName;
		private String phone;
		private String zip;
		private String address;
		private String city;
		private String email;

		public PersonBuilder() {
		}

		public PersonBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public PersonBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public PersonBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public PersonBuilder zip(String zip) {
			this.zip = zip;
			return this;
		}

		public PersonBuilder address(String address) {
			this.address = address;
			return this;
		}

		public PersonBuilder city(String city) {
			this.city = city;
			return this;
		}

		public PersonBuilder email(String email) {
			this.email = email;
			return this;
		}

		public Person build() {
			return new Person(firstName, lastName, phone, zip, address, city, email);
		}
	}

	private Person(String firstName, String lastName, String phone, String zip, String address, String city,
			String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.zip = zip;
		this.address = address;
		this.city = city;
		this.email = email;
	}

	@Override
	public boolean equals(Object obj) {
		return this.firstName == ((Person) obj).firstName && this.lastName == ((Person) obj).lastName;
	}
}
