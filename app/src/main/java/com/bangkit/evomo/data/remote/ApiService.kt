package com.bangkit.evomo.data.remote

import com.bangkit.evomo.data.response.AgreeTnCResponse
import retrofit2.Call
import com.bangkit.evomo.data.response.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    fun login (
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("tnc")
    fun isAgree (
        @Header("x-authenticated-userid") userId: String,
    ): Call<AgreeTnCResponse>
}
