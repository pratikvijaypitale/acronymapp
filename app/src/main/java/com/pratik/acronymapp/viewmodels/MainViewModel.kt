package com.pratik.acronymapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratik.acronymapp.api.ApiResponse
import com.pratik.acronymapp.models.Acronym
import com.pratik.acronymapp.repository.AcronymRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository : AcronymRepository): ViewModel() {

    val acronymApiResponseLiveData: LiveData<ApiResponse<Acronym>>
    get() = repository.acronymLivaData

    fun getAcronym(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAcronymDetails(input)
        }
    }
}