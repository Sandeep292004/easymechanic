package com.example.easymechanic.data.repository

import com.example.easymechanic.data.api.ApiClient
import com.example.easymechanic.data.model.LoginRequest
import com.example.easymechanic.data.model.RegisterRequest
import com.example.easymechanic.data.model.User

class AuthRepository {
    private val apiService = ApiClient.apiService
    
    suspend fun registerUser(request: RegisterRequest): Result<User> {
        return try {
            val response = apiService.userRegister(request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun loginUser(request: LoginRequest): Result<User> {
        return try {
            val response = apiService.userLogin(request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun registerMechanic(request: com.example.easymechanic.data.model.MechanicRegisterRequest): Result<com.example.easymechanic.data.model.Mechanic> {
        return try {
            val response = apiService.mechanicRegister(request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun loginMechanic(request: com.example.easymechanic.data.model.MechanicLoginRequest): Result<com.example.easymechanic.data.model.Mechanic> {
        return try {
            val response = apiService.mechanicLogin(request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

