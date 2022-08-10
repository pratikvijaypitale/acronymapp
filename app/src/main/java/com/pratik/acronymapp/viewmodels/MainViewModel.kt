package com.pratik.acronymapp.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratik.acronymapp.api.AcronymApiService
import com.pratik.acronymapp.api.ApiResponse
import com.pratik.acronymapp.api.RetrofitHelper
import com.pratik.acronymapp.models.Acronym
import com.pratik.acronymapp.repository.AcronymRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//ViewModel class of MainActivity
class MainViewModel : ViewModel() {

    private val TAG = "MainViewModel"

    private val repository: AcronymRepository by lazy {
        val apiService = RetrofitHelper.getInstance().create(AcronymApiService::class.java)
        AcronymRepository(apiService)
    }

    //Live data to handle API response
    val acronymApiResponseLiveData: MutableLiveData<ApiResponse<Acronym>>
        get() = repository.acronymLivaData as MutableLiveData<ApiResponse<Acronym>>

    //Live data to handle progressbar loading
    val progressBarLiveData = MutableLiveData(View.GONE)

    //API call to get acronym
    private fun getAcronym(input: String) {
        progressBarLiveData.postValue(View.VISIBLE)
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAcronymDetails(input)
        }
    }

    //This will execute when user start typing in search view
    fun onSearchTextChanged(data: String) {
        acronymApiResponseLiveData.postValue(ApiResponse.Loader())
        getAcronym(data)
    }
}