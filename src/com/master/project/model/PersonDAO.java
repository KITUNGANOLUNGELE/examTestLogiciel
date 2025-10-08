package com.master.project.model;

import java.sql.SQLException;
import java.util.List;

public interface PersonDAO {
	/**
	 * Save person's data to database.
	 * @param person
	 * @throws SQLException
	 */
	void save(Person person) throws SQLException;	
	
	/**
	 * Update person's data to the database.
	 * @param person
	 * @throws SQLException
	 */
	void update(Person person) throws SQLException;
	
	/**
	 * Delete person's data from the database.
	 * @param person
	 * @throws SQLException
	 */
	void delete(Person person) throws SQLException;
	
	/**
	 * Fetch person's information from the database using her/his Id.
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	Person findPersonById(int id) throws SQLException;
	
	/**
	 * Fetch person's information from the database using her/his FistName.
	 * @param firstName
	 * @return
	 * @throws SQLException
	 */
	Person findPersonByName(String firstName) throws SQLException;
	
	/**
	 * Fetch all persons' information from the database.
	 * @return
	 * @throws SQLException
	 */
	List<Person> findPersons() throws SQLException;

    /**
     * connect a user
     * */
    Person findPersonByEmail(String email) throws SQLException;
	
}
