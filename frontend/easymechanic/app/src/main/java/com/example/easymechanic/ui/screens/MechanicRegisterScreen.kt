package com.example.easymechanic.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easymechanic.ui.viewmodel.AuthUiState
import com.example.easymechanic.ui.viewmodel.AuthViewModel

@Composable
fun MechanicRegisterScreen(
    authViewModel: AuthViewModel,
    authState: AuthUiState,
    onRegisterClick: (String, String, String, String, String?, Int) -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var experienceYears by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val isLoading = authState is AuthUiState.Loading
    val errorMessage = if (authState is AuthUiState.Error) authState.message else null
    
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "mechanic_register_animation")
    
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )
    
    val contentAlpha = remember { Animatable(0f) }
    val contentOffset = remember { Animatable(30f) }
    
    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        contentOffset.animateTo(0f, animationSpec = tween(800, easing = FastOutSlowInEasing))
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFF9800),
                        Color(0xFFFF6F00),
                        Color(0xFFE65100)
                    )
                )
            )
    ) {
        val offsetY = contentOffset.value
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .alpha(contentAlpha.value)
                .offset(x = 0.dp, y = offsetY.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Mechanic Logo",
                tint = Color.White,
                modifier = Modifier
                    .size(80.dp)
                    .scale(logoScale)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Join as Mechanic",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Start providing services",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (errorMessage != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = errorMessage,
                                modifier = Modifier.padding(12.dp),
                                color = Color(0xFFC62828),
                                fontSize = 14.sp
                            )
                        }
                    }
                    
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name", fontWeight = FontWeight.Medium) },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFFFF9800)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF9800),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        enabled = !isLoading
                    )
                    
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", fontWeight = FontWeight.Medium) },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFFFF9800)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF9800),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        enabled = !isLoading
                    )
                    
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone", fontWeight = FontWeight.Medium) },
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = Color(0xFFFF9800)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF9800),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        enabled = !isLoading
                    )
                    
                    OutlinedTextField(
                        value = specialization,
                        onValueChange = { specialization = it },
                        label = { Text("Specialization (Optional)", fontWeight = FontWeight.Medium) },
                        leadingIcon = { Icon(Icons.Default.Build, null, tint = Color(0xFFFF9800)) },
                        placeholder = { Text("e.g., Engine Repair, AC Service") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF9800),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        enabled = !isLoading
                    )
                    
                    OutlinedTextField(
                        value = experienceYears,
                        onValueChange = { if (it.all { char -> char.isDigit() }) experienceYears = it },
                        label = { Text("Experience (Years)", fontWeight = FontWeight.Medium) },
                        leadingIcon = { Icon(Icons.Default.Info, null, tint = Color(0xFFFF9800)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF9800),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        enabled = !isLoading
                    )
                    
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", fontWeight = FontWeight.Medium) },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFFFF9800)) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.CheckCircle else Icons.Default.Lock,
                                    null,
                                    tint = Color(0xFFFF9800)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF9800),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        enabled = !isLoading
                    )
                    
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password", fontWeight = FontWeight.Medium) },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFFFF9800)) },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    if (confirmPasswordVisible) Icons.Default.CheckCircle else Icons.Default.Lock,
                                    null,
                                    tint = Color(0xFFFF9800)
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF9800),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(
                                color = Color(0xFFFF9800),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable(enabled = !isLoading && name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && password.isNotBlank() && password == confirmPassword) {
                                onRegisterClick(
                                    name,
                                    email,
                                    phone,
                                    password,
                                    specialization.takeIf { it.isNotBlank() },
                                    experienceYears.toIntOrNull() ?: 0
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Register", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Already have an account? ", fontSize = 14.sp, color = Color(0xFF757575))
                        Text(
                            "Login",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF9800),
                            modifier = Modifier.clickable(enabled = !isLoading) { onLoginClick() }
                        )
                    }
                }
            }
        }
    }
}

