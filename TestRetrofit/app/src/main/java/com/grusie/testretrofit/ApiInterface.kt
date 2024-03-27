package com.grusie.testretrofit

import com.grusie.testretrofit.model.UserInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("users/")
    fun getUserData(
        @Query("id")id: String?
    ): Call<UserInfo>
}