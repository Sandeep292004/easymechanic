# Complete Connectivity Setup - EASY MECHANIC

## ✅ Setup Verification Checklist

### 1. XAMPP Setup ✅
- [x] XAMPP is installed
- [x] Apache is running
- [x] MySQL is running
- [x] API files are in `C:\xampp\htdocs\easymechanic\api\`

### 2. Database Setup
1. Open phpMyAdmin: `http://localhost/phpmyadmin/`
2. Create database: `easymechanic`
3. Import schema: `api/database/schema.sql`

### 3. API Files Location
All API files should be in:
```
C:\xampp\htdocs\easymechanic\api\
├── auth/
│   ├── user_register.php
│   ├── user_login.php
│   └── ...
├── config/
│   ├── config.php
│   ├── db.php
│   ├── headers.php
│   └── jwt.php
└── ...
```

### 4. Android App Configuration

#### Base URL Configuration
The app is configured to use:
- **Emulator**: `http://10.0.2.2/easymechanic/api/`
- **Physical Device**: Update `ApiClient.kt` with your computer's IP

#### To Find Your Computer's IP:
1. Open Command Prompt
2. Run: `ipconfig`
3. Find "IPv4 Address" (e.g., 192.168.1.100)
4. Update `ApiClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://192.168.1.100/easymechanic/api/"
   ```

### 5. Testing Connectivity

#### Test Backend (Browser/Postman):
```
GET http://localhost/easymechanic/api/index.php
```

#### Test Registration Endpoint:
```
POST http://localhost/easymechanic/api/auth/user_register.php
Content-Type: application/json

{
  "name": "Test User",
  "email": "test@example.com",
  "phone": "1234567890",
  "password": "test123"
}
```

#### Test Login Endpoint:
```
POST http://localhost/easymechanic/api/auth/user_login.php
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "test123"
}
```

### 6. Android App Testing

1. **Start XAMPP** (Apache + MySQL)
2. **Run Android App** (Emulator or Physical Device)
3. **Check Logcat** for HTTP requests:
   - Filter by: `OkHttp`
   - You should see request/response logs

### 7. Common Issues & Solutions

#### Issue: Connection Refused
**Solution:**
- Check if Apache is running in XAMPP
- Verify base URL in `ApiClient.kt`
- For physical device, ensure phone and computer are on same WiFi network

#### Issue: 404 Not Found
**Solution:**
- Verify API files are in `C:\xampp\htdocs\easymechanic\api\`
- Check file permissions
- Restart Apache

#### Issue: Database Connection Failed
**Solution:**
- Check if MySQL is running
- Verify database exists
- Check `config/config.php` database credentials

#### Issue: JSON Parsing Error
**Solution:**
- Verify backend response format matches `ApiResponse` model
- Check Gson annotations in data models

### 8. Network Configuration

#### For Android Emulator:
- Use: `http://10.0.2.2/easymechanic/api/`
- `10.0.2.2` automatically maps to `localhost` on host machine

#### For Physical Device:
1. Find your computer's IP address
2. Update `ApiClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://YOUR_IP/easymechanic/api/"
   ```
3. Ensure phone and computer are on same WiFi network
4. Disable Windows Firewall or allow Apache through firewall

### 9. Verification Steps

1. ✅ XAMPP Apache running
2. ✅ XAMPP MySQL running
3. ✅ Database `easymechanic` created
4. ✅ Schema imported
5. ✅ API files in `C:\xampp\htdocs\easymechanic\api\`
6. ✅ Test API in browser/Postman
7. ✅ Android app base URL configured
8. ✅ Run app and test registration/login

### 10. Current Status

- ✅ Retrofit configured
- ✅ API endpoints defined
- ✅ Data models created
- ✅ Repository pattern implemented
- ✅ ViewModel integration complete
- ✅ Error handling implemented
- ✅ Loading states managed

## 🚀 Ready to Use!

Your app is now fully connected to the XAMPP backend. Users can:
- Register new accounts (stored in MySQL)
- Login with credentials (authenticated via JWT)
- Data persists in database
- Tokens stored securely in SharedPreferences

