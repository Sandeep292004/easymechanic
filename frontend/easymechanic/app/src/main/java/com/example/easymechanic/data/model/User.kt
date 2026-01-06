package com.example.easymechanic.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("vehicle_type") val vehicleType: String? = null,
    @SerializedName("vehicle_number") val vehicleNumber: String? = null,
    @SerializedName("user_type") val userType: String,
    @SerializedName("token") val token: String
)

