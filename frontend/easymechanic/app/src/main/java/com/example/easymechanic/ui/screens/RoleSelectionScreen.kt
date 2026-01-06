package com.example.easymechanic.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easymechanic.R

@Composable
fun RoleSelectionScreen(
    onUserClick: () -> Unit,
    onMechanicClick: () -> Unit
) {
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "role_animation")
    
    // Card scale animation
    val cardScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_scale"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F5F5),
                        Color(0xFFE0E0E0)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = stringResource(id = R.string.select_role),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // User Card
            RoleCard(
                title = stringResource(id = R.string.i_am_user),
                icon = Icons.Default.Person,
                iconColor = Color(0xFF2196F3),
                backgroundColor = Color(0xFFE3F2FD),
                onClick = onUserClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(cardScale)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Mechanic Card
            RoleCard(
                title = stringResource(id = R.string.i_am_mechanic),
                icon = Icons.Default.Build,
                iconColor = Color(0xFFFF9800),
                backgroundColor = Color(0xFFFFF3E0),
                onClick = onMechanicClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(cardScale)
            )
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Icon
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(48.dp),
                tint = iconColor
            )
            
            Spacer(modifier = Modifier.width(24.dp))
            
            // Title
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                modifier = Modifier.weight(1f)
            )
            
            // Arrow icon
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF757575)
            )
        }
    }
}

