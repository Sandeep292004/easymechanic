# EASY MECHANIC API - Postman Testing Guide

## 📥 Import Postman Collection

1. **Download the collection**: `POSTMAN_COLLECTION.json` from the API folder
2. **Open Postman**
3. Click **"Import"** button (top left)
4. Select **"Upload Files"**
5. Choose `POSTMAN_COLLECTION.json`
6. Click **"Import"**

## 🔧 Setup Variables

After importing, set up environment variables:

1. Click on **"EASY MECHANIC API"** collection
2. Go to **"Variables"** tab
3. Set the following:
   - `base_url`: `http://localhost/easymechanic/api`
   - `token`: (leave empty, will be set after login)
   - `user_token`: (leave empty, will be set after user login)
   - `mechanic_token`: (leave empty, will be set after mechanic login)

## 🧪 Testing Flow

### Step 1: Health Check
**GET** `http://localhost/easymechanic/api/index.php`

**Expected Response:**
```json
{
  "success": true,
  "message": "EASY MECHANIC API is running",
  "status": "healthy",
  "database": "connected"
}
```

---

### Step 2: Register User

**POST** `http://localhost/easymechanic/api/auth/user_register.php`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
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

**Expected Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "user_id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "token": "eyJ0eXAiOiJKV1QiLCJhbGc..."
  }
}
```

**💡 Copy the `token` from response and save it as `user_token` variable**

---

### Step 3: Register Mechanic

**POST** `http://localhost/easymechanic/api/auth/mechanic_register.php`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "name": "Mechanic Name",
  "email": "mechanic@example.com",
  "phone": "9876543210",
  "password": "password123",
  "specialization": "Engine Repair",
  "experience_years": 5
}
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Mechanic registered successfully",
  "data": {
    "mechanic_id": 1,
    "name": "Mechanic Name",
    "token": "eyJ0eXAiOiJKV1QiLCJhbGc..."
  }
}
```

**💡 Copy the `token` from response and save it as `mechanic_token` variable**

---

### Step 4: User Login

**POST** `http://localhost/easymechanic/api/auth/user_login.php`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "user_id": 1,
    "name": "John Doe",
    "token": "eyJ0eXAiOiJKV1QiLCJhbGc..."
  }
}
```

---

### Step 5: Mechanic Login

**POST** `http://localhost/easymechanic/api/auth/mechanic_login.php`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "email": "mechanic@example.com",
  "password": "password123"
}
```

---

### Step 6: Update Mechanic Location

**POST** `http://localhost/easymechanic/api/mechanic/update_location.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{mechanic_token}}
```

**Body (raw JSON):**
```json
{
  "latitude": 28.6139,
  "longitude": 77.2090
}
```

---

### Step 7: Find Nearby Mechanics

**GET** `http://localhost/easymechanic/api/mechanic/find_mechanics.php?latitude=28.6139&longitude=77.2090&radius=10`

**Headers:** (None required)

**Query Parameters:**
- `latitude`: 28.6139
- `longitude`: 77.2090
- `radius`: 10 (optional, default: 10 km)

---

### Step 8: Create Service Request

**POST** `http://localhost/easymechanic/api/requests/create_request.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{user_token}}
```

**Body (raw JSON):**
```json
{
  "issue_description": "Engine not starting, makes clicking sound when turning key",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "address": "123 Main Street, New Delhi"
}
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Service request created successfully",
  "data": {
    "request_id": 1,
    "status": "pending",
    "created_at": "2024-01-01 12:00:00"
  }
}
```

**💡 Note the `request_id` for next steps**

---

### Step 9: Accept Service Request (Mechanic)

**POST** `http://localhost/easymechanic/api/requests/accept_request.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{mechanic_token}}
```

**Body (raw JSON):**
```json
{
  "request_id": 1
}
```

---

### Step 10: Complete Service Request (Mechanic)

**POST** `http://localhost/easymechanic/api/requests/complete_request.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{mechanic_token}}
```

**Body (raw JSON):**
```json
{
  "request_id": 1
}
```

---

### Step 11: Get Service Requests

**GET** `http://localhost/easymechanic/api/requests/get_requests.php?type=user&status=pending`

**Headers:**
```
Authorization: Bearer {{user_token}}
```

**Query Parameters:**
- `type`: user or mechanic (optional)
- `status`: pending, accepted, in_progress, completed, cancelled (optional)

---

### Step 12: Process Payment

**POST** `http://localhost/easymechanic/api/payment/process_payment.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{user_token}}
```

**Body (raw JSON):**
```json
{
  "service_request_id": 1,
  "amount": 500.00,
  "payment_method": "cash"
}
```

**For Razorpay:**
```json
{
  "service_request_id": 1,
  "amount": 500.00,
  "payment_method": "razorpay",
  "razorpay_order_id": "order_xxx",
  "razorpay_payment_id": "pay_xxx"
}
```

---

### Step 13: Get Payment History

**GET** `http://localhost/easymechanic/api/payment/get_payments.php`

**Headers:**
```
Authorization: Bearer {{user_token}}
```

**Query Parameters:**
- `service_request_id`: (optional) Filter by request ID

---

### Step 14: AI Troubleshooting

**POST** `http://localhost/easymechanic/api/ai/troubleshoot.php`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "problem_description": "My car engine won't start"
}
```

**Other examples:**
```json
{
  "problem_description": "Battery is dead"
}
```

```json
{
  "problem_description": "Car is overheating"
}
```

```json
{
  "problem_description": "Brakes are making squeaking noise"
}
```

---

## 📋 Complete Request Examples

### User Registration
```http
POST http://localhost/easymechanic/api/auth/user_register.php
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "password": "password123",
  "vehicle_type": "Car",
  "vehicle_number": "ABC123"
}
```

### Mechanic Registration
```http
POST http://localhost/easymechanic/api/auth/mechanic_register.php
Content-Type: application/json

{
  "name": "Mechanic Name",
  "email": "mechanic@example.com",
  "phone": "9876543210",
  "password": "password123",
  "specialization": "Engine Repair",
  "experience_years": 5
}
```

### User Login
```http
POST http://localhost/easymechanic/api/auth/user_login.php
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

### Create Service Request
```http
POST http://localhost/easymechanic/api/requests/create_request.php
Content-Type: application/json
Authorization: Bearer YOUR_TOKEN_HERE

{
  "issue_description": "Engine not starting",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "address": "123 Main Street"
}
```

### Find Nearby Mechanics
```http
GET http://localhost/easymechanic/api/mechanic/find_mechanics.php?latitude=28.6139&longitude=77.2090&radius=10
```

## 🔑 Setting Tokens in Postman

After login, copy the token from response and:

1. Click on collection **"EASY MECHANIC API"**
2. Go to **"Variables"** tab
3. Update:
   - `user_token` = token from user login
   - `mechanic_token` = token from mechanic login
4. All requests will automatically use these tokens

## ✅ Expected Status Codes

- `200` - Success
- `400` - Bad Request (validation error)
- `401` - Unauthorized (invalid/missing token)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found
- `409` - Conflict (email already exists)
- `500` - Internal Server Error

## 🐛 Troubleshooting

### Issue: "Database connection failed"
- Check MySQL is running in XAMPP
- Verify database `easymechanic` exists
- Check credentials in `config/config.php`

### Issue: "Invalid or expired token"
- Login again to get new token
- Update token in Postman variables
- Check token hasn't expired (24 hours)

### Issue: "CORS error"
- CORS is configured for API access
- This is normal in Postman, ignore CORS warnings
- For browser testing, use Postman or Android app

## 📚 Additional Resources

- **Full Documentation**: See `README.md`
- **API Reference**: See `API_REFERENCE.md`
- **Setup Guide**: See `SETUP.md`

---

**Happy Testing!** 🚀

