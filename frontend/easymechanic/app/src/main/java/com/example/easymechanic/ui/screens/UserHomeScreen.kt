package com.example.easymechanic.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.easymechanic.ui.viewmodel.TroubleshootViewModel
import com.example.easymechanic.ui.viewmodel.TroubleshootUiState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easymechanic.R
import com.example.easymechanic.ui.components.QuickActionCard

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    userName: String = "User",
    onFindMechanicClick: () -> Unit = {},
    onMyRequestsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onEmergencyClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val troubleshootViewModel: TroubleshootViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
        )
    )
    val troubleshootState by troubleshootViewModel.uiState.collectAsState()
    
    var isChatExpanded by remember { mutableStateOf(false) }
    var chatInput by remember { mutableStateOf("") }
    var chatMessages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    var isSendingMessage by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    // Track last processed response ID to prevent duplicates
    var lastProcessedResponseId by remember { mutableStateOf<String?>(null) }
    
    // Handle troubleshoot state changes - use key to ensure it triggers
    LaunchedEffect(key1 = troubleshootState) {
        android.util.Log.d("UserHomeScreen", "LaunchedEffect triggered - State: ${troubleshootState::class.simpleName}")
        
        when (val currentState = troubleshootState) {
            is TroubleshootUiState.Loading -> {
                isSendingMessage = true
            }
            is TroubleshootUiState.Success -> {
                isSendingMessage = false
                
                try {
                    val response = currentState.response
                    // Create unique ID from response content to prevent duplicates
                    val responseId = "${response.issueType}_${response.stepByStepSolution?.joinToString("")?.hashCode()}_${System.currentTimeMillis()}"
                    
                    android.util.Log.d("UserHomeScreen", "Received response - IssueType: ${response.issueType}, ResponseID: $responseId")
                    
                    // Only add if this is a new response (different ID)
                    if (lastProcessedResponseId != responseId) {
                        val formattedResponse = formatTroubleshootResponse(response)
                        
                        android.util.Log.d("UserHomeScreen", "Adding new response to chat. Length: ${formattedResponse.length}")
                        
                        // Add AI response
                        chatMessages = chatMessages + ChatMessage(
                            text = formattedResponse,
                            isUser = false
                        )
                        
                        lastProcessedResponseId = responseId
                    } else {
                        android.util.Log.d("UserHomeScreen", "Skipping duplicate response")
                    }
                } catch (e: Exception) {
                    android.util.Log.e("UserHomeScreen", "Error formatting response: ${e.message}", e)
                    chatMessages = chatMessages + ChatMessage(
                        text = "⚠️ Error processing response: ${e.message}\n\nPlease try again or request a mechanic.",
                        isUser = false
                    )
                }
                
                // Reset state after a short delay to allow UI to update
                kotlinx.coroutines.delay(200)
                troubleshootViewModel.resetState()
            }
            is TroubleshootUiState.Error -> {
                isSendingMessage = false
                
                val errorMessage = currentState.message
                android.util.Log.e("UserHomeScreen", "Error state: $errorMessage")
                
                chatMessages = chatMessages + ChatMessage(
                    text = "⚠️ $errorMessage\n\nPlease try again or request a mechanic through the app.",
                    isUser = false
                )
                
                // Reset state after a short delay
                kotlinx.coroutines.delay(200)
                troubleshootViewModel.resetState()
            }
            is TroubleshootUiState.Idle -> {
                // Do nothing - keep lastProcessedResponseId to allow new questions
            }
        }
    }

    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "home_animation")
    
    // Floating action button pulse animation
    val fabScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fab_pulse"
    )

    // Background gradient animation
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_animation"
    )

    // Content fade-in animation
    val contentAlpha = remember { Animatable(0f) }
    val contentOffset = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        )
        contentOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2196F3),
                        Color(0xFF1976D2),
                        Color(0xFF0D47A1)
                    )
                )
            )
    ) {
        // Decorative background elements
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f)
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(x = (-100).dp, y = (-100).dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(150.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .offset(x = 300.dp, y = 600.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(125.dp)
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha.value)
                .offset(x = 0.dp, y = contentOffset.value.dp)
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Welcome, $userName!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "How can we help you today?",
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
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // Main Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quick Actions Section
                item {
                    Text(
                        text = "Quick Actions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionCard(
                            title = "Find Mechanic",
                            icon = Icons.Default.Search,
                            color = Color(0xFFFF9800),
                            modifier = Modifier.weight(1f),
                            onClick = onFindMechanicClick
                        )
                        QuickActionCard(
                            title = "My Requests",
                            icon = Icons.Default.List,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f),
                            onClick = onMyRequestsClick
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionCard(
                            title = "Emergency",
                            icon = Icons.Default.Warning,
                            color = Color(0xFFF44336),
                            modifier = Modifier.weight(1f),
                            onClick = onEmergencyClick
                        )
                        QuickActionCard(
                            title = "History",
                            icon = Icons.Default.Info,
                            color = Color(0xFF9C27B0),
                            modifier = Modifier.weight(1f),
                            onClick = onHistoryClick
                        )
                    }
                }

                // AI Assistant Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "AI Assistant",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        Color(0xFF2196F3),
                                                        Color(0xFF1976D2)
                                                    )
                                                ),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Build,
                                            contentDescription = "AI Assistant",
                                            tint = Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "AI Troubleshooting",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF212121)
                                        )
                                        Text(
                                            text = "Get instant help with vehicle issues",
                                            fontSize = 12.sp,
                                            color = Color(0xFF757575)
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { isChatExpanded = !isChatExpanded }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = if (isChatExpanded) "Collapse" else "Expand",
                                        tint = Color(0xFF2196F3),
                                        modifier = Modifier.rotate(if (isChatExpanded) 90f else -90f)
                                    )
                                }
                            }

                            // Chat Messages (when expanded)
                            if (isChatExpanded) {
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Chat messages area
                                if (chatMessages.isEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .background(
                                                color = Color(0xFFF5F5F5),
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = "Chat",
                                                tint = Color(0xFF9E9E9E),
                                                modifier = Modifier.size(48.dp)
                                            )
                                            Text(
                                                text = "Ask me anything about your vehicle",
                                                fontSize = 14.sp,
                                                color = Color(0xFF757575),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                } else {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .background(
                                                color = Color(0xFFF5F5F5),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(chatMessages) { message ->
                                            ChatBubble(message = message)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Chat input
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = chatInput,
                                        onValueChange = { chatInput = it },
                                        modifier = Modifier.weight(1f),
                                        placeholder = {
                                            Text(
                                                text = "Describe your vehicle problem...",
                                                fontSize = 14.sp,
                                                color = Color(0xFF9E9E9E)
                                            )
                                        },
                                        shape = RoundedCornerShape(24.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF2196F3),
                                            unfocusedBorderColor = Color(0xFFE0E0E0),
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White
                                        ),
                                        singleLine = true,
                                        enabled = !isSendingMessage
                                    )
                                    FloatingActionButton(
                                        onClick = {
                                            if (chatInput.isNotBlank() && !isSendingMessage) {
                                                try {
                                                    val userMessage = chatInput.trim()
                                                    if (userMessage.isNotBlank()) {
                                                        android.util.Log.d("UserHomeScreen", "Sending message: $userMessage")
                                                        
                                                        chatInput = ""
                                                        
                                                        // Add user message immediately
                                                        chatMessages = chatMessages + ChatMessage(
                                                            text = userMessage,
                                                            isUser = true
                                                        )
                                                        
                                                        // Call AI troubleshooting API
                                                        android.util.Log.d("UserHomeScreen", "Calling troubleshootViewModel.troubleshoot()")
                                                        troubleshootViewModel.troubleshoot(userMessage)
                                                    }
                                                } catch (e: Exception) {
                                                    android.util.Log.e("UserHomeScreen", "Error in onClick: ${e.message}", e)
                                                    chatMessages = chatMessages + ChatMessage(
                                                        text = "⚠️ An error occurred: ${e.message}. Please try again.",
                                                        isUser = false
                                                    )
                                                    isSendingMessage = false
                                                }
                                            } else {
                                                android.util.Log.d("UserHomeScreen", "Cannot send - Input blank: ${chatInput.isBlank()}, Sending: $isSendingMessage")
                                            }
                                        },
                                        modifier = Modifier.size(48.dp),
                                        containerColor = Color(0xFFFF9800),
                                        contentColor = Color.White
                                    ) {
                                        if (isSendingMessage) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                color = Color.White,
                                                strokeWidth = 2.dp
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.ArrowForward,
                                                contentDescription = "Send",
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// QuickActionCard moved to SharedComposables.kt

/**
 * Format troubleshoot response for display in chat - Enhanced AI Assistant
 */
fun formatTroubleshootResponse(response: com.example.easymechanic.data.model.TroubleshootResponse): String {
    return try {
        val sb = StringBuilder()
        
        // AI Greeting (if available)
        val greeting = response.greeting?.takeIf { it.isNotBlank() }
        if (greeting != null) {
            sb.append("$greeting\n\n")
        }
        
        // Issue Type - personalized header
        val issueType = response.issueType?.takeIf { it.isNotBlank() } ?: "Vehicle Issue"
        sb.append("🔧 I've identified this as: $issueType\n\n")
        
        // Urgency Level (if available)
        val urgency = response.urgencyLevel?.takeIf { it.isNotBlank() }
        if (urgency != null && (urgency == "critical" || urgency == "high")) {
            sb.append("⚠️ Urgency Level: ${urgency.uppercase()}\n\n")
        }
        
        // Analysis (if available)
        val analysis = response.analysis
        if (analysis != null) {
            if (analysis.requiresImmediateAttention) {
                sb.append("🚨 This requires immediate attention!\n\n")
            }
            if (analysis.safetyCritical) {
                sb.append("⚠️ This is a safety-critical issue. Please exercise extreme caution.\n\n")
            }
        }
        
        // Possible Causes
        val causes = response.possibleCauses?.filter { !it.isNullOrBlank() } ?: emptyList()
        if (causes.isNotEmpty()) {
            sb.append("📋 Based on your description, here are the most likely causes:\n\n")
            causes.forEachIndexed { index, cause ->
                sb.append("${index + 1}. $cause\n")
            }
            sb.append("\n")
        }
        
        // Step-by-Step Solution - make it feel conversational
        val solutions = response.stepByStepSolution?.filter { !it.isNullOrBlank() } ?: emptyList()
        if (solutions.isNotEmpty()) {
            sb.append("🔧 Here's what you should do step by step:\n\n")
            solutions.forEach { step ->
                sb.append("$step\n\n")
            }
        } else {
            sb.append("🔧 Solution:\n\n")
            sb.append("I need more details about your problem. Please describe it in more detail, or request a mechanic through the EASY MECHANIC app for professional assistance.\n\n")
        }
        
        // Next Steps (if available)
        val nextSteps = response.nextSteps?.filter { !it.isNullOrBlank() }
        if (!nextSteps.isNullOrEmpty()) {
            sb.append("📌 Next Steps:\n\n")
            nextSteps.forEachIndexed { index, step ->
                sb.append("${index + 1}. $step\n")
            }
            sb.append("\n")
        }
        
        // Safety Note
        val safetyNote = response.safetyNote?.takeIf { it.isNotBlank() }
        if (safetyNote != null) {
            sb.append("⚠️ Important Safety Reminder:\n")
            sb.append(safetyNote)
            sb.append("\n\n")
        }
        
        // Suggested Questions (if available)
        val suggestedQuestions = response.suggestedQuestions?.filter { !it.isNullOrBlank() }
        if (!suggestedQuestions.isNullOrEmpty()) {
            sb.append("💬 You might want to ask:\n")
            suggestedQuestions.take(2).forEach { question ->
                sb.append("• $question\n")
            }
            sb.append("\n")
        }
        
        // Footer - personalized
        sb.append("💡 Remember: If the problem persists or seems risky, don't hesitate to request a mechanic through the EASY MECHANIC app. Your safety is our priority!")
        
        val result = sb.toString()
        android.util.Log.d("formatTroubleshootResponse", "Formatted response - IssueType: $issueType, Solutions: ${solutions.size}, AI Powered: ${response.aiPowered}")
        result
    } catch (e: Exception) {
        android.util.Log.e("formatTroubleshootResponse", "Error: ${e.message}", e)
        "⚠️ I encountered an error processing your request: ${e.message}\n\nPlease try again or request a mechanic."
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) Color(0xFF2196F3) else Color(0xFFE0E0E0)
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                fontSize = 14.sp,
                color = if (message.isUser) Color.White else Color(0xFF212121),
                lineHeight = 20.sp
            )
        }
    }
}

