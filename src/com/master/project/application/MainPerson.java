package com.master.project.application;

import java.sql.SQLException;

import com.master.project.accessdata.MySQLPersonDAO;
import com.master.project.model.Person;
import com.master.project.model.PersonDAO;
import com.master.project.service.PersonService;

public class MainPerson {

	public static void main(String[] args) {
		try {
			PersonDAO dao = new MySQLPersonDAO();
			PersonService service = new PersonService(dao);

			// Insert person
			System.out.println("-------------------Insert person--------------");
			Person person1 = new Person("Alice", "Kobo", 22, "alice@nestle.com", "P@ssword");
			service.create(person1);

			Person person2 = new Person("Samuel", "Eto", 32, "samuel@baca.com", "P@ssword");
			service.create(person2);

			Person person3 = new Person("Sifa", "Malembe", 12, "sifa@sifa.com", "P@ssword");
			service.create(person3);

			Person person4 = new Person("Koffi", "Olomide", 2, "bbc@bbc.com", "pass");
			service.create(person4);

			// List info
			System.out.println("-------------------List info--------------");
			System.out.println(service.listPersonByName("Koffi").toString());

			Person person5 = new Person("Salima", "Kwabo", 20, "kwabo@salem.com", "PWD");
			service.create(person5);
			
			// Fake person
			Person person6 = new Person(0, "Fake firstName", "Fake lastName", 10, "fakemail@mail.com", "P@ssword");

			person4.setFirstName("Nasralah");
			person4.setLastName("Bin Kojo");
			person4.setAge(54);
			person4.setEmail("binnas@hotmail.com");
			person4.setPassword("P@ssword");

			// Update person
			System.out.println("-------------------Update person--------------");
			service.edit(person4);

			// List info
			System.out.println("-------------------List info--------------");
			System.out.println(service.listPersonById(person4.getId()).toString());
			
			// Delete person
			System.out.println("-------------------Delete person--------------");
			service.remove(person5.getId());

			System.out.println("========================LIST ALL PERSONS===========================");
			service.listPersons().forEach(person -> {
				System.out.println(person.toString());
			});
						
			// Fake person deletion
			service.remove(person6.getId());

		} catch (IllegalArgumentException e1) {
//			e1.printStackTrace();
			System.err.println(e1.getMessage());
		} catch (SQLException e2) {
//			e2.printStackTrace();
			System.err.println(e2.getMessage());
		} catch (ClassNotFoundException e2) {
//			e2.printStackTrace();
			System.err.println(e2.getMessage());
		} catch (Exception e3) {
//			e3.printStackTrace();
			System.err.println("Unknown error, " + e3.getMessage());
		}

	}

}
