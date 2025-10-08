package com.master.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AnnotationsSecondTest {

	@Test
	void testAnnotation() {
		String obj1 = "Master";
		String obj2 = "Master";
		String obj3 = "Car";
		String obj4 = "car";
		String obj5 = null;
		String obj6 = obj3;
		
		int number = 100;
		int[] num1 = { 10, 14, -1 };
		int[] num2 = { 10, 14, -1 };

		assertEquals(obj1, obj2); // Pass
		assertNotEquals(obj3, obj4); // Pass
		assertSame(obj3, obj6); // Pass
		assertNotSame(obj1, obj3); // Pass
		assertNull(obj5); // Pass
		assertNotNull(number); // Pass
		assertTrue((number + 10) > 11); // Pass
		assertFalse(number == 15); // Pass
		assertArrayEquals(num1, num2); // Pass
		
		// IllegalArgumentException => Exception => Throwable
		assertThrows(Exception.class, () -> error(5));
		assertThrowsExactly(IllegalArgumentException.class, () -> error(5));		
	}
	
	void error(int age) throws IllegalArgumentException {
		if (age == 5) {
			throw new IllegalArgumentException("Bad Age.");
		}
	}

}
