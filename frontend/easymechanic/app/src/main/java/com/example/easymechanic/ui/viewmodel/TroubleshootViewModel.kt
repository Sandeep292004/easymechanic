package com.example.easymechanic.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymechanic.data.model.TroubleshootResponse
import com.example.easymechanic.data.repository.TroubleshootRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TroubleshootViewModel(application: Application) : AndroidViewModel(application) {
    private val troubleshootRepository = TroubleshootRepository()
    
    private val _uiState = MutableStateFlow<TroubleshootUiState>(TroubleshootUiState.Idle)
    val uiState: StateFlow<TroubleshootUiState> = _uiState
    
    fun troubleshoot(problemDescription: String) {
        if (problemDescription.isBlank()) {
            _uiState.value = TroubleshootUiState.Error("Please describe your vehicle problem")
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.value = TroubleshootUiState.Loading
                android.util.Log.d("TroubleshootViewModel", "Troubleshooting problem: $problemDescription")
                
                val result = troubleshootRepository.troubleshoot(problemDescription.trim())
                
                result.onSuccess { response ->
                    android.util.Log.d("TroubleshootViewModel", "Success - IssueType: ${response.issueType}")
                    _uiState.value = TroubleshootUiState.Success(response)
                }.onFailure { error ->
                    android.util.Log.e("TroubleshootViewModel", "Error: ${error.message}", error)
                    _uiState.value = TroubleshootUiState.Error(error.message ?: "Failed to get troubleshooting guidance. Please check your connection and try again.")
                }
            } catch (e: Exception) {
                android.util.Log.e("TroubleshootViewModel", "Exception: ${e.message}", e)
                _uiState.value = TroubleshootUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
    
    fun resetState() {
        _uiState.value = TroubleshootUiState.Idle
    }
}

sealed class TroubleshootUiState {
    object Idle : TroubleshootUiState()
    object Loading : TroubleshootUiState()
    data class Success(val response: TroubleshootResponse) : TroubleshootUiState()
    data class Error(val message: String) : TroubleshootUiState()
}

