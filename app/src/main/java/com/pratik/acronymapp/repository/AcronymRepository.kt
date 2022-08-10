package com.pratik.acronymapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pratik.acronymapp.api.AcronymApiService
import com.pratik.acronymapp.api.ApiResponse
import com.pratik.acronymapp.models.Acronym
import kotlinx.coroutines.delay

//Repository class to handle API and Database related methods
class AcronymRepository(private val acronymService: AcronymApiService) {
    private val internalAcronymLiveData = MutableLiveData<ApiResponse<Acronym>>()
    val acronymLivaData: LiveData<ApiResponse<Acronym>>
        get() = internalAcronymLiveData

    suspend fun getAcronymDetails(acronymInput: String) {
        try {
            val result = acronymService.getAcronym(acronymInput)
            if (result.body() != null) {
                internalAcronymLiveData.postValue(ApiResponse.Success(result.body()))
            } else {
                internalAcronymLiveData.postValue(ApiResponse.Error("Result is empty"))
            }
        } catch (e: Exception) {
            internalAcronymLiveData.postValue(ApiResponse.Error(e.message.toString()))
        }
    }

}