package com.example.easymechanic.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "easymechanic_prefs",
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_VEHICLE_TYPE = "vehicle_type"
        private const val KEY_VEHICLE_NUMBER = "vehicle_number"
        private const val KEY_ADDRESS = "address"
        private const val KEY_CITY = "city"
        private const val KEY_STATE = "state"
        private const val KEY_PINCODE = "pincode"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        // Mechanic specific
        private const val KEY_MECHANIC_ID = "mechanic_id"
        private const val KEY_SPECIALIZATION = "specialization"
        private const val KEY_EXPERIENCE_YEARS = "experience_years"
        private const val KEY_RATING = "rating"
        private const val KEY_IS_AVAILABLE = "is_available"
    }
    
    fun saveUserData(
        token: String,
        userId: Int,
        name: String,
        email: String,
        phone: String,
        userType: String,
        vehicleType: String? = null,
        vehicleNumber: String? = null,
        address: String? = null,
        city: String? = null,
        state: String? = null,
        pincode: String? = null
    ) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_PHONE, phone)
            putString(KEY_USER_TYPE, userType)
            putString(KEY_VEHICLE_TYPE, vehicleType)
            putString(KEY_VEHICLE_NUMBER, vehicleNumber)
            putString(KEY_ADDRESS, address)
            putString(KEY_CITY, city)
            putString(KEY_STATE, state)
            putString(KEY_PINCODE, pincode)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    
    fun getUserName(): String? = prefs.getString(KEY_USER_NAME, null)
    
    fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)
    
    fun getUserPhone(): String? = prefs.getString(KEY_USER_PHONE, null)
    
    fun getUserType(): String? = prefs.getString(KEY_USER_TYPE, null)
    
    fun getVehicleType(): String? = prefs.getString(KEY_VEHICLE_TYPE, null)
    
    fun getVehicleNumber(): String? = prefs.getString(KEY_VEHICLE_NUMBER, null)
    
    fun getAddress(): String? = prefs.getString(KEY_ADDRESS, null)
    
    fun getCity(): String? = prefs.getString(KEY_CITY, null)
    
    fun getState(): String? = prefs.getString(KEY_STATE, null)
    
    fun getPincode(): String? = prefs.getString(KEY_PINCODE, null)
    
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    
    fun saveMechanicData(
        token: String,
        mechanicId: Int,
        name: String,
        email: String,
        phone: String,
        specialization: String? = null,
        experienceYears: Int = 0,
        rating: Double = 0.0,
        isAvailable: Boolean = true
    ) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putInt(KEY_USER_ID, mechanicId)
            putInt(KEY_MECHANIC_ID, mechanicId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_PHONE, phone)
            putString(KEY_USER_TYPE, "mechanic")
            putString(KEY_SPECIALIZATION, specialization)
            putInt(KEY_EXPERIENCE_YEARS, experienceYears)
            putFloat(KEY_RATING, rating.toFloat())
            putBoolean(KEY_IS_AVAILABLE, isAvailable)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    fun getMechanicId(): Int = prefs.getInt(KEY_MECHANIC_ID, -1)
    fun getSpecialization(): String? = prefs.getString(KEY_SPECIALIZATION, null)
    fun getExperienceYears(): Int = prefs.getInt(KEY_EXPERIENCE_YEARS, 0)
    fun getRating(): Double = prefs.getFloat(KEY_RATING, 0f).toDouble()
    fun isAvailable(): Boolean = prefs.getBoolean(KEY_IS_AVAILABLE, true)
    
    fun clearUserData() {
        prefs.edit().clear().apply()
    }
}

