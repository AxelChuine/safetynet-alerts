package com.safetynetalerts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class Person {

	public final String firstName;
	public final String lastName;
	public final String address;
	public final String city;
	public final String zip;
	public final String phone;
	public final String email;

	public static class PersonBuilder {
		private String firstName;
		private String lastName;
		private String address;
		private String city;
		private String zip;
		private String phone;
		private String email;

		public PersonBuilder () {

		}

		public PersonBuilder firstName (String pFirstName) {
			this.firstName = pFirstName;
			return this;
		}

		public PersonBuilder lastName (String pLastName) {
			this.lastName = pLastName;
			return this;
		}

		public PersonBuilder address (String pAddress) {
			this.address = pAddress;
			return this;
		}

		public PersonBuilder city (String pCity) {
			this.city = pCity;
			return this;
		}

		public PersonBuilder zip (String pZip) {
			this.zip = pZip;
			return this;
		}

		public PersonBuilder phone (String pPhone) {
			this.phone = pPhone;
			return this;
		}

		public PersonBuilder email (String pEmail) {
			this.email = pEmail;
			return this;
		}

		public Person build () {
			return new Person(firstName, lastName, address, city, zip, phone, email);
		}
	}

	private Person(String firstName, String lastName, String address, String city, String zip,
			 String phone, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.zip = zip;
		this.phone = phone;
		this.email = email;
	}

	public boolean equals (Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Person person = (Person) obj;
		return this.firstName.equals(person.firstName) && this.lastName.equals(person.lastName);
	}

}
