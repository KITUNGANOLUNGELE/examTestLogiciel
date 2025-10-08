package com.master.project.service;

import java.sql.SQLException;
import java.util.List;

import com.master.project.model.Person;
import com.master.project.model.PersonDAO;

public class PersonService {
	
	private PersonDAO dao;
	
	public PersonService(PersonDAO dao) {
		this.dao = dao;
	}
	
	public void create(Person person) throws SQLException {
		dao.save(person);
	}

    public Person authenticate(String email, String password) throws SQLException {
        Person person = dao.findPersonByEmail(email);
        if (person == null) {
            throw new IllegalArgumentException("Email not found");
        }
        if (!person.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return person;
    }
	
	public void edit(Person person) throws SQLException {
		dao.update(person);
	}
	
	public void remove(Person person) throws SQLException {
		dao.delete(person);
	}

	public Person listPersonById(int id) throws SQLException {
		return dao.findPersonById(id);
	}
	
	public Person listPersonByName(String firstname) throws SQLException {
		return dao.findPersonByName(firstname);
	}
	
	public List<Person> listPersons() throws SQLException {
		return dao.findPersons();
	}
}
