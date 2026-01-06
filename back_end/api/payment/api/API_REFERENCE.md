# EASY MECHANIC API - Quick Reference

## Base URL
```
http://localhost/easymechanic/api/
```

## Authentication Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/auth/user_register.php` | No | Register as user (vehicle owner) |
| POST | `/auth/mechanic_register.php` | No | Register as mechanic |
| POST | `/auth/user_login.php` | No | Login as user (vehicle owner) |
| POST | `/auth/mechanic_login.php` | No | Login as mechanic |
| POST | `/auth/logout.php` | Yes | Logout and invalidate token |

## Mechanic Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/mechanic/update_location.php` | Yes (Mechanic) | Update GPS location |
| GET | `/mechanic/find_mechanics.php` | No | Find nearby mechanics |
| POST | `/mechanic/toggle_availability.php` | Yes (Mechanic) | Toggle availability status |

## Service Request Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/requests/create_request.php` | Yes (User) | Create service request |
| POST | `/requests/accept_request.php` | Yes (Mechanic) | Accept service request |
| POST | `/requests/complete_request.php` | Yes (Mechanic) | Complete service request |
| GET | `/requests/get_requests.php` | Yes | Get service requests |

## Payment Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/payment/process_payment.php` | Yes (User) | Process payment |
| GET | `/payment/get_payments.php` | Yes | Get payment history |

## AI Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/ai/troubleshoot.php` | No | Get troubleshooting steps |

## Status Endpoint

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| GET | `/index.php` | No | API health check |

## Request/Response Examples

### Register User
```http
POST /auth/user_register.php
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

### Register Mechanic
```http
POST /auth/mechanic_register.php
Content-Type: application/json

{
  "name": "Mechanic Name",
  "email": "mechanic@example.com",
  "phone": "1234567890",
  "password": "password123",
  "specialization": "Engine Repair",
  "experience_years": 5
}
```

### User Login
```http
POST /auth/user_login.php
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

### Mechanic Login
```http
POST /auth/mechanic_login.php
Content-Type: application/json

{
  "email": "mechanic@example.com",
  "password": "password123"
}
```

### Find Nearby Mechanics
```http
GET /mechanic/find_mechanics.php?latitude=28.6139&longitude=77.2090&radius=10
```

### Create Service Request
```http
POST /requests/create_request.php
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGc...
Content-Type: application/json

{
  "issue_description": "Engine not starting",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "address": "123 Main Street"
}
```

### Process Payment
```http
POST /payment/process_payment.php
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGc...
Content-Type: application/json

{
  "service_request_id": 1,
  "amount": 500.00,
  "payment_method": "cash"
}
```

## Response Format

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error description",
  "error": "Detailed error message (optional)"
}
```

## HTTP Status Codes

- `200` - Success
- `400` - Bad Request (validation error)
- `401` - Unauthorized (invalid/missing token)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found
- `405` - Method Not Allowed
- `409` - Conflict (e.g., email already exists)
- `500` - Internal Server Error
- `503` - Service Unavailable

## Authentication

All protected endpoints require:
```
Authorization: Bearer {token}
```

Token is obtained from `/auth/login.php` or `/auth/register.php`

Token expires after 24 hours.

## Notes

- All coordinates use decimal degrees (latitude, longitude)
- Distance calculations use Haversine formula
- Default search radius: 10 km
- All timestamps in server timezone (Asia/Kolkata)
- Payment system currently uses dummy logic (ready for Razorpay)

