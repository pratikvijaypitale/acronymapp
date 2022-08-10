package com.pratik.acronymapp.models

//To validate user input
object AcronymValidation {

    private const val MIN_REQUIRED_INITIALS = 3

    /*
    Following function will return true only if input string's length is greater than 3
    * */
    fun isValidInput(input: String?): Boolean {
        if (input.isNullOrBlank()) return false
        return input.length >= MIN_REQUIRED_INITIALS
    }
}