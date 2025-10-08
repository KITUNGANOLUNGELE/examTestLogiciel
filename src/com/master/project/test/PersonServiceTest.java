package com.master.project.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.master.project.accessdata.MySQLPersonDAO;
import com.master.project.model.Person;
import com.master.project.model.PersonDAO;
import com.master.project.service.PersonService;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonServiceTest {
    static PersonService service;

    @BeforeAll
    static void setup() throws ClassNotFoundException {
        PersonDAO dao = new MySQLPersonDAO();
        service = new PersonService(dao);
    }

    @Test
    @Order(1)
    void testAuthenticateSuccess() throws SQLException {
        Person person = service.authenticate("alice@nestle.com", "P@ssword");
        assertNotNull(person);
        assertEquals("Alice", person.getFirstName());
    }

    @Test
    @Order(2)
    void testAuthenticateWrongPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.authenticate("alice@nestle.com", "wrongpass");
        });
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    @Order(3)
    void testAuthenticateUnknownEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.authenticate("unknown@mail.com", "pwd");
        });
        assertEquals("Email not found", exception.getMessage());
    }
}
