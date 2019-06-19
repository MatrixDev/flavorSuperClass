package com.matrixdev.flavorsuperclass

import org.junit.Test

import org.junit.Assert.*

/**
 * @author Rostyslav.Lesovyi
 */
class MainClassTest {
	@Test
	fun testSimpleConstructorClass() {
		val value = SimpleConstructorClass()

		assertEquals(value.flavor, "flavor")
	}

	@Test
	fun testConstructorWithArgClass() {
		val value = ConstructorWithArgClass("1")

		assertEquals(value.arg, "1")
		assertEquals(value.flavor, "flavor")
	}

	@Test
	fun testConstructorWithGenericClass() {
		val value = ConstructorWithGenericClass(1)

		assertEquals(value.arg, 1)
		assertEquals(value.flavor, "flavor")
	}
}
