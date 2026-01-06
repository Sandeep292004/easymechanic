package com.example.easymechanic.data.api

import com.example.easymechanic.data.model.*
import retrofit2.http.*

interface EasyMechanicApi {
    // Authentication
    @POST("auth/user_register.php")
    suspend fun userRegister(@Body request: RegisterRequest): ApiResponse<User>
    
    @POST("auth/user_login.php")
    suspend fun userLogin(@Body request: LoginRequest): ApiResponse<User>
    
    // Mechanics
    @GET("mechanic/find_mechanics.php")
    suspend fun findMechanics(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double = 10.0
    ): MechanicsResponse
    
    // Service Requests
    @POST("requests/create_request.php")
    suspend fun createRequest(
        @Header("Authorization") token: String,
        @Body request: CreateRequestRequest
    ): ApiResponse<ServiceRequest>
    
    @GET("requests/get_requests.php")
    suspend fun getRequests(
        @Header("Authorization") token: String,
        @Query("type") type: String = "user",
        @Query("status") status: String? = null
    ): ServiceRequestsResponse
    
    // Payments
    @POST("payment/process_payment.php")
    suspend fun processPayment(
        @Header("Authorization") token: String,
        @Body request: ProcessPaymentRequest
    ): ApiResponse<Payment>
    
    @GET("payment/get_payments.php")
    suspend fun getPayments(
        @Header("Authorization") token: String
    ): PaymentsResponse
    
    // AI Troubleshooting
    @POST("ai/troubleshoot.php")
    suspend fun troubleshoot(
        @Body request: TroubleshootRequest
    ): ApiResponse<TroubleshootResponse>
    
    // Mechanic Authentication
    @POST("auth/mechanic_register.php")
    suspend fun mechanicRegister(@Body request: MechanicRegisterRequest): ApiResponse<Mechanic>
    
    @POST("auth/mechanic_login.php")
    suspend fun mechanicLogin(@Body request: MechanicLoginRequest): ApiResponse<Mechanic>
    
    // Mechanic Features
    @POST("mechanic/update_location.php")
    suspend fun updateLocation(
        @Header("Authorization") token: String,
        @Body request: UpdateLocationRequest
    ): ApiResponse<Map<String, Any>>
    
    @POST("mechanic/toggle_availability.php")
    suspend fun toggleAvailability(
        @Header("Authorization") token: String,
        @Body request: ToggleAvailabilityRequest
    ): ApiResponse<Map<String, Any>>
    
    // Service Requests (for mechanics)
    @POST("requests/accept_request.php")
    suspend fun acceptRequest(
        @Header("Authorization") token: String,
        @Body request: AcceptRequestRequest
    ): ApiResponse<ServiceRequest>
    
    @POST("requests/complete_request.php")
    suspend fun completeRequest(
        @Header("Authorization") token: String,
        @Body request: CompleteRequestRequest
    ): ApiResponse<ServiceRequest>
}

