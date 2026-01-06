package com.example.easymechanic.data.model

import com.google.gson.annotations.SerializedName

data class Mechanic(
    @SerializedName("mechanic_id") val mechanicId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("specialization") val specialization: String? = null,
    @SerializedName("experience_years") val experienceYears: Int = 0,
    @SerializedName("rating") val rating: Double = 0.0,
    @SerializedName("is_available") val isAvailable: Boolean = true,
    @SerializedName("user_type") val userType: String = "mechanic",
    @SerializedName("token") val token: String,
    @SerializedName("shop_name") val shopName: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("pincode") val pincode: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("distance_km") val distanceKm: Double? = null,
    @SerializedName("upi_id") val upiId: String? = null,
    @SerializedName("upi_qr_code") val upiQrCode: String? = null
)

data class MechanicRegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
    @SerializedName("specialization") val specialization: String? = null,
    @SerializedName("experience_years") val experienceYears: Int = 0
)

data class MechanicLoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class UpdateLocationRequest(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)

data class ToggleAvailabilityRequest(
    @SerializedName("is_available") val isAvailable: Boolean
)

data class MechanicsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: MechanicsData?
)

data class MechanicsData(
    @SerializedName("count") val count: Int,
    @SerializedName("search_radius_km") val searchRadiusKm: Double,
    @SerializedName("user_location") val userLocation: UserLocation?,
    @SerializedName("mechanics") val mechanics: List<Mechanic>?
)

data class UserLocation(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)

data class AcceptRequestRequest(
    @SerializedName("request_id") val requestId: Int
)

data class CompleteRequestRequest(
    @SerializedName("request_id") val requestId: Int,
    @SerializedName("actual_cost") val actualCost: Double? = null,
    @SerializedName("notes") val notes: String? = null
)
