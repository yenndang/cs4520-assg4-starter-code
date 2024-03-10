package com.cs4520.assignment4.api

import com.cs4520.assignment4.Api
import com.cs4520.assignment4.models.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Api.ENDPOINT)
    suspend fun getProducts(@Query("page") pageNumber: Int?): Response<List<Product>>
}
