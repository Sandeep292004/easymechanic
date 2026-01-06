# EASY MECHANIC API - Project Summary

## Overview

A complete REST API backend for the EASY MECHANIC Android application, built with PHP (procedural) and MySQL. The API supports dual roles (Users and Mechanics), real-time GPS tracking, service request management, payment processing, and AI-powered troubleshooting.

## What Has Been Built

### ✅ Core Infrastructure
- **Database Schema**: Complete MySQL database with 6 tables
- **Configuration System**: Centralized config, database connection, headers, and JWT management
- **Security**: JWT token-based authentication, password hashing, SQL injection protection
- **CORS Support**: Configured for Android Retrofit and Postman

### ✅ Authentication System
- User and Mechanic registration
- Login with JWT token generation
- Logout with token invalidation
- Token validation middleware

### ✅ Mechanic Features
- Real-time GPS location updates
- Nearby mechanics search using Haversine formula
- Availability toggle
- Distance-based filtering within radius

### ✅ Service Request System
- Create service requests (Users)
- Accept requests (Mechanics)
- Complete requests (Mechanics)
- View requests with filtering
- Status tracking (pending → accepted → in_progress → completed)

### ✅ Payment Module
- Process payments (dummy/success logic)
- Payment history retrieval
- Razorpay-ready structure
- Transaction ID generation
- Multiple payment methods support

### ✅ AI Troubleshooting
- Rule-based troubleshooting engine
- Step-by-step solutions
- OpenAI integration ready
- Covers common vehicle issues (engine, battery, brakes, tires, etc.)

## File Structure

```
api/
├── config/              # Core configuration files
│   ├── config.php      # Main configuration
│   ├── db.php          # Database connection
│   ├── headers.php     # CORS and headers
│   └── jwt.php         # JWT token management
│
├── auth/               # Authentication endpoints
│   ├── register.php
│   ├── login.php
│   └── logout.php
│
├── mechanic/           # Mechanic-specific endpoints
│   ├── update_location.php
│   ├── find_mechanics.php
│   └── toggle_availability.php
│
├── requests/           # Service request endpoints
│   ├── create_request.php
│   ├── accept_request.php
│   ├── complete_request.php
│   └── get_requests.php
│
├── payment/            # Payment endpoints
│   ├── process_payment.php
│   └── get_payments.php
│
├── ai/                 # AI troubleshooting
│   └── troubleshoot.php
│
├── database/           # Database schema
│   └── schema.sql
│
├── index.php          # Health check endpoint
├── .htaccess          # Apache configuration
├── README.md          # Full documentation
├── SETUP.md           # Setup instructions
├── API_REFERENCE.md   # Quick reference
└── PROJECT_SUMMARY.md # This file
```

## Database Tables

1. **users** - Vehicle owners
2. **mechanics** - Service providers
3. **mechanic_locations** - GPS coordinates
4. **service_requests** - Service request records
5. **payments** - Payment transactions
6. **user_tokens** - JWT token management

## API Endpoints Summary

### Authentication (3 endpoints)
- Register, Login, Logout

### Mechanic (3 endpoints)
- Update location, Find nearby, Toggle availability

### Service Requests (4 endpoints)
- Create, Accept, Complete, Get requests

### Payment (2 endpoints)
- Process payment, Get payment history

### AI (1 endpoint)
- Troubleshooting

### Status (1 endpoint)
- Health check

**Total: 14 API endpoints**

## Key Features

### 🔐 Security
- JWT token authentication
- Password hashing (bcrypt)
- Prepared statements (SQL injection protection)
- Token expiration (24 hours)
- Role-based access control

### 📍 Location Services
- Haversine formula for distance calculation
- Real-time GPS tracking
- Radius-based search
- Location history

### 💳 Payment System
- Dummy payment logic (always succeeds)
- Ready for Razorpay integration
- Transaction tracking
- Payment history

### 🤖 AI Troubleshooting
- Rule-based engine
- Multiple problem categories
- Step-by-step solutions
- OpenAI-ready structure

## Setup Requirements

1. **XAMPP** with Apache and MySQL
2. **PHP 7.4+** (included in XAMPP)
3. **MySQL 5.7+** (included in XAMPP)
4. **phpMyAdmin** (included in XAMPP)

## Installation Steps

1. Copy `api` folder to `C:\xampp\htdocs\easymechanic\`
2. Start Apache and MySQL in XAMPP
3. Import `database/schema.sql` in phpMyAdmin
4. Test: http://localhost/easymechanic/api/index.php

See `SETUP.md` for detailed instructions.

## Testing

### Postman
- Import endpoints as collection
- Use base URL: `http://localhost/easymechanic/api/`
- Add `Authorization: Bearer {token}` header for protected endpoints

### Android Retrofit
```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("http://localhost/easymechanic/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

## Extensibility

### Razorpay Integration
- Structure ready in `payment/process_payment.php`
- Add Razorpay SDK
- Update verification logic
- Add API keys to config

### OpenAI Integration
- Structure ready in `ai/troubleshoot.php`
- Uncomment OpenAI function
- Add API key to config
- Replace rule-based logic

## Production Checklist

- [ ] Change JWT_SECRET
- [ ] Disable error display
- [ ] Set strong database password
- [ ] Enable HTTPS
- [ ] Configure CORS origins
- [ ] Set up backups
- [ ] Configure Razorpay keys
- [ ] Set up OpenAI key (if using)

## Documentation Files

- **README.md** - Complete API documentation
- **SETUP.md** - Step-by-step setup guide
- **API_REFERENCE.md** - Quick endpoint reference
- **PROJECT_SUMMARY.md** - This overview

## Support

All endpoints return JSON responses with consistent format:
```json
{
  "success": true/false,
  "message": "Description",
  "data": { ... }
}
```

Error responses include appropriate HTTP status codes (400, 401, 403, 404, 500, etc.)

## Next Steps

1. **Test all endpoints** using Postman
2. **Integrate with Android app** using Retrofit
3. **Configure payment gateway** (Razorpay)
4. **Set up AI service** (OpenAI, optional)
5. **Deploy to production** server

---

**API Version**: 1.0.0  
**Last Updated**: 2024  
**Status**: Production Ready (after configuration)

