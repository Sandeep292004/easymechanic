# EASY MECHANIC REST API

A comprehensive REST API backend for the EASY MECHANIC Android application, built with PHP (procedural) and MySQL.

## Features

- **Dual Role System**: Support for Users (vehicle owners) and Mechanics
- **Authentication**: JWT-based token authentication with hashed passwords
- **GPS Tracking**: Real-time mechanic location updates
- **Nearby Search**: Haversine formula-based distance calculation to find nearby mechanics
- **Service Requests**: Complete request lifecycle (create, accept, complete)
- **Payment Integration**: Dummy payment system ready for Razorpay integration
- **AI Troubleshooting**: Rule-based troubleshooting API (OpenAI-ready)

## Setup Instructions

### Prerequisites

- XAMPP installed and running
- PHP 7.4 or higher
- MySQL 5.7 or higher
- phpMyAdmin access

### Installation Steps

1. **Copy API Files to XAMPP**
   ```
   Copy the entire 'api' folder to: C:\xampp\htdocs\easymechanic\api
   ```

2. **Create Database**
   - Open phpMyAdmin: `http://localhost/phpmyadmin/`
   - Import the SQL file: `api/database/schema.sql`
   - Or manually run the SQL commands in phpMyAdmin

3. **Configure Database Connection**
   - Edit `api/config/config.php`
   - Update database credentials if needed:
     ```php
     define('DB_HOST', 'localhost');
     define('DB_USER', 'root');
     define('DB_PASS', '');
     define('DB_NAME', 'easymechanic');
     ```

4. **Configure JWT Secret**
   - Edit `api/config/config.php`
   - Change `JWT_SECRET` to a strong random string in production

5. **Test API**
   - Start Apache and MySQL in XAMPP
   - Test endpoints using Postman or curl

## API Base URL

```
http://localhost/easymechanic/api/
```

## API Endpoints

### Authentication

#### 1. Register User (Vehicle Owner)
- **URL**: `POST /auth/user_register.php`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "password": "password123",
    "vehicle_type": "Car",
    "vehicle_number": "ABC123"
  }
  ```
- **Response**:
  ```json
  {
    "success": true,
    "message": "User registered successfully",
    "data": {
      "user_id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "phone": "1234567890",
      "vehicle_type": "Car",
      "vehicle_number": "ABC123",
      "user_type": "user",
      "token": "eyJ0eXAiOiJKV1QiLCJhbGc..."
    }
  }
  ```

#### 2. Register Mechanic
- **URL**: `POST /auth/mechanic_register.php`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "name": "Mechanic Name",
    "email": "mechanic@example.com",
    "phone": "1234567890",
    "password": "password123",
    "specialization": "Engine Repair",
    "experience_years": 5
  }
  ```
- **Response**:
  ```json
  {
    "success": true,
    "message": "Mechanic registered successfully",
    "data": {
      "mechanic_id": 1,
      "name": "Mechanic Name",
      "email": "mechanic@example.com",
      "phone": "1234567890",
      "specialization": "Engine Repair",
      "experience_years": 5,
      "rating": 0.00,
      "is_available": true,
      "user_type": "mechanic",
      "token": "eyJ0eXAiOiJKV1QiLCJhbGc..."
    }
  }
  ```

**Note**: The old `/auth/register.php` endpoint is deprecated but still works for backward compatibility. It requires `user_type` parameter and redirects to the appropriate endpoint.

#### 3. User Login
- **URL**: `POST /auth/user_login.php`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "email": "john@example.com",
    "password": "password123"
  }
  ```
- **Response**: Returns user data and JWT token

#### 4. Mechanic Login
- **URL**: `POST /auth/mechanic_login.php`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "email": "mechanic@example.com",
    "password": "password123"
  }
  ```
- **Response**: Returns mechanic data and JWT token

#### 5. Logout
- **URL**: `POST /auth/logout.php`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: Success message

**Note**: The old `/auth/login.php` endpoint is deprecated but still works for backward compatibility. It requires `user_type` parameter and redirects to the appropriate endpoint.

### Mechanic Endpoints

#### 6. Update Location
- **URL**: `POST /mechanic/update_location.php`
- **Headers**: 
  - `Authorization: Bearer {token}`
  - `Content-Type: application/json`
- **Body**:
  ```json
  {
    "latitude": 28.6139,
    "longitude": 77.2090
  }
  ```
- **Response**: Updated location data

#### 7. Find Nearby Mechanics
- **URL**: `GET /mechanic/find_mechanics.php?latitude=28.6139&longitude=77.2090&radius=10`
- **Query Parameters**:
  - `latitude` (required): User's latitude
  - `longitude` (required): User's longitude
  - `radius` (optional): Search radius in kilometers (default: 10)
- **Response**: List of nearby mechanics with distance

#### 8. Toggle Availability
- **URL**: `POST /mechanic/toggle_availability.php`
- **Headers**: 
  - `Authorization: Bearer {token}`
  - `Content-Type: application/json`
- **Body**:
  ```json
  {
    "is_available": true
  }
  ```

### Service Requests

#### 9. Create Service Request
- **URL**: `POST /requests/create_request.php`
- **Headers**: 
  - `Authorization: Bearer {token}`
  - `Content-Type: application/json`
- **Body**:
  ```json
  {
    "issue_description": "Engine not starting",
    "latitude": 28.6139,
    "longitude": 77.2090,
    "address": "123 Main Street, City"
  }
  ```
- **Response**: Created request details

#### 10. Accept Service Request
- **URL**: `POST /requests/accept_request.php`
- **Headers**: 
  - `Authorization: Bearer {token}`
  - `Content-Type: application/json`
- **Body**:
  ```json
  {
    "request_id": 1
  }
  ```
- **Response**: Updated request with mechanic assignment

#### 11. Complete Service Request
- **URL**: `POST /requests/complete_request.php`
- **Headers**: 
  - `Authorization: Bearer {token}`
  - `Content-Type: application/json`
- **Body**:
  ```json
  {
    "request_id": 1
  }
  ```
- **Response**: Completed request details

#### 12. Get Service Requests
- **URL**: `GET /requests/get_requests.php?type=user&status=pending`
- **Headers**: `Authorization: Bearer {token}`
- **Query Parameters**:
  - `type` (optional): "user" or "mechanic"
  - `status` (optional): Filter by status
- **Response**: List of service requests

### Payment

#### 13. Process Payment
- **URL**: `POST /payment/process_payment.php`
- **Headers**: 
  - `Authorization: Bearer {token}`
  - `Content-Type: application/json`
- **Body**:
  ```json
  {
    "service_request_id": 1,
    "amount": 500.00,
    "payment_method": "cash",
    "razorpay_order_id": "order_xxx",
    "razorpay_payment_id": "pay_xxx"
  }
  ```
- **Response**: Payment transaction details

#### 14. Get Payment History
- **URL**: `GET /payment/get_payments.php?service_request_id=1`
- **Headers**: `Authorization: Bearer {token}`
- **Query Parameters**:
  - `service_request_id` (optional): Filter by request
- **Response**: Payment history

### AI Troubleshooting

#### 15. Get Troubleshooting Steps
- **URL**: `POST /ai/troubleshoot.php`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "problem_description": "My car engine won't start"
  }
  ```
- **Response**: Step-by-step troubleshooting solutions

## Request Status Values

- `pending`: Request created, waiting for mechanic
- `accepted`: Mechanic has accepted the request
- `in_progress`: Work in progress
- `completed`: Service completed
- `cancelled`: Request cancelled

## Payment Status Values

- `pending`: Payment initiated
- `success`: Payment successful
- `failed`: Payment failed
- `refunded`: Payment refunded

## Android Retrofit Integration

### Example Retrofit Interface

```kotlin
interface EasyMechanicAPI {
    @POST("auth/user_register.php")
    suspend fun userRegister(@Body request: UserRegisterRequest): Response<RegisterResponse>
    
    @POST("auth/mechanic_register.php")
    suspend fun mechanicRegister(@Body request: MechanicRegisterRequest): Response<RegisterResponse>
    
    @POST("auth/user_login.php")
    suspend fun userLogin(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("auth/mechanic_login.php")
    suspend fun mechanicLogin(@Body request: LoginRequest): Response<LoginResponse>
    
    @GET("mechanic/find_mechanics.php")
    suspend fun findMechanics(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double,
        @Query("radius") radius: Double = 10.0
    ): Response<MechanicsResponse>
    
    @POST("requests/create_request.php")
    @Headers("Authorization: Bearer {token}")
    suspend fun createRequest(@Body request: ServiceRequest): Response<RequestResponse>
}
```

### Example Retrofit Client

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("http://localhost/easymechanic/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

## Postman Testing

1. **Import Collection**: Create a new collection in Postman
2. **Set Base URL**: `http://localhost/easymechanic/api/`
3. **Add Headers**: 
   - `Content-Type: application/json`
   - `Authorization: Bearer {token}` (for protected endpoints)
4. **Test Endpoints**: Use the provided examples above

## Database Schema

### Tables

- `users`: Vehicle owners
- `mechanics`: Service providers
- `mechanic_locations`: GPS coordinates for mechanics
- `service_requests`: Service request records
- `payments`: Payment transactions
- `user_tokens`: JWT token management

## Security Notes

1. **JWT Secret**: Change `JWT_SECRET` in production
2. **Password Hashing**: Uses PHP `password_hash()` with bcrypt
3. **SQL Injection**: All queries use prepared statements
4. **CORS**: Configured for Android and Postman access
5. **Input Validation**: All endpoints validate input data

## Extending the API

### Razorpay Integration

1. Install Razorpay PHP SDK:
   ```bash
   composer require razorpay/razorpay
   ```

2. Update `api/payment/process_payment.php`:
   - Add Razorpay verification logic
   - Use Razorpay API keys from config

### OpenAI Integration

1. Get OpenAI API key
2. Uncomment OpenAI function in `api/ai/troubleshoot.php`
3. Add API key to config
4. Replace rule-based logic with OpenAI calls

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check XAMPP MySQL is running
   - Verify database credentials in `config.php`
   - Ensure database exists

2. **CORS Errors**
   - Check `headers.php` is included
   - Verify Apache is running

3. **Token Invalid**
   - Check token is sent in Authorization header
   - Verify token hasn't expired (24 hours)
   - Ensure JWT_SECRET matches

4. **404 Not Found**
   - Verify files are in correct directory
   - Check Apache document root
   - Ensure `.htaccess` allows PHP execution

## File Structure

```
api/
├── config/
│   ├── config.php          # Configuration
│   ├── db.php              # Database connection
│   ├── headers.php         # CORS and headers
│   └── jwt.php             # JWT token management
├── auth/
│   ├── register.php        # User/Mechanic registration
│   ├── login.php           # User/Mechanic login
│   └── logout.php          # Logout
├── mechanic/
│   ├── update_location.php # Update GPS location
│   ├── find_mechanics.php  # Find nearby mechanics
│   └── toggle_availability.php # Toggle availability
├── requests/
│   ├── create_request.php  # Create service request
│   ├── accept_request.php  # Accept request (mechanic)
│   ├── complete_request.php # Complete request
│   └── get_requests.php    # Get requests list
├── payment/
│   ├── process_payment.php # Process payment
│   └── get_payments.php    # Get payment history
├── ai/
│   └── troubleshoot.php    # AI troubleshooting
├── database/
│   └── schema.sql          # Database schema
└── README.md               # This file
```

## License

This project is part of the EASY MECHANIC application.

## Support

For issues or questions, please refer to the project documentation or contact the development team.

