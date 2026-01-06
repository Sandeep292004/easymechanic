package com.example.easymechanic.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Google Maps imports removed - using placeholder for now
// Add when Google Maps API key is configured

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicRequestDetailsScreen(
    requestId: Int,
    userLatitude: Double? = null,
    userLongitude: Double? = null,
    userPhone: String? = null,
    userName: String? = null,
    userEmail: String? = null,
    issueDescription: String? = null,
    vehicleType: String? = null,
    vehicleNumber: String? = null,
    address: String? = null,
    onBackClick: () -> Unit = {},
    onAcceptClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {},
    onCallClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var requestStatus by remember { mutableStateOf("Pending") }
    
    val contentAlpha = remember { Animatable(0f) }
    val contentOffset = remember { Animatable(30f) }
    
    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        contentOffset.animateTo(0f, animationSpec = tween(800, easing = FastOutSlowInEasing))
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF9800)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .alpha(contentAlpha.value)
                .offset(x = 0.dp, y = contentOffset.value.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (requestStatus) {
                        "Pending" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                        "Active" -> Color(0xFF2196F3).copy(alpha = 0.1f)
                        else -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Request #$requestId",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Status: $requestStatus",
                            fontSize = 14.sp,
                            color = Color(0xFF757575)
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when (requestStatus) {
                                "Pending" -> Color(0xFFFF9800)
                                "Active" -> Color(0xFF2196F3)
                                else -> Color(0xFF4CAF50)
                            }
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = requestStatus,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            
            // User Info Card
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
                        text = "Customer Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    
                    InfoRow(icon = Icons.Default.Person, label = "Name", value = userName ?: "N/A")
                    InfoRow(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = userPhone ?: "N/A",
                        onClick = if (userPhone != null) {
                            {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${userPhone}"))
                                context.startActivity(intent)
                                onCallClick(userPhone)
                            }
                        } else null
                    )
                    InfoRow(icon = Icons.Default.Email, label = "Email", value = userEmail ?: "N/A")
                    InfoRow(icon = Icons.Default.LocationOn, label = "Address", value = address ?: "Location shared")
                }
            }
            
            // Location Map Card
            if (userLatitude != null && userLongitude != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color(0xFF4CAF50)
                            )
                            Text(
                                text = "Customer Location",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121)
                            )
                        }
                        
                        // Map placeholder - Replace with actual GoogleMap when API key is added
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = "Map",
                                    modifier = Modifier.size(48.dp),
                                    tint = Color(0xFF4CAF50)
                                )
                                Text(
                                    text = "Location: ${String.format("%.6f", userLatitude)}, ${String.format("%.6f", userLongitude)}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF757575)
                                )
                                Text(
                                    text = "Add Google Maps API key to enable map view",
                                    fontSize = 10.sp,
                                    color = Color(0xFF9E9E9E)
                                )
                            }
                        }
                        
                        Button(
                            onClick = {
                                val gmmIntentUri = Uri.parse("google.navigation:q=${userLatitude},${userLongitude}")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                context.startActivity(mapIntent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {
                            Icon(Icons.Default.LocationOn, "Navigate", modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Navigate to Location")
                        }
                    }
                }
            }
            
            // Issue Details Card
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
                        text = "Issue Description",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Text(
                        text = issueDescription ?: "No description provided",
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        lineHeight = 20.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (vehicleType != null) {
                        InfoRow(icon = Icons.Default.Build, label = "Vehicle Type", value = vehicleType)
                    }
                    if (vehicleNumber != null) {
                        InfoRow(icon = Icons.Default.Info, label = "Vehicle Number", value = vehicleNumber)
                    }
                }
            }
            
            // Action Buttons
            if (requestStatus == "Pending") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            color = Color(0xFFFF9800),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(onClick = {
                            requestStatus = "Active"
                            onAcceptClick()
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Accept Request",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else if (requestStatus == "Active") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            color = Color(0xFF4CAF50),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(onClick = {
                            requestStatus = "Completed"
                            onCompleteClick()
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mark as Completed",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFFFF9800),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF212121)
            )
        }
        if (onClick != null) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Action",
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

