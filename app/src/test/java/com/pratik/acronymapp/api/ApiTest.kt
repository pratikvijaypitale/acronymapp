package com.pratik.acronymapp.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

//Test to check Acronym API result
class ApiTest {

    private lateinit var apiService: AcronymApiService

    @Before
    fun setUp() {
        apiService = RetrofitHelper.getInstance().create(AcronymApiService::class.java)
    }

    @Test
    fun testApi() {
        runBlocking {
            launch(Dispatchers.IO) {
                val response = apiService.getAcronym("")
                assertEquals(response.code(), 200)
            }
        }
    }
}