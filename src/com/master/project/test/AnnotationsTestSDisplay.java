package com.master.project.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnnotationsTestSDisplay {

	@BeforeAll
	@DisplayName("Execute before class.")
	static void classBefore() {
		System.out.println("classBefore.");
	}

	@AfterAll
	@DisplayName("Execute after class.")
	static void classAfter() {
		System.out.println("classAfter.");
	}

	@BeforeEach
	@DisplayName("Execute before each test.")
	void before() {
		System.out.println("before.");
	}

	@AfterEach
	@DisplayName("Execute after each test.")
	void after() {
		System.out.println("after.");
	}

	@Test
	@Order(2)
	@DisplayName("Test sum of two numbers.")
	void sum(TestInfo info) {
		int x = 4;
		int y = 2;
		System.out.println("4 + 2 = " + (x + y)); // 6
		System.out.println(info.getDisplayName());
		System.out.println(String.format("%d plus %d = %d", x, y, x + y));
	}
	
	@Test
	@Order(1)
	@DisplayName("Test mult of two numbers.")
	void mult() {
		System.out.println("2 * 5 = " + 2 * 5); // 10
	}

}
