package com.example.easymechanic.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymechanic.data.model.LoginRequest
import com.example.easymechanic.data.model.RegisterRequest
import com.example.easymechanic.data.model.User
import com.example.easymechanic.data.repository.AuthRepository
import com.example.easymechanic.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository()
    private val preferencesManager = PreferencesManager(application)
    
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState
    
    fun registerUser(
        name: String,
        email: String,
        phone: String,
        password: String,
        vehicleType: String?,
        vehicleNumber: String?
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = RegisterRequest(name, email, phone, password, vehicleType, vehicleNumber)
            val result = authRepository.registerUser(request)
            
            result.onSuccess { user ->
                // Save user data
                preferencesManager.saveUserData(
                    token = user.token,
                    userId = user.userId,
                    name = user.name,
                    email = user.email,
                    phone = user.phone,
                    userType = user.userType,
                    vehicleType = user.vehicleType,
                    vehicleNumber = user.vehicleNumber
                )
                _uiState.value = AuthUiState.Success(user)
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(error.message ?: "Registration failed")
            }
        }
    }
    
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = LoginRequest(email, password)
            val result = authRepository.loginUser(request)
            
            result.onSuccess { user ->
                // Save user data
                preferencesManager.saveUserData(
                    token = user.token,
                    userId = user.userId,
                    name = user.name,
                    email = user.email,
                    phone = user.phone,
                    userType = user.userType,
                    vehicleType = user.vehicleType,
                    vehicleNumber = user.vehicleNumber
                )
                _uiState.value = AuthUiState.Success(user)
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(error.message ?: "Login failed")
            }
        }
    }
    
    fun registerMechanic(
        name: String,
        email: String,
        phone: String,
        password: String,
        specialization: String?,
        experienceYears: Int
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = com.example.easymechanic.data.model.MechanicRegisterRequest(
                name, email, phone, password, specialization, experienceYears
            )
            val result = authRepository.registerMechanic(request)
            
            result.onSuccess { mechanic ->
                // Save mechanic data
                preferencesManager.saveMechanicData(
                    token = mechanic.token,
                    mechanicId = mechanic.mechanicId,
                    name = mechanic.name,
                    email = mechanic.email,
                    phone = mechanic.phone,
                    specialization = mechanic.specialization,
                    experienceYears = mechanic.experienceYears,
                    rating = mechanic.rating,
                    isAvailable = mechanic.isAvailable
                )
                _uiState.value = AuthUiState.Success(com.example.easymechanic.data.model.User(
                    userId = mechanic.mechanicId,
                    name = mechanic.name,
                    email = mechanic.email,
                    phone = mechanic.phone,
                    vehicleType = null,
                    vehicleNumber = null,
                    userType = "mechanic",
                    token = mechanic.token
                ))
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(error.message ?: "Registration failed")
            }
        }
    }
    
    fun loginMechanic(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = com.example.easymechanic.data.model.MechanicLoginRequest(email, password)
            val result = authRepository.loginMechanic(request)
            
            result.onSuccess { mechanic ->
                // Save mechanic data
                preferencesManager.saveMechanicData(
                    token = mechanic.token,
                    mechanicId = mechanic.mechanicId,
                    name = mechanic.name,
                    email = mechanic.email,
                    phone = mechanic.phone,
                    specialization = mechanic.specialization,
                    experienceYears = mechanic.experienceYears,
                    rating = mechanic.rating,
                    isAvailable = mechanic.isAvailable
                )
                _uiState.value = AuthUiState.Success(com.example.easymechanic.data.model.User(
                    userId = mechanic.mechanicId,
                    name = mechanic.name,
                    email = mechanic.email,
                    phone = mechanic.phone,
                    vehicleType = null,
                    vehicleNumber = null,
                    userType = "mechanic",
                    token = mechanic.token
                ))
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(error.message ?: "Login failed")
            }
        }
    }
    
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

