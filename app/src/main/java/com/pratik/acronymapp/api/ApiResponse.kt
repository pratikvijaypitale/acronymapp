package com.pratik.acronymapp.api

/**
 * Generic API response class to handle success and error state
 */
sealed class ApiResponse<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loader<T>: ApiResponse<T>()
    class Success<T>(data: T? = null) : ApiResponse<T>(data = data)
    class Error<T>(errorMessage: String) : ApiResponse<T>(errorMessage = errorMessage)
}