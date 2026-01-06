package com.example.easymechanic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.easymechanic.ui.screens.*
import com.example.easymechanic.ui.viewmodel.AuthViewModel
import com.example.easymechanic.utils.PreferencesManager

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object RoleSelection : Screen("role_selection")
    
    // User Screens
    object UserLogin : Screen("user_login")
    object UserRegister : Screen("user_register")
    object UserHome : Screen("user_home")
    object FindMechanics : Screen("find_mechanics")
    object CreateRequest : Screen("create_request")
    object MyRequests : Screen("my_requests")
    object RequestDetails : Screen("request_details/{requestId}") {
        fun createRoute(requestId: Int) = "request_details/$requestId"
    }
    object Profile : Screen("profile")
    object Payment : Screen("payment/{requestId}/{amount}") {
        fun createRoute(requestId: Int, amount: Double) = "payment/$requestId/$amount"
    }
    object PaymentHistory : Screen("payment_history")
    object History : Screen("history")
    object Settings : Screen("settings")
    
    // Mechanic Screens
    object MechanicLogin : Screen("mechanic_login")
    object MechanicRegister : Screen("mechanic_register")
    object MechanicHome : Screen("mechanic_home")
    object MechanicRequests : Screen("mechanic_requests")
    object MechanicRequestDetails : Screen("mechanic_request_details/{requestId}") {
        fun createRoute(requestId: Int) = "mechanic_request_details/$requestId"
    }
    object MechanicProfile : Screen("mechanic_profile")
    object MechanicSettings : Screen("mechanic_settings")
    object UpdateLocation : Screen("update_location")
    object MechanicHistory : Screen("mechanic_history")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val authState by authViewModel.uiState.collectAsState()
    
    // Handle auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is com.example.easymechanic.ui.viewmodel.AuthUiState.Success -> {
                val userType = preferencesManager.getUserType()
                val destination = if (userType == "mechanic") {
                    Screen.MechanicHome.route
                } else {
                    Screen.UserHome.route
                }
                navController.navigate(destination) {
                    popUpTo(Screen.RoleSelection.route) {
                        inclusive = true
                    }
                }
                authViewModel.resetState()
            }
            else -> {}
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onGetStartedClick = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onUserClick = {
                    navController.navigate(Screen.UserLogin.route)
                },
                onMechanicClick = {
                    navController.navigate(Screen.MechanicLogin.route)
                }
            )
        }
        
        composable(Screen.UserLogin.route) {
            UserLoginScreen(
                authViewModel = authViewModel,
                authState = authState,
                onLoginClick = { email, password ->
                    authViewModel.loginUser(email, password)
                },
                onRegisterClick = {
                    navController.navigate(Screen.UserRegister.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.UserRegister.route) {
            UserRegisterScreen(
                authViewModel = authViewModel,
                authState = authState,
                onRegisterClick = { name, email, phone, password, vehicleType, vehicleNumber ->
                    authViewModel.registerUser(name, email, phone, password, vehicleType, vehicleNumber)
                },
                onLoginClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.UserHome.route) {
            UserHomeScreen(
                userName = preferencesManager.getUserName() ?: "User",
                onFindMechanicClick = {
                    navController.navigate(Screen.FindMechanics.route)
                },
                onMyRequestsClick = {
                    navController.navigate(Screen.MyRequests.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                },
                onEmergencyClick = {
                    navController.navigate(Screen.CreateRequest.route)
                },
                        onLogoutClick = {
                            preferencesManager.clearUserData()
                            navController.navigate(Screen.RoleSelection.route) {
                                popUpTo(Screen.UserHome.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onSettingsClick = {
                            navController.navigate(Screen.Settings.route)
                        }
                    )
                }
        
        composable(Screen.FindMechanics.route) {
            FindMechanicsScreen(
                onBackClick = { navController.popBackStack() },
                onMechanicClick = { mechanicId ->
                    // Navigate to create request with selected mechanic
                    navController.navigate(Screen.CreateRequest.route)
                }
            )
        }
        
                composable(Screen.CreateRequest.route) {
                    CreateRequestScreen(
                        onBackClick = { navController.popBackStack() },
                        onSubmitClick = { issue, vehicleType, vehicleNumber, priority, latitude, longitude, address ->
                            // TODO: Create request via API with location
                            android.util.Log.d("Navigation", "Creating request with location: $latitude, $longitude")
                            navController.navigate(Screen.MyRequests.route) {
                                popUpTo(Screen.UserHome.route)
                            }
                        }
                    )
                }
        
        composable(Screen.MyRequests.route) {
            MyRequestsScreen(
                onBackClick = { navController.popBackStack() },
                onRequestClick = { requestId ->
                    navController.navigate(Screen.RequestDetails.createRoute(requestId))
                }
            )
        }
        
        composable(
            route = Screen.RequestDetails.route,
            arguments = listOf(navArgument("requestId") { type = NavType.IntType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getInt("requestId") ?: 0
            RequestDetailsScreen(
                requestId = requestId,
                onBackClick = { navController.popBackStack() },
                onPayClick = {
                    // TODO: Get actual cost from request
                    navController.navigate(Screen.Payment.createRoute(requestId, 500.0))
                }
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                preferencesManager = preferencesManager,
                onBackClick = { navController.popBackStack() },
                onEditClick = {
                    // TODO: Navigate to edit profile screen
                },
                onLogoutClick = {
                    preferencesManager.clearUserData()
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.UserHome.route) {
                            inclusive = true
                        }
                    }
                },
                onPaymentHistoryClick = {
                    navController.navigate(Screen.PaymentHistory.route)
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                }
            )
        }
        
                composable(
                    route = Screen.Payment.route,
                    arguments = listOf(
                        navArgument("requestId") { type = NavType.IntType },
                        navArgument("amount") { type = NavType.FloatType }
                    )
                ) { backStackEntry ->
                    val requestId = backStackEntry.arguments?.getInt("requestId") ?: 0
                    val amount = backStackEntry.arguments?.getFloat("amount")?.toDouble() ?: 0.0
                    // TODO: Get mechanic UPI details from request/mechanic data
                    PaymentScreen(
                        requestId = requestId,
                        amount = amount,
                        mechanicUpiId = "mechanic@paytm", // TODO: Get from mechanic data
                        mechanicUpiQrCode = null, // TODO: Get from mechanic data
                        onBackClick = { navController.popBackStack() },
                        onPaymentSuccess = {
                            navController.navigate(Screen.PaymentHistory.route) {
                                popUpTo(Screen.UserHome.route)
                            }
                        }
                    )
                }
        
        composable(Screen.PaymentHistory.route) {
            PaymentHistoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.History.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() },
                onRequestClick = { requestId ->
                    navController.navigate(Screen.RequestDetails.createRoute(requestId))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                preferencesManager = preferencesManager,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { settingsData ->
                    // Settings saved - navigate back
                    navController.popBackStack()
                }
            )
        }
        
        // Mechanic Screens
        composable(Screen.MechanicLogin.route) {
            MechanicLoginScreen(
                authViewModel = authViewModel,
                authState = authState,
                onLoginClick = { email, password ->
                    authViewModel.loginMechanic(email, password)
                },
                onRegisterClick = {
                    navController.navigate(Screen.MechanicRegister.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.MechanicRegister.route) {
            MechanicRegisterScreen(
                authViewModel = authViewModel,
                authState = authState,
                onRegisterClick = { name, email, phone, password, specialization, experienceYears ->
                    authViewModel.registerMechanic(name, email, phone, password, specialization, experienceYears)
                },
                onLoginClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.MechanicHome.route) {
            MechanicHomeScreen(
                mechanicName = preferencesManager.getUserName() ?: "Mechanic",
                onRequestsClick = {
                    navController.navigate(Screen.MechanicRequests.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.MechanicProfile.route)
                },
                onHistoryClick = {
                    navController.navigate(Screen.MechanicHistory.route)
                },
                onUpdateLocationClick = {
                    navController.navigate(Screen.UpdateLocation.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.MechanicSettings.route)
                },
                onLogoutClick = {
                    preferencesManager.clearUserData()
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.MechanicHome.route) {
                            inclusive = true
                        }
                    }
                },
                isAvailable = preferencesManager.isAvailable(),
                onToggleAvailability = { isAvailable ->
                    // TODO: Call API to toggle availability
                }
            )
        }
        
        composable(Screen.MechanicRequests.route) {
            MechanicRequestsScreen(
                onBackClick = { navController.popBackStack() },
                onRequestClick = { requestId ->
                    navController.navigate(Screen.MechanicRequestDetails.createRoute(requestId))
                }
            )
        }
        
        composable(
            route = Screen.MechanicRequestDetails.route,
            arguments = listOf(navArgument("requestId") { type = NavType.IntType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getInt("requestId") ?: 0
            // TODO: Get actual request data from API/ViewModel
            MechanicRequestDetailsScreen(
                requestId = requestId,
                userLatitude = 28.6139, // TODO: Get from request data
                userLongitude = 77.2090, // TODO: Get from request data
                userPhone = "+91 9876543210", // TODO: Get from request data
                userName = "John Doe", // TODO: Get from request data
                userEmail = "john@example.com", // TODO: Get from request data
                issueDescription = "Engine not starting", // TODO: Get from request data
                vehicleType = "Sedan", // TODO: Get from request data
                vehicleNumber = "MH-12-AB-1234", // TODO: Get from request data
                address = "123 Main St, City", // TODO: Get from request data
                onBackClick = { navController.popBackStack() },
                onAcceptClick = {
                    // TODO: Accept request via API
                    navController.popBackStack()
                },
                onCompleteClick = {
                    // TODO: Complete request via API
                    navController.popBackStack()
                },
                onCallClick = { phone ->
                    // Call functionality handled in screen
                    android.util.Log.d("Navigation", "Calling: $phone")
                }
            )
        }
        
        composable(Screen.MechanicProfile.route) {
            MechanicProfileScreen(
                preferencesManager = preferencesManager,
                onBackClick = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate(Screen.MechanicSettings.route)
                },
                onLogoutClick = {
                    preferencesManager.clearUserData()
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.MechanicHome.route) {
                            inclusive = true
                        }
                    }
                },
                onHistoryClick = {
                    navController.navigate(Screen.MechanicHistory.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.MechanicSettings.route)
                }
            )
        }
        
        composable(Screen.MechanicSettings.route) {
            MechanicSettingsScreen(
                preferencesManager = preferencesManager,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { settingsData ->
                    // TODO: Save settings via API
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.UpdateLocation.route) {
            UpdateLocationScreen(
                onBackClick = { navController.popBackStack() },
                onUpdateClick = { lat: Double, lon: Double ->
                    // TODO: Update location via API
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.MechanicHistory.route) {
            MechanicHistoryScreen(
                onBackClick = { navController.popBackStack() },
                onRequestClick = { requestId ->
                    navController.navigate(Screen.MechanicRequestDetails.createRoute(requestId))
                }
            )
        }
    }
}

