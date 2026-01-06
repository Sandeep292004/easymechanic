# API Changelog

## Version 1.2.0 - Separate Registration Endpoints

### Changes Made

#### New Endpoints
- **`/auth/user_register.php`** - Dedicated registration endpoint for users (vehicle owners)
  - No longer requires `user_type` parameter
  - Simplified request body: `name`, `email`, `phone`, `password`, `vehicle_type` (optional), `vehicle_number` (optional)
  
- **`/auth/mechanic_register.php`** - Dedicated registration endpoint for mechanics
  - No longer requires `user_type` parameter
  - Simplified request body: `name`, `email`, `phone`, `password`, `specialization` (optional), `experience_years` (optional)

#### Deprecated Endpoint
- **`/auth/register.php`** - Still functional for backward compatibility
  - Now redirects to appropriate endpoint based on `user_type`
  - Will show deprecation message if `user_type` is not provided
  - Recommended to use separate endpoints instead

### Benefits

1. **Clearer API Design**: Separate endpoints make it clear which role is registering
2. **Simplified Requests**: No need to specify `user_type` in registration request
3. **Better Validation**: Each endpoint validates only relevant fields
4. **Easier Android Integration**: Can have separate registration screens/flows for users and mechanics

### Migration Guide

#### Before (Old Way)
```json
POST /auth/register.php
{
  "name": "John Doe",
  "email": "user@example.com",
  "phone": "1234567890",
  "password": "password123",
  "user_type": "user",
  "vehicle_type": "Car",
  "vehicle_number": "ABC123"
}
```

#### After (New Way - Users)
```json
POST /auth/user_register.php
{
  "name": "John Doe",
  "email": "user@example.com",
  "phone": "1234567890",
  "password": "password123",
  "vehicle_type": "Car",
  "vehicle_number": "ABC123"
}
```

#### After (New Way - Mechanics)
```json
POST /auth/mechanic_register.php
{
  "name": "Mechanic Name",
  "email": "mechanic@example.com",
  "phone": "1234567890",
  "password": "password123",
  "specialization": "Engine Repair",
  "experience_years": 5
}
```

### Android Integration

Update your Retrofit interface:

```kotlin
// Old
@POST("auth/register.php")
suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

// New
@POST("auth/user_register.php")
suspend fun userRegister(@Body request: UserRegisterRequest): Response<RegisterResponse>

@POST("auth/mechanic_register.php")
suspend fun mechanicRegister(@Body request: MechanicRegisterRequest): Response<RegisterResponse>
```

### Backward Compatibility

The old `/auth/register.php` endpoint still works but is deprecated. It will:
- Accept requests with `user_type` parameter
- Redirect to appropriate endpoint internally
- Show deprecation message if `user_type` is missing

---

## Version 1.1.0 - Separate Login Endpoints

### Changes Made

#### New Endpoints
- **`/auth/user_login.php`** - Dedicated login endpoint for users (vehicle owners)
  - No longer requires `user_type` parameter
  - Simplified request body: only `email` and `password`
  
- **`/auth/mechanic_login.php`** - Dedicated login endpoint for mechanics
  - No longer requires `user_type` parameter
  - Simplified request body: only `email` and `password`

#### Deprecated Endpoint
- **`/auth/login.php`** - Still functional for backward compatibility
  - Now redirects to appropriate endpoint based on `user_type`
  - Will show deprecation message if `user_type` is not provided
  - Recommended to use separate endpoints instead

### Benefits

1. **Clearer API Design**: Separate endpoints make it clear which role is logging in
2. **Simplified Requests**: No need to specify `user_type` in login request
3. **Better Security**: Each endpoint only queries its respective table
4. **Easier Android Integration**: Can have separate login screens/flows for users and mechanics

### Migration Guide

#### Before (Old Way)
```json
POST /auth/login.php
{
  "email": "user@example.com",
  "password": "password123",
  "user_type": "user"
}
```

#### After (New Way - Users)
```json
POST /auth/user_login.php
{
  "email": "user@example.com",
  "password": "password123"
}
```

#### After (New Way - Mechanics)
```json
POST /auth/mechanic_login.php
{
  "email": "mechanic@example.com",
  "password": "password123"
}
```

### Android Integration

Update your Retrofit interface:

```kotlin
// Old
@POST("auth/login.php")
suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

// New
@POST("auth/user_login.php")
suspend fun userLogin(@Body request: UserLoginRequest): Response<LoginResponse>

@POST("auth/mechanic_login.php")
suspend fun mechanicLogin(@Body request: MechanicLoginRequest): Response<LoginResponse>
```

### Backward Compatibility

The old `/auth/login.php` endpoint still works but is deprecated. It will:
- Accept requests with `user_type` parameter
- Redirect to appropriate endpoint internally
- Show deprecation message if `user_type` is missing

### Updated Documentation

- `README.md` - Updated with new endpoints
- `API_REFERENCE.md` - Added separate login endpoints
- `SETUP.md` - Updated testing examples
- `index.php` - Updated health check endpoint list

---

## Version 1.0.0 - Initial Release

- Complete REST API backend
- JWT authentication
- User and Mechanic registration
- GPS location tracking
- Nearby mechanics search
- Service request management
- Payment processing
- AI troubleshooting

