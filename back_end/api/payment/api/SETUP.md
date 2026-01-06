# EASY MECHANIC API - Quick Setup Guide

## Step-by-Step Installation

### 1. Install XAMPP
- Download and install XAMPP from https://www.apachefriends.org/
- Ensure Apache and MySQL services are installed

### 2. Copy API Files
1. Copy the entire `api` folder to: `C:\xampp\htdocs\easymechanic\`
2. Final structure should be: `C:\xampp\htdocs\easymechanic\api\`

### 3. Start XAMPP Services
1. Open XAMPP Control Panel
2. Start **Apache** service
3. Start **MySQL** service

### 4. Create Database
1. Open phpMyAdmin: http://localhost/phpmyadmin/
2. Click on "SQL" tab
3. Copy and paste the entire content from `api/database/schema.sql`
4. Click "Go" to execute
5. Verify database `easymechanic` is created with all tables

### 5. Configure Database (if needed)
- Open `C:\xampp\htdocs\easymechanic\api\config\config.php`
- Update database credentials if your MySQL has a password:
  ```php
  define('DB_USER', 'root');
  define('DB_PASS', 'your_password_here');
  ```

### 6. Test API
1. Open browser: http://localhost/easymechanic/api/index.php
2. You should see a JSON response with API status
3. If you see "database connected", setup is successful!

## Testing with Postman

### Test User Registration
1. **Method**: POST
2. **URL**: http://localhost/easymechanic/api/auth/user_register.php
3. **Headers**: 
   - `Content-Type: application/json`
4. **Body** (raw JSON):
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
5. Click "Send"
6. You should receive a response with a token

### Test Mechanic Registration
1. **Method**: POST
2. **URL**: http://localhost/easymechanic/api/auth/mechanic_register.php
3. **Headers**: 
   - `Content-Type: application/json`
4. **Body** (raw JSON):
   ```json
   {
     "name": "Test Mechanic",
     "email": "mechanic@example.com",
     "phone": "1234567890",
     "password": "test123",
     "specialization": "Engine Repair",
     "experience_years": 5
   }
   ```
5. Click "Send"
6. You should receive a response with a token

### Test User Login
1. **Method**: POST
2. **URL**: http://localhost/easymechanic/api/auth/user_login.php
3. **Headers**: 
   - `Content-Type: application/json`
4. **Body** (raw JSON):
   ```json
   {
     "email": "test@example.com",
     "password": "test123"
   }
   ```
5. Copy the token from response

### Test Mechanic Login
1. **Method**: POST
2. **URL**: http://localhost/easymechanic/api/auth/mechanic_login.php
3. **Headers**: 
   - `Content-Type: application/json`
4. **Body** (raw JSON):
   ```json
   {
     "email": "mechanic@example.com",
     "password": "test123"
   }
   ```
5. Copy the token from response

### Test Protected Endpoint
1. **Method**: GET
2. **URL**: http://localhost/easymechanic/api/requests/get_requests.php
3. **Headers**: 
   - `Content-Type: application/json`
   - `Authorization: Bearer YOUR_TOKEN_HERE`
4. Replace `YOUR_TOKEN_HERE` with the token from login

## Common Issues & Solutions

### Issue: "Database connection failed"
**Solution**: 
- Check MySQL is running in XAMPP
- Verify database name is `easymechanic`
- Check credentials in `config.php`

### Issue: "404 Not Found"
**Solution**:
- Verify files are in `C:\xampp\htdocs\easymechanic\api\`
- Check Apache is running
- Try: http://localhost/easymechanic/api/index.php

### Issue: "CORS error" in browser
**Solution**:
- CORS is configured for API access
- Use Postman or Android app (not browser directly)
- Check `headers.php` is included in endpoints

### Issue: "Token invalid"
**Solution**:
- Ensure token is sent as: `Authorization: Bearer TOKEN`
- Check token hasn't expired (24 hours)
- Try logging in again to get new token

## Next Steps

1. **Test all endpoints** using Postman
2. **Integrate with Android app** using Retrofit
3. **Configure Razorpay** (if needed) in `config.php`
4. **Set up OpenAI** (if needed) in `ai/troubleshoot.php`
5. **Change JWT_SECRET** in production

## Production Checklist

- [ ] Change `JWT_SECRET` to a strong random string
- [ ] Set `error_reporting(0)` in `config.php`
- [ ] Set `display_errors` to `Off` in `.htaccess`
- [ ] Use strong database password
- [ ] Enable HTTPS
- [ ] Configure proper CORS origins
- [ ] Set up database backups
- [ ] Configure Razorpay production keys
- [ ] Set up OpenAI API key (if using)

## Support

For detailed API documentation, see `README.md`

