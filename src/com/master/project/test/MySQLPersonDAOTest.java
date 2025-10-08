package com.master.project.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.master.project.accessdata.MySQLPersonDAO;
import com.master.project.model.Person;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MySQLPersonDAOTest {
	
	private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
	private final String URL = "jdbc:mysql://localhost:3306/gestion_person";
	private final String USER = "root";
	private final String PASSWORD = "";
	
//	private static PersonDAO dao = null;
	private static MySQLPersonDAO dao = null;
	
	private static Person testPerson = null;

	@BeforeAll
	static void checkDatabaseDriver() {
		try {
			assertNotNull(DRIVER);
			Class.forName(DRIVER);
			
			dao = new MySQLPersonDAO();
			assertNotNull(dao);
		} catch (ClassNotFoundException e) {
			fail(e.getMessage());
		}
	}

	@BeforeEach
	void checkDatabaseConnection() {
		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
			assertNotNull(PASSWORD);
			assertFalse(con.isClosed());
		} catch (SQLException e) {
			fail("Enable to access the database, " + e.getMessage());
		}
	}

	@Test
	@Order(1)
	@DisplayName("Save a new person to the database.")
	void saveTest() {
		try {
			// Bad Person
			Person person = new Person();
			
			assertThrows(IllegalArgumentException.class, () -> person.setFirstName(null));
			assertThrows(IllegalArgumentException.class, () -> person.setLastName(null));
			assertThrows(IllegalArgumentException.class, () -> person.setAge(-1));
			assertThrows(IllegalArgumentException.class, () -> person.setAge(0));
			assertThrows(IllegalArgumentException.class, () -> person.setEmail(null));
			assertThrows(IllegalArgumentException.class, () -> person.setEmail("bademail"));
			assertThrows(IllegalArgumentException.class, () -> person.setPassword(null));
			
			// Good Person
			person.setFirstName("Charles");
			person.setLastName("Quintin");
			person.setAge(18);
			person.setEmail("quintin@charles.com");
			person.setPassword("PWD");
			
			dao.save(person);
			
			testPerson = person;
			
			assertTrue(testPerson.getId() > 0);
			
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	@DisplayName("Update person with new data to the database.")
	void updateTest() {
		try {
			testPerson.setFirstName("Amida");
			testPerson.setLastName("Bin Saleh");
			testPerson.setAge(45);
			testPerson.setEmail("amida.bin.saleh@hotmail.com");
			testPerson.setPassword("ABS23");
			
			dao.update(testPerson);
			
			// Check the inserted person
			Person personUpdated = dao.findPersonById(testPerson.getId());
			
			assertEquals("Amida", personUpdated.getFirstName());
			assertEquals("Bin Saleh", personUpdated.getLastName());
			assertEquals(45, personUpdated.getAge());
			assertEquals("amida.bin.saleh@hotmail.com", personUpdated.getEmail());
			assertEquals("ABS23", personUpdated.getPassword());
			
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	@DisplayName("Fetch person from the database throught its ID.")
	void findPersonByIdTest() {
		try {
			Person person = dao.findPersonById(testPerson.getId());
			assertEquals(testPerson.getFirstName(), person.getFirstName());
			
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	@Order(4)
	@DisplayName("Fetch person from the database throught its Firstname.")
	void findPersonByName() {
		try {
			Person person = dao.findPersonByName(testPerson.getFirstName());
			assertEquals(testPerson.getFirstName(), person.getFirstName());
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	@Order(5)
	@DisplayName("Fetch all person from the database.")
	void findPersonsTest() {
		try {
			List<Person> persons = dao.findPersons();
			assertFalse(persons.isEmpty());
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	@Order(6)
	@DisplayName("Delete person from the database.")
	void deleteTest() {
		try {
			dao.delete(testPerson.getId());
			Person person = dao.findPersonById(testPerson.getId());
			assertNull(person);
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	@Order(7)
	@DisplayName("Delete fake person from the database.")
	void deleteFakePersonTest() {
		try {
			Person fakePerson = new Person(0, "fakeFirstName", "fakeLastName", 20, "fakeemail@fake.com", "fakepassword");
			assertThrowsExactly(IllegalArgumentException.class, () -> dao.delete(fakePerson.getId()));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
