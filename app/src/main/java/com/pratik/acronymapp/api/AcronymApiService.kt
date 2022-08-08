package com.pratik.acronymapp.api

import com.pratik.acronymapp.models.Acromine
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AcronymApiService {
    @GET("dictionary.py")
    suspend fun getAcronym(@Query("sf") sf : String) : Response<Acromine>
}