package com.master.project.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnnotationsTest {

	@BeforeAll
	static void beforeClass() {
		System.out.println("BeforeClass tests cases.");
	}

	@AfterAll
	static void afterClass() {
		System.out.println("AfterClass tests cases.");
	}

	@BeforeEach
	void beforeEach() {
		System.out.println("BeforeEach test.");
	}

	@AfterEach
	void afterEach() {
		System.out.println("AfterEach test.");
	}

	@Test
	@Order(2)
	void test1() {
		System.out.println("Test1.");
	}
	
	@Test
	@Order(1)
	void test2() {
		System.out.println("Test2.");
	}

}
