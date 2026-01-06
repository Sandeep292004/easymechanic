# 🔧 COMPLETE FIX: 404 Error & Database Connection

## ❌ Current Problem
- **404 Not Found** error when registering
- Data not saving to database
- Apache is NOT running

## ✅ Step-by-Step Solution

### STEP 1: Start Apache in XAMPP (CRITICAL!)

1. **Open XAMPP Control Panel**
   - Find XAMPP in Start Menu
   - Or navigate to: `C:\xampp\xampp-control.exe`

2. **Start Apache:**
   - Find "Apache" in the list
   - Click the **"Start"** button
   - Wait until it shows **"Running"** (green background)
   - If it shows errors, check the "Logs" button

3. **Start MySQL:**
   - Find "MySQL" in the list
   - Click the **"Start"** button
   - Wait until it shows **"Running"** (green background)

### STEP 2: Verify Database Exists

1. **Open phpMyAdmin:**
   - Go to: `http://localhost/phpmyadmin/`
   - Or click "Admin" next to MySQL in XAMPP

2. **Check Database:**
   - Look for database: `easymechanic`
   - If it doesn't exist, create it:
     - Click "New" on left sidebar
     - Database name: `easymechanic`
     - Collation: `utf8mb4_unicode_ci`
     - Click "Create"

3. **Import Schema:**
   - Select `easymechanic` database
   - Click "Import" tab
   - Choose file: `C:\xampp\htdocs\easymechanic\api\database\schema.sql`
   - Click "Go"
   - Wait for "Import has been successfully finished"

4. **Verify Tables:**
   - You should see these tables:
     - `users`
     - `mechanics`
     - `service_requests`
     - `payments`
     - etc.

### STEP 3: Test API in Browser

1. **Test Health Check:**
   - Open browser
   - Go to: `http://localhost/easymechanic/api/index.php`
   - Should see JSON response (not 404)

2. **Test Registration Endpoint:**
   - Use Postman or browser developer tools
   - URL: `http://localhost/easymechanic/api/auth/user_register.php`
   - Method: POST
   - Headers: `Content-Type: application/json`
   - Body:
     ```json
     {
       "name": "Test User",
       "email": "test@example.com",
       "phone": "1234567890",
       "password": "test123"
     }
     ```
   - Should return success response (not 404)

### STEP 4: Verify Android App URL

**Current IP:** `10.183.237.243` (already correct in ApiClient.kt)

**File:** `app/src/main/java/com/example/easymechanic/data/api/ApiClient.kt`

**Current setting:**
```kotlin
private const val BASE_URL = "http://10.183.237.243/easymechanic/api/"
```

**✅ This is correct!** No changes needed.

### STEP 5: Test from Android App

1. **Ensure Apache is running** (Step 1)
2. **Ensure database exists** (Step 2)
3. **Run Android app**
4. **Try to register**
5. **Check Logcat** for API requests:
   - Filter: `OkHttp`
   - Look for request/response logs

### STEP 6: Verify Data in Database

1. **Open phpMyAdmin:** `http://localhost/phpmyadmin/`
2. **Select database:** `easymechanic`
3. **Click table:** `users`
4. **Click "Browse"** tab
5. **You should see registered users!**

## 🔍 Troubleshooting

### If Apache Won't Start:

1. **Check Port 80:**
   - Another application might be using port 80
   - Common culprits: Skype, IIS, other web servers
   - Solution: Stop those applications or change Apache port

2. **Check XAMPP Logs:**
   - Click "Logs" button next to Apache
   - Look for error messages
   - Common errors:
     - Port already in use
     - Permission denied
     - Configuration error

3. **Run as Administrator:**
   - Right-click XAMPP Control Panel
   - Select "Run as administrator"

### If Database Connection Fails:

1. **Check MySQL is running** in XAMPP
2. **Verify credentials** in `api/config/config.php`:
   ```php
   define('DB_HOST', 'localhost');
   define('DB_USER', 'root');
   define('DB_PASS', '');
   define('DB_NAME', 'easymechanic');
   ```
3. **Test connection** in phpMyAdmin

### If Still Getting 404:

1. **Verify files are in correct location:**
   - `C:\xampp\htdocs\easymechanic\api\index.php` ✅
   - `C:\xampp\htdocs\easymechanic\api\auth\user_register.php` ✅

2. **Restart Apache:**
   - Stop Apache in XAMPP
   - Wait 5 seconds
   - Start Apache again

3. **Check .htaccess file:**
   - Should exist at: `C:\xampp\htdocs\easymechanic\api\.htaccess`
   - If missing, it's okay (not required)

## ✅ Success Checklist

- [ ] Apache is running (green in XAMPP)
- [ ] MySQL is running (green in XAMPP)
- [ ] Database `easymechanic` exists
- [ ] Tables are imported (users, mechanics, etc.)
- [ ] `http://localhost/easymechanic/api/index.php` works in browser
- [ ] Android app BASE_URL is correct: `http://10.183.237.243/easymechanic/api/`
- [ ] Phone and computer are on same WiFi network
- [ ] Registration works and data appears in database

## 🎯 Quick Test Commands

**Test API (PowerShell):**
```powershell
Invoke-WebRequest -Uri "http://localhost/easymechanic/api/index.php" -Method GET
```

**Test Registration (PowerShell):**
```powershell
$body = @{name="Test";email="test@test.com";phone="1234567890";password="test123"} | ConvertTo-Json
Invoke-WebRequest -Uri "http://localhost/easymechanic/api/auth/user_register.php" -Method POST -Body $body -ContentType "application/json"
```

## 📝 Summary

**The main issue is Apache not running.** Once you start Apache in XAMPP, the 404 error will be fixed and data will save to the database.

**Your Android app URL is already correct!** No changes needed there.

