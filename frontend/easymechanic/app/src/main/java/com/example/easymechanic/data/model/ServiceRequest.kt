package com.example.easymechanic.data.model

import com.google.gson.annotations.SerializedName

data class ServiceRequest(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("mechanic_id") val mechanicId: Int?,
    @SerializedName("issue_description") val issueDescription: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("address") val address: String?,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("mechanic_name") val mechanicName: String?,
    @SerializedName("mechanic_phone") val mechanicPhone: String?,
    @SerializedName("specialization") val specialization: String?,
    @SerializedName("estimated_cost") val estimatedCost: Double?,
    @SerializedName("actual_cost") val actualCost: Double?,
    @SerializedName("vehicle_type") val vehicleType: String?,
    @SerializedName("vehicle_number") val vehicleNumber: String?,
    @SerializedName("priority") val priority: String?
)

data class ServiceRequestsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val requests: List<ServiceRequest>?
)

data class CreateRequestRequest(
    @SerializedName("issue_description") val issueDescription: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("address") val address: String?,
    @SerializedName("vehicle_type") val vehicleType: String?,
    @SerializedName("vehicle_number") val vehicleNumber: String?,
    @SerializedName("priority") val priority: String?
)

data class CreateRequestResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val request: ServiceRequest?
)

