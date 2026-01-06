# EASY MECHANIC API - Quick Start Guide

## ✅ Files Successfully Copied to XAMPP

All API files have been copied to: `C:\xampp\htdocs\easymechanic\api`

## 📋 Setup Checklist

### Step 1: Start XAMPP Services
1. Open **XAMPP Control Panel**
2. Start **Apache** service (click "Start")
3. Start **MySQL** service (click "Start")
4. Both should show green "Running" status

### Step 2: Create Database
1. Open phpMyAdmin: http://localhost/phpmyadmin/
2. Click on **"SQL"** tab
3. Open file: `C:\xampp\htdocs\easymechanic\api\database\schema.sql`
4. Copy entire content and paste in SQL tab
5. Click **"Go"** to execute
6. Verify database `easymechanic` is created with all tables

### Step 3: Configure Database (if needed)
1. Open: `C:\xampp\htdocs\easymechanic\api\config\config.php`
2. Update if your MySQL has a password:
   ```php
   define('DB_USER', 'root');
   define('DB_PASS', 'your_password_here');
   ```

### Step 4: Test API
1. Open browser: http://localhost/easymechanic/api/index.php
2. You should see JSON response with API status
3. If you see "database connected", setup is successful!

## 🧪 Quick Test

### Test User Registration
**URL**: http://localhost/easymechanic/api/auth/user_register.php  
**Method**: POST  
**Body** (JSON):
```json
{
  "name": "Test User",
  "email": "test@example.com",
  "phone": "1234567890",
  "password": "test123",
  "vehicle_type": "Car",
  "vehicle_number": "ABC123"
}
```

### Test User Login
**URL**: http://localhost/easymechanic/api/auth/user_login.php  
**Method**: POST  
**Body** (JSON):
```json
{
  "email": "test@example.com",
  "password": "test123"
}
```

## 📁 File Structure

```
C:\xampp\htdocs\easymechanic\api\
├── config/              # Configuration files
├── auth/                # Authentication endpoints
├── mechanic/           # Mechanic endpoints
├── requests/           # Service request endpoints
├── payment/            # Payment endpoints
├── ai/                 # AI troubleshooting
├── database/           # Database schema and migrations
├── index.php           # Health check endpoint
├── .htaccess           # Apache configuration
└── README.md           # Full documentation
```

## 🔗 Important URLs

- **API Base URL**: http://localhost/easymechanic/api/
- **Health Check**: http://localhost/easymechanic/api/index.php
- **phpMyAdmin**: http://localhost/phpmyadmin/

## 📚 Documentation

- **README.md** - Complete API documentation
- **SETUP.md** - Detailed setup instructions
- **API_REFERENCE.md** - Quick endpoint reference
- **database/README.md** - Database documentation

## ⚠️ Troubleshooting

### Issue: "Database connection failed"
- Check MySQL is running in XAMPP
- Verify database name is `easymechanic`
- Check credentials in `config/config.php`

### Issue: "404 Not Found"
- Verify Apache is running
- Check files are in correct directory
- Try: http://localhost/easymechanic/api/index.php

### Issue: "CORS error"
- CORS is configured for API access
- Use Postman or Android app (not browser directly)
- Check `config/headers.php` is included

## ✅ Next Steps

1. Test all endpoints using Postman
2. Integrate with Android app using Retrofit
3. Configure Razorpay (if needed)
4. Set up OpenAI (if needed)

---

**API is ready to use!** 🚀

