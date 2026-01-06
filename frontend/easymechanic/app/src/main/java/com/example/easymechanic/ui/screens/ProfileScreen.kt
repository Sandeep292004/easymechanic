package com.example.easymechanic.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easymechanic.utils.PreferencesManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    preferencesManager: PreferencesManager,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onPaymentHistoryClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {}
) {
    val userName = preferencesManager.getUserName() ?: "User"
    val userEmail = preferencesManager.getUserEmail() ?: ""
    val userPhone = preferencesManager.getUserPhone() ?: ""
    val vehicleType = preferencesManager.getVehicleType() ?: ""
    val vehicleNumber = preferencesManager.getVehicleNumber() ?: ""
    val address = preferencesManager.getAddress() ?: ""
    val city = preferencesManager.getCity() ?: ""
    val state = preferencesManager.getState() ?: ""
    val pincode = preferencesManager.getPincode() ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
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
            ) {
                // Profile Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2196F3),
                                    Color(0xFF1976D2)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color.White,
                                            Color(0xFFE3F2FD)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(60.dp),
                                tint = Color(0xFF2196F3)
                            )
                        }
                        Text(
                            text = userName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Profile Details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileInfoCard(
                        title = "Email",
                        value = userEmail,
                        icon = Icons.Default.Email
                    )
                    ProfileInfoCard(
                        title = "Phone",
                        value = userPhone,
                        icon = Icons.Default.Phone
                    )
                    if (vehicleType.isNotBlank()) {
                        ProfileInfoCard(
                            title = "Vehicle Type",
                            value = vehicleType,
                            icon = Icons.Default.Build
                        )
                    }
                    if (vehicleNumber.isNotBlank()) {
                        ProfileInfoCard(
                            title = "Vehicle Number",
                            value = vehicleNumber,
                            icon = Icons.Default.Settings
                        )
                    }
                    
                    // Address Section
                    if (address.isNotBlank() || city.isNotBlank() || state.isNotBlank() || pincode.isNotBlank()) {
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
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        "Address",
                                        modifier = Modifier.size(32.dp),
                                        tint = Color(0xFF2196F3)
                                    )
                                    Text(
                                        text = "Address",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF212121)
                                    )
                                }
                                if (address.isNotBlank()) {
                                    Text(
                                        text = address,
                                        fontSize = 14.sp,
                                        color = Color(0xFF757575)
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (city.isNotBlank()) {
                                        Text(
                                            text = city,
                                            fontSize = 14.sp,
                                            color = Color(0xFF757575)
                                        )
                                    }
                                    if (state.isNotBlank()) {
                                        Text(
                                            text = if (city.isNotBlank()) ", $state" else state,
                                            fontSize = 14.sp,
                                            color = Color(0xFF757575)
                                        )
                                    }
                                    if (pincode.isNotBlank()) {
                                        Text(
                                            text = if (state.isNotBlank() || city.isNotBlank()) " - $pincode" else pincode,
                                            fontSize = 14.sp,
                                            color = Color(0xFF757575)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Settings Section
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
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Settings",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            
                            // Edit Profile
                            ProfileSettingItem(
                                title = "Edit Profile",
                                icon = Icons.Default.Edit,
                                onClick = onEditClick
                            )
                            
                            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                            
                            // Payment History
                            ProfileSettingItem(
                                title = "Payment History",
                                icon = Icons.Default.CheckCircle,
                                onClick = onPaymentHistoryClick
                            )
                            
                            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                            
                            // Service History
                            ProfileSettingItem(
                                title = "Service History",
                                icon = Icons.Default.Info,
                                onClick = onHistoryClick
                            )
                            
                            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                            
                            // Logout
                            ProfileSettingItem(
                                title = "Logout",
                                icon = Icons.Default.ArrowBack,
                                onClick = onLogoutClick,
                                textColor = Color(0xFFF44336)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                title,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF2196F3)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF212121)
                )
            }
        }
    }
}

@Composable
fun ProfileSettingItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    textColor: Color = Color(0xFF212121)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            title,
            modifier = Modifier.size(24.dp),
            tint = textColor
        )
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Default.ArrowForward,
            "Navigate",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF9E9E9E)
        )
    }
}

