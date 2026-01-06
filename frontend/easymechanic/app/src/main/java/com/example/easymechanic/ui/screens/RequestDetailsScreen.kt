package com.example.easymechanic.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDetailsScreen(
    requestId: Int,
    onBackClick: () -> Unit,
    onPayClick: () -> Unit = {}
) {
    // TODO: Load request details from API
    val request = remember { mutableStateOf<com.example.easymechanic.data.model.ServiceRequest?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Request Details",
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
        },
        bottomBar = {
            if (request.value?.status == "completed" && request.value?.actualCost != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Button(
                        onClick = onPayClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF9800),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, "Pay", modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pay ₹${request.value?.actualCost}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE3F2FD),
                            Color(0xFFBBDEFB),
                            Color.White
                        )
                    )
                )
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2196F3))
                }
            } else if (request.value != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Status Card
                    val statusColor = when (request.value!!.status.lowercase()) {
                        "pending" -> Color(0xFFFF9800)
                        "accepted" -> Color(0xFF2196F3)
                        "in_progress" -> Color(0xFF9C27B0)
                        "completed" -> Color(0xFF4CAF50)
                        else -> Color(0xFF9E9E9E)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = statusColor.copy(alpha = 0.1f)
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
                                    text = "Status",
                                    fontSize = 14.sp,
                                    color = Color(0xFF757575)
                                )
                                Text(
                                    text = request.value!!.status.replace("_", " ").uppercase(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            }
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Status",
                                modifier = Modifier.size(32.dp),
                                tint = statusColor
                            )
                        }
                    }

                    // Issue Description
                    DetailCard(
                        title = "Issue Description",
                        icon = Icons.Default.Info,
                        content = request.value!!.issueDescription
                    )

                    // Mechanic Info
                    if (request.value!!.mechanicName != null) {
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
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        Color(0xFF2196F3),
                                                        Color(0xFF1976D2)
                                                    )
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Build,
                                            contentDescription = "Mechanic",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Column {
                                        val currentRequest = request.value!!
                                        val mechanicName = currentRequest.mechanicName ?: ""
                                        val mechanicPhone = currentRequest.mechanicPhone
                                        Text(
                                            text = mechanicName,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF212121)
                                        )
                                        if (mechanicPhone != null) {
                                            Text(
                                                text = mechanicPhone,
                                                fontSize = 14.sp,
                                                color = Color(0xFF757575)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Cost Information
                    if (request.value!!.estimatedCost != null || request.value!!.actualCost != null) {
                        val costText = buildString {
                            if (request.value!!.estimatedCost != null) {
                                append("Estimated: ₹${request.value!!.estimatedCost}")
                            }
                            if (request.value!!.actualCost != null) {
                                if (isNotEmpty()) append("\n")
                                append("Actual: ₹${request.value!!.actualCost}")
                            }
                        }
                        DetailCard(
                            title = "Cost",
                            icon = Icons.Default.CheckCircle,
                            content = costText
                        )
                    }

                    // Location
                    if (request.value!!.address != null) {
                        DetailCard(
                            title = "Location",
                            icon = Icons.Default.LocationOn,
                            content = request.value!!.address ?: ""
                        )
                    }

                    // Timestamps
                    DetailCard(
                        title = "Created",
                        icon = Icons.Default.Info,
                        content = request.value!!.createdAt
                    )
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: String
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(icon, title, tint = Color(0xFF2196F3))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            }
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
        }
    }
}

