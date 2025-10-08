package com.master.project.model;

public class Person {

	private int id;
	private String firstName;
	private String lastName;
	private int age;
	private String email;
	private String password;

	public Person() {
	}

	public Person(int id, String firstName, String lastName, int age, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.email = email;
		this.password = password;
	}

	public Person(String firstName, String lastName, int age, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.email = email;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if (firstName == null || firstName.isEmpty()) {
			throw new IllegalArgumentException("Invalid firstname provided!");
		}
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if (lastName == null || lastName.isEmpty()) {
			throw new IllegalArgumentException("Invalid lastname provided!");
		}
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		if (age <= 0) {
			throw new IllegalArgumentException("Invalid age provided!");
		}
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Empty email address provided!");
		}
		
		if (!email.contains("@")) {
			throw new IllegalArgumentException("Invalid email address provided!");
		}
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Empty password is not allowed!");
		}
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format("[Id = %s, FirstName = %s, LastName = %s, Age = %s, Email = %s]", id, firstName, lastName,
				age, email);
	}

}
