package com.example.easymechanic.data.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
    @SerializedName("vehicle_type") val vehicleType: String? = null,
    @SerializedName("vehicle_number") val vehicleNumber: String? = null
)

