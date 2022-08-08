package com.pratik.acronymapp.api

/*sealed class ApiResponse(val acromine: Acronym? = null, val errorMessage: String? = null) {
    class Loading : ApiResponse()
    class Success(acromine: Acronym) : ApiResponse(acromine = acromine)
    class Error(errorMessage: String) : ApiResponse(errorMessage = errorMessage)
}*/

/**
 * Generic API response class to handle success and error state
 */
sealed class ApiResponse<T>(val data: T? = null, val errorMessage: String? = null) {
    class Success<T>(data: T? = null) : ApiResponse<T>(data = data)
    class Error<T>(errorMessage: String) : ApiResponse<T>(errorMessage = errorMessage)
}