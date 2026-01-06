package com.example.easymechanic.data.repository

import com.example.easymechanic.data.api.ApiClient
import com.example.easymechanic.data.model.TroubleshootRequest
import com.example.easymechanic.data.model.TroubleshootResponse

class TroubleshootRepository {
    private val apiService = ApiClient.apiService
    
    suspend fun troubleshoot(problemDescription: String): Result<TroubleshootResponse> {
        return try {
            val request = TroubleshootRequest(problemDescription = problemDescription.trim())
            val response = apiService.troubleshoot(request)
            
            // Log for debugging
            android.util.Log.d("TroubleshootRepository", "API Response - Success: ${response.success}, Message: ${response.message}")
            
            if (response.success) {
                if (response.data != null) {
                    android.util.Log.d("TroubleshootRepository", "Response Data - IssueType: ${response.data.issueType}, Solutions: ${response.data.stepByStepSolution?.size}")
                    Result.success(response.data)
                } else {
                    android.util.Log.e("TroubleshootRepository", "Response data is null")
                    Result.failure(Exception(response.message ?: "No troubleshooting data received"))
                }
            } else {
                android.util.Log.e("TroubleshootRepository", "API returned success=false: ${response.message}")
                Result.failure(Exception(response.message ?: "Failed to get troubleshooting guidance"))
            }
        } catch (e: Exception) {
            android.util.Log.e("TroubleshootRepository", "Exception in troubleshoot: ${e.message}", e)
            Result.failure(e)
        }
    }
}

