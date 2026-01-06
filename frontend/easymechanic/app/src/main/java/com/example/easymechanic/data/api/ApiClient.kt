package com.example.easymechanic.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // Base URL - Use 10.0.2.2 for Android emulator, or your computer's IP for physical device
    // For physical device: Replace with your computer's IP address (e.g., "http://192.168.1.100/easymechanic/api/")
    // IMPORTANT: Make sure XAMPP Apache is running and files are in C:\xampp\htdocs\easymechanic\api\
    // CRITICAL: NO SPACES after http:// - this causes "Invalid URL host" error!
    private const val BASE_URL = "http://10.163.250.243/easymechanic/api/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: EasyMechanicApi = retrofit.create(EasyMechanicApi::class.java)
}

