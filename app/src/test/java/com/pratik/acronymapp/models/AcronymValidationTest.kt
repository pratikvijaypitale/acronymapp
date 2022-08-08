package com.pratik.acronymapp.models

import org.junit.Assert.*

import org.junit.Test

//Acronym user input validation test
class AcronymValidationTest {
    @Test
    fun validateAcronym() {
        val result = AcronymValidation.isValidInput("AB")
        assertFalse(result)
    }
}