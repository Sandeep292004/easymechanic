# Retrofit Setup Guide - EASY MECHANIC

## ✅ Retrofit is Already Configured!

This document explains how Retrofit is set up and how to use it in the EASY MECHANIC app.

## 📦 Dependencies

All Retrofit dependencies are already added in `build.gradle.kts`:

```kotlin
// Retrofit for API calls
implementation(libs.retrofit)                    // Retrofit 2.9.0
implementation(libs.retrofit.gson)                // Gson Converter
implementation(libs.gson)                         // Gson 2.10.1
implementation(libs.okhttp)                      // OkHttp 4.12.0
implementation(libs.okhttp.logging)              // Logging Interceptor
```

## 🏗️ Architecture

### 1. API Interface (`EasyMechanicApi.kt`)
Defines all API endpoints using Retrofit annotations:

```kotlin
interface EasyMechanicApi {
    @POST("auth/user_register.php")
    suspend fun userRegister(@Body request: RegisterRequest): ApiResponse<User>
    
    @POST("auth/user_login.php")
    suspend fun userLogin(@Body request: LoginRequest): ApiResponse<User>
}
```

### 2. API Client (`ApiClient.kt`)
Creates and configures the Retrofit instance:

```kotlin
object ApiClient {
    private const val BASE_URL = "http://10.0.2.2/easymechanic/api/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: EasyMechanicApi = retrofit.create(EasyMechanicApi::class.java)
}
```

### 3. Repository (`AuthRepository.kt`)
Handles API calls and error handling:

```kotlin
class AuthRepository {
    private val apiService = ApiClient.apiService
    
    suspend fun registerUser(request: RegisterRequest): Result<User> {
        return try {
            val response = apiService.userRegister(request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 4. ViewModel (`AuthViewModel.kt`)
Manages UI state and calls repository:

```kotlin
class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository()
    
    fun registerUser(...) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.registerUser(request)
            result.onSuccess { user ->
                // Handle success
            }.onFailure { error ->
                // Handle error
            }
        }
    }
}
```

## 🔧 Configuration

### Base URL Configuration

The base URL is set in `ApiClient.kt`:

- **Android Emulator**: `http://10.0.2.2/easymechanic/api/`
  - `10.0.2.2` is a special IP that maps to `localhost` on the host machine

- **Physical Device**: Replace with your computer's IP address
  - Example: `http://192.168.1.100/easymechanic/api/`
  - Find your IP: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)

### To Change Base URL:

1. Open `app/src/main/java/com/example/easymechanic/data/api/ApiClient.kt`
2. Update the `BASE_URL` constant:
   ```kotlin
   private const val BASE_URL = "http://YOUR_IP_ADDRESS/easymechanic/api/"
   ```

## 📝 Data Models

### Request Models
- `RegisterRequest` - User registration data
- `LoginRequest` - Login credentials

### Response Models
- `ApiResponse<T>` - Generic API response wrapper
- `User` - User data model

## 🚀 How It Works

### Registration Flow:
1. User fills registration form
2. `UserRegisterScreen` calls `authViewModel.registerUser()`
3. ViewModel calls `authRepository.registerUser()`
4. Repository calls `apiService.userRegister()` (Retrofit)
5. Retrofit makes HTTP POST request to backend
6. Response is parsed by Gson
7. Result is returned to ViewModel
8. ViewModel updates UI state
9. Navigation happens on success

### Login Flow:
1. User enters credentials
2. `UserLoginScreen` calls `authViewModel.loginUser()`
3. Same flow as registration

## 🔍 Debugging

### HTTP Logging
HTTP logging is enabled by default. You can see all requests/responses in Logcat:

```
D/OkHttp: --> POST http://10.0.2.2/easymechanic/api/auth/user_register.php
D/OkHttp: Content-Type: application/json
D/OkHttp: {"name":"John Doe","email":"john@example.com",...}
D/OkHttp: <-- 200 OK
D/OkHttp: {"success":true,"message":"User registered successfully",...}
```

### Common Issues

1. **Connection Refused**
   - Check if XAMPP Apache is running
   - Verify base URL is correct
   - For physical device, ensure phone and computer are on same network

2. **404 Not Found**
   - Verify API files are in `C:\xampp\htdocs\easymechanic\api\`
   - Check endpoint paths in `EasyMechanicApi.kt`

3. **JSON Parsing Error**
   - Verify backend response matches `ApiResponse` model
   - Check Gson annotations in data models

## ➕ Adding New Endpoints

To add a new API endpoint:

1. **Add to API Interface** (`EasyMechanicApi.kt`):
   ```kotlin
   @POST("requests/create_request.php")
   suspend fun createRequest(@Body request: CreateRequest): ApiResponse<ServiceRequest>
   ```

2. **Create Request/Response Models** (if needed):
   ```kotlin
   data class CreateRequest(
       @SerializedName("issue_description") val issueDescription: String,
       @SerializedName("latitude") val latitude: Double,
       @SerializedName("longitude") val longitude: Double
   )
   ```

3. **Add to Repository**:
   ```kotlin
   suspend fun createRequest(request: CreateRequest): Result<ServiceRequest> {
       return try {
           val response = apiService.createRequest(request)
           if (response.success && response.data != null) {
               Result.success(response.data)
           } else {
               Result.failure(Exception(response.message))
           }
       } catch (e: Exception) {
           Result.failure(e)
       }
   }
   ```

4. **Use in ViewModel**:
   ```kotlin
   fun createRequest(...) {
       viewModelScope.launch {
           val result = repository.createRequest(request)
           // Handle result
       }
   }
   ```

## 🔐 Authentication Headers

For authenticated endpoints, add an interceptor to `ApiClient.kt`:

```kotlin
private val authInterceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("Authorization", "Bearer ${getToken()}")
        .build()
    chain.proceed(request)
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .addInterceptor(loggingInterceptor)
    .build()
```

## ✅ Current Status

- ✅ Retrofit configured
- ✅ Gson converter added
- ✅ OkHttp with logging enabled
- ✅ User registration endpoint connected
- ✅ User login endpoint connected
- ✅ Error handling implemented
- ✅ Loading states managed
- ✅ Data persistence with SharedPreferences

## 📚 Resources

- [Retrofit Documentation](https://square.github.io/retrofit/)
- [OkHttp Documentation](https://square.github.io/okhttp/)
- [Gson Documentation](https://github.com/google/gson)

