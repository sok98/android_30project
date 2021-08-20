package com.yeseul.part3.chapter07

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/d643417c-700f-4034-b4d9-476c94870fad")
    fun getHouseList(): Call<HouseDto>
}