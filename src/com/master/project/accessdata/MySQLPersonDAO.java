package com.master.project.accessdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.master.project.model.Person;
import com.master.project.model.PersonDAO;

public class MySQLPersonDAO implements PersonDAO {
	private final String URL = "jdbc:mysql://localhost:3306/gestion_person"; // "jdbc:mysql://192.168.14.5:3306/gestion_person"
	private final String USER = "root";
	private final String PASSWORD = "";
	final String DRIVER = "com.mysql.cj.jdbc.Driver";

	public MySQLPersonDAO() throws ClassNotFoundException {
		try {
			Class.forName(DRIVER);
			System.out.println("Driver found.");
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException("Driver not found!");
		}
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	@Override
	public void save(Person person) throws SQLException {
		String query = "INSERT INTO person(firstname, lastname, age, email, password) VALUES(?, ?, ?, ?, ?)";

		try (Connection con = getConnection()) {
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, person.getFirstName());
			ps.setString(2, person.getLastName());
			ps.setInt(3, person.getAge());
			ps.setString(4, person.getEmail());
			ps.setString(5, person.getPassword());

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					person.setId(rs.getInt(1));
					System.out.println("Person saved successfully.");
				}
			}

		} catch (SQLException e) {
//			System.err.println(String.format("com.master.project.accessdata.MySQLPersonDAO => save : %s", e.getMessage()));
			throw new SQLException("Failed to save a new person, " + e.getMessage());
		}
	}

	@Override
	public void update(Person person) throws SQLException {
		String query = "UPDATE person SET firstname = ?, lastname = ?, age = ?, email = ?, password = ? WHERE id = ?";

		try (Connection con = getConnection()) {
			PreparedStatement ps = con.prepareStatement(query);

			ps.setString(1, person.getFirstName());
			ps.setString(2, person.getLastName());
			ps.setInt(3, person.getAge());
			ps.setString(4, person.getEmail());
			ps.setString(5, person.getPassword());
			ps.setInt(6, person.getId());

			int record = ps.executeUpdate();

			if (record > 0) {
				System.out.println("Person updated successfully.");
			}
		} catch (SQLException e) {
			throw new SQLException("Failed to update person, " + e.getMessage());
		}

	}

	@Override
	public void delete(int id) throws SQLException {
		String query = "DELETE FROM person WHERE id = ?";

		try (Connection con = getConnection()) {
			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, id);
			int record = ps.executeUpdate();

			if (record > 0) {
				System.out.println("Person deleted successfully.");
			} else {
				throw new IllegalArgumentException(
						String.format("%s is not deleted from the database."));
			}
		} catch (SQLException e) {
			throw new SQLException("Failed to delete person, " + e.getMessage());
		}

	}

	@Override
	public Person findPersonById(int id) throws SQLException {
		String query = "SELECT id, firstname, lastname, age, email, password FROM person WHERE id = ?";

		try (Connection con = getConnection()) {
			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Person(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"),
							rs.getInt("age"), rs.getString("email"), rs.getString("password"));
				}
			}

		} catch (SQLException e) {
			throw new SQLException(String.format("Failed to fetch person from its id '%d', %s", id, e.getMessage()));
		}

		return null;
	}

	@Override
	public Person findPersonByName(String firstName) throws SQLException {
		String query = "SELECT id, firstname, lastname, age, email, password FROM person WHERE firstname = ?";

		try (Connection con = getConnection()) {
			PreparedStatement ps = con.prepareStatement(query);

			ps.setString(1, firstName);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Person(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"),
							rs.getInt("age"), rs.getString("email"), rs.getString("password"));
				}
			}

		} catch (SQLException e) {
			throw new SQLException(
					String.format("Failed to fetch person from its firstname '%s', %s", firstName, e.getMessage()));
		}
		return null;
	}

	@Override
	public List<Person> findPersons() throws SQLException {
		String query = "SELECT id, firstname, lastname, age, email, password FROM person";
		List<Person> persons = new ArrayList<>();
		
		try(Connection con = getConnection()) {
			PreparedStatement ps = con.prepareStatement(query);
			
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next()) {
					Person person = new Person(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"),
							rs.getInt("age"), rs.getString("email"), rs.getString("password"));
					
					persons.add(person);
				}
			} 
		} catch (SQLException e) {
			throw new SQLException("Failed to fetch persons' data, " + e.getMessage());
		}
		
		return persons;
	}

    @Override
    public Person findPersonByEmail(String email) throws SQLException {
        String query = "SELECT id, firstname, lastname, age, email, password FROM person WHERE email = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Person(
                            rs.getInt("id"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getInt("age"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

}
