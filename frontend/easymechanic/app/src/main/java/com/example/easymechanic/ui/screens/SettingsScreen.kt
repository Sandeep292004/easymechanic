package com.example.easymechanic.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easymechanic.utils.PreferencesManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferencesManager: PreferencesManager,
    onBackClick: () -> Unit,
    onSaveClick: (Map<String, String>) -> Unit = {}
) {
    // Load current user data
    val currentName = remember { mutableStateOf(preferencesManager.getUserName() ?: "") }
    val currentEmail = remember { mutableStateOf(preferencesManager.getUserEmail() ?: "") }
    val currentPhone = remember { mutableStateOf(preferencesManager.getUserPhone() ?: "") }
    val currentVehicleType = remember { mutableStateOf(preferencesManager.getVehicleType() ?: "") }
    val currentVehicleNumber = remember { mutableStateOf(preferencesManager.getVehicleNumber() ?: "") }
    val currentAddress = remember { mutableStateOf(preferencesManager.getAddress() ?: "") }
    val currentCity = remember { mutableStateOf(preferencesManager.getCity() ?: "") }
    val currentState = remember { mutableStateOf(preferencesManager.getState() ?: "") }
    val currentPincode = remember { mutableStateOf(preferencesManager.getPincode() ?: "") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3),
                            Color(0xFF1976D2),
                            Color(0xFFE3F2FD)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Personal Information Section
                SettingsSectionCard(
                    title = "Personal Information",
                    icon = Icons.Default.Person
                ) {
                    SettingsTextField(
                        label = "Full Name",
                        value = currentName.value,
                        onValueChange = { currentName.value = it },
                        icon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text
                    )
                    SettingsTextField(
                        label = "Email",
                        value = currentEmail.value,
                        onValueChange = { currentEmail.value = it },
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        enabled = false // Email usually can't be changed
                    )
                    SettingsTextField(
                        label = "Phone Number",
                        value = currentPhone.value,
                        onValueChange = { currentPhone.value = it },
                        icon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone
                    )
                }

                // Vehicle Information Section
                SettingsSectionCard(
                    title = "Vehicle Information",
                    icon = Icons.Default.Build
                ) {
                    SettingsTextField(
                        label = "Vehicle Type",
                        value = currentVehicleType.value,
                        onValueChange = { currentVehicleType.value = it },
                        icon = Icons.Default.Build,
                        placeholder = "e.g., Car, Bike, Truck",
                        keyboardType = KeyboardType.Text
                    )
                    SettingsTextField(
                        label = "Vehicle Number",
                        value = currentVehicleNumber.value,
                        onValueChange = { currentVehicleNumber.value = it },
                        icon = Icons.Default.Settings,
                        placeholder = "e.g., ABC1234",
                        keyboardType = KeyboardType.Text
                    )
                }

                // Address Information Section
                SettingsSectionCard(
                    title = "Address Information",
                    icon = Icons.Default.LocationOn
                ) {
                    SettingsTextField(
                        label = "Address",
                        value = currentAddress.value,
                        onValueChange = { currentAddress.value = it },
                        icon = Icons.Default.LocationOn,
                        placeholder = "Street address",
                        keyboardType = KeyboardType.Text,
                        maxLines = 3
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SettingsTextField(
                            label = "City",
                            value = currentCity.value,
                            onValueChange = { currentCity.value = it },
                            icon = Icons.Default.LocationOn,
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Text
                        )
                        SettingsTextField(
                            label = "State",
                            value = currentState.value,
                            onValueChange = { currentState.value = it },
                            icon = Icons.Default.LocationOn,
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Text
                        )
                    }
                    SettingsTextField(
                        label = "Pincode",
                        value = currentPincode.value,
                        onValueChange = { currentPincode.value = it },
                        icon = Icons.Default.LocationOn,
                        placeholder = "e.g., 123456",
                        keyboardType = KeyboardType.Number
                    )
                }

                // Error/Success Messages
                if (errorMessage != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF44336)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp
                        )
                    }
                }

                if (successMessage != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = successMessage ?: "",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp
                        )
                    }
                }

                // Save Button
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        successMessage = null
                        
                        val settingsData = mapOf(
                            "name" to currentName.value,
                            "phone" to currentPhone.value,
                            "vehicle_type" to currentVehicleType.value,
                            "vehicle_number" to currentVehicleNumber.value,
                            "address" to currentAddress.value,
                            "city" to currentCity.value,
                            "state" to currentState.value,
                            "pincode" to currentPincode.value
                        )
                        
                        // Update local preferences
                        preferencesManager.saveUserData(
                            token = preferencesManager.getToken() ?: "",
                            userId = preferencesManager.getUserId(),
                            name = currentName.value,
                            email = currentEmail.value,
                            phone = currentPhone.value,
                            userType = preferencesManager.getUserType() ?: "user",
                            vehicleType = currentVehicleType.value.ifBlank { null },
                            vehicleNumber = currentVehicleNumber.value.ifBlank { null },
                            address = currentAddress.value.ifBlank { null },
                            city = currentCity.value.ifBlank { null },
                            state = currentState.value.ifBlank { null },
                            pincode = currentPincode.value.ifBlank { null }
                        )
                        
                        onSaveClick(settingsData)
                        isLoading = false
                        successMessage = "Settings saved successfully!"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Icon(Icons.Default.CheckCircle, "Save", modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save Settings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SettingsSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    icon,
                    title,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF2196F3)
                )
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            }
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            content()
        }
    }
}

@Composable
fun SettingsTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { if (placeholder.isNotBlank()) Text(placeholder) },
        leadingIcon = {
            Icon(icon, label, tint = Color(0xFF2196F3))
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        enabled = enabled,
        maxLines = maxLines,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2196F3),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedLabelColor = Color(0xFF2196F3),
            unfocusedLabelColor = Color(0xFF757575)
        )
    )
}

