package com.example.easymechanic.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easymechanic.ui.components.QuickActionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicHomeScreen(
    mechanicName: String = "Mechanic",
    onRequestsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onUpdateLocationClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    isAvailable: Boolean = true,
    onToggleAvailability: (Boolean) -> Unit = {}
) {
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "mechanic_home_animation")
    
    val cardScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_scale"
    )
    
    val contentAlpha = remember { Animatable(0f) }
    val contentOffset = remember { Animatable(30f) }
    
    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        contentOffset.animateTo(0f, animationSpec = tween(800, easing = FastOutSlowInEasing))
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Welcome, $mechanicName",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isAvailable) "Available" else "Busy",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF9800)
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .alpha(contentAlpha.value)
                .offset(x = 0.dp, y = contentOffset.value.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Availability Toggle Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(cardScale),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isAvailable) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isAvailable) "You're Available" else "You're Busy",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isAvailable) "Receiving new requests" else "Not accepting new requests",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                            Switch(
                                checked = isAvailable,
                                onCheckedChange = onToggleAvailability,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color.White.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                }
                
                // Quick Stats
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Pending",
                            value = "5",
                            icon = Icons.Default.Info,
                            color = Color(0xFFFF9800),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Active",
                            value = "2",
                            icon = Icons.Default.Build,
                            color = Color(0xFF2196F3),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Completed",
                            value = "24",
                            icon = Icons.Default.CheckCircle,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                // Quick Actions
                item {
                    Text(
                        text = "Quick Actions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionCard(
                            title = "New Requests",
                            icon = Icons.Default.Notifications,
                            color = Color(0xFFFF9800),
                            modifier = Modifier.weight(1f),
                            onClick = onRequestsClick
                        )
                        QuickActionCard(
                            title = "Update Location",
                            icon = Icons.Default.LocationOn,
                            color = Color(0xFF2196F3),
                            modifier = Modifier.weight(1f),
                            onClick = onUpdateLocationClick
                        )
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionCard(
                            title = "History",
                            icon = Icons.Default.Info,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f),
                            onClick = onHistoryClick
                        )
                        QuickActionCard(
                            title = "Profile",
                            icon = Icons.Default.Person,
                            color = Color(0xFF9C27B0),
                            modifier = Modifier.weight(1f),
                            onClick = onProfileClick
                        )
                    }
                }
                
                // Recent Activity
                item {
                    Text(
                        text = "Recent Activity",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(3) { index ->
                    ActivityCard(
                        title = "Request #${1000 + index}",
                        description = "Engine repair requested",
                        time = "${index + 1} hours ago",
                        status = if (index == 0) "Pending" else if (index == 1) "Active" else "Completed"
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF757575)
            )
        }
    }
}

// QuickActionCard is defined in UserHomeScreen.kt - using that one

@Composable
fun ActivityCard(
    title: String,
    description: String,
    time: String,
    status: String
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = Color(0xFF9E9E9E)
                )
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = when (status) {
                        "Pending" -> Color(0xFFFF9800).copy(alpha = 0.2f)
                        "Active" -> Color(0xFF2196F3).copy(alpha = 0.2f)
                        else -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                    }
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = when (status) {
                        "Pending" -> Color(0xFFFF9800)
                        "Active" -> Color(0xFF2196F3)
                        else -> Color(0xFF4CAF50)
                    }
                )
            }
        }
    }
}

