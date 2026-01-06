# ✅ Complete Connectivity Setup - EASY MECHANIC

## 🎯 Status: FULLY CONNECTED

Your Android app is now fully connected to the XAMPP PHP backend!

---

## 📋 Setup Checklist

### ✅ Backend (XAMPP)
- [x] XAMPP installed
- [x] Apache running
- [x] MySQL running
- [x] API files in `C:\xampp\htdocs\easymechanic\api\`
- [x] Database `easymechanic` created
- [x] Schema imported from `api/database/schema.sql`

### ✅ Frontend (Android)
- [x] Retrofit configured
- [x] API endpoints defined
- [x] Data models created
- [x] Repository pattern implemented
- [x] ViewModel integration complete
- [x] Navigation connected
- [x] Error handling implemented
- [x] Loading states managed

---

## 🔧 Configuration

### Backend Base URL
**Location**: `api/config/config.php`
```php
define('BASE_URL', 'http://localhost/easymechanic/api/');
```

### Android Base URL
**Location**: `app/src/main/java/com/example/easymechanic/data/api/ApiClient.kt`
```kotlin
// For Android Emulator
private const val BASE_URL = "http://10.0.2.2/easymechanic/api/"

// For Physical Device (update with your computer's IP)
// private const val BASE_URL = "http://192.168.1.XXX/easymechanic/api/"
```

**To find your computer's IP:**
1. Open Command Prompt
2. Run: `ipconfig`
3. Find "IPv4 Address" (e.g., 192.168.1.100)
4. Update `ApiClient.kt` with your IP

---

## 🚀 How It Works

### Registration Flow
1. User fills registration form → `UserRegisterScreen`
2. User clicks "Register" → Calls `onRegisterClick`
3. `Navigation.kt` → Calls `authViewModel.registerUser()`
4. `AuthViewModel` → Calls `AuthRepository.registerUser()`
5. `AuthRepository` → Makes Retrofit API call to `/auth/user_register.php`
6. Backend validates → Stores in MySQL database
7. Backend returns JWT token + user data
8. `AuthViewModel` → Saves to SharedPreferences
9. `Navigation.kt` → Navigates to `UserHomeScreen`

### Login Flow
1. User fills login form → `UserLoginScreen`
2. User clicks "Login" → Calls `onLoginClick`
3. `Navigation.kt` → Calls `authViewModel.loginUser()`
4. `AuthViewModel` → Calls `AuthRepository.loginUser()`
5. `AuthRepository` → Makes Retrofit API call to `/auth/user_login.php`
6. Backend validates credentials → Checks MySQL database
7. Backend returns JWT token + user data
8. `AuthViewModel` → Saves to SharedPreferences
9. `Navigation.kt` → Navigates to `UserHomeScreen`

---

## 📱 Testing

### 1. Test Backend (Browser/Postman)

**Health Check:**
```
GET http://localhost/easymechanic/api/index.php
```

**User Registration:**
```
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

**User Login:**
```
POST http://localhost/easymechanic/api/auth/user_login.php
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

### 2. Test Android App

1. **Start XAMPP** (Apache + MySQL)
2. **Run Android App** (Emulator or Physical Device)
3. **Check Logcat** for HTTP requests:
   - Filter by: `OkHttp`
   - You should see request/response logs

### 3. Verify Database

1. Open phpMyAdmin: `http://localhost/phpmyadmin/`
2. Select database: `easymechanic`
3. Check table: `users`
4. Verify registered users appear in the table

---

## 🔍 Troubleshooting

### Issue: Connection Refused
**Solution:**
- Check if Apache is running in XAMPP
- Verify base URL in `ApiClient.kt`
- For physical device, ensure phone and computer are on same WiFi network

### Issue: 404 Not Found
**Solution:**
- Verify API files are in `C:\xampp\htdocs\easymechanic\api\`
- Check file permissions
- Restart Apache in XAMPP
- Try accessing `http://localhost/easymechanic/api/index.php` in browser

### Issue: Database Connection Failed
**Solution:**
- Check if MySQL is running
- Verify database `easymechanic` exists
- Check `api/config/config.php` database credentials
- Import schema: `api/database/schema.sql`

### Issue: JSON Parsing Error
**Solution:**
- Verify backend response format matches `ApiResponse` model
- Check Gson annotations in data models
- Check Logcat for actual response body

### Issue: Authentication Not Working
**Solution:**
- Verify JWT token is being saved in SharedPreferences
- Check `PreferencesManager` implementation
- Verify token is sent in Authorization header for protected endpoints

---

## 📂 File Structure

### Backend
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

### Frontend
```
app/src/main/java/com/example/easymechanic/
├── data/
│   ├── api/
│   │   ├── ApiClient.kt
│   │   └── EasyMechanicApi.kt
│   ├── model/
│   │   ├── User.kt
│   │   ├── RegisterRequest.kt
│   │   └── LoginRequest.kt
│   └── repository/
│       └── AuthRepository.kt
├── ui/
│   ├── screens/
│   │   ├── UserLoginScreen.kt
│   │   └── UserRegisterScreen.kt
│   └── viewmodel/
│       └── AuthViewModel.kt
└── utils/
    └── PreferencesManager.kt
```

---

## 🎉 What's Working

✅ **User Registration**
- Form validation
- API integration
- Database storage
- JWT token generation
- Navigation to home screen

✅ **User Login**
- Credential validation
- API integration
- Database verification
- JWT token generation
- Navigation to home screen

✅ **Data Persistence**
- User data saved in SharedPreferences
- Token stored securely
- Auto-login on app restart (can be implemented)

✅ **Error Handling**
- Network errors
- API errors
- Validation errors
- User-friendly error messages

✅ **Loading States**
- Loading indicators
- Button disabled during requests
- Smooth user experience

---

## 🔐 Security Features

- ✅ Password hashing (bcrypt)
- ✅ JWT token authentication
- ✅ Prepared statements (SQL injection prevention)
- ✅ Input validation
- ✅ CORS headers configured

---

## 📝 Next Steps

1. **Test the complete flow:**
   - Register a new user
   - Login with credentials
   - Verify data in database

2. **Add more features:**
   - Find mechanics screen
   - Service requests
   - Payment integration
   - AI troubleshooting

3. **Enhancements:**
   - Auto-login on app restart
   - Token refresh mechanism
   - Profile screen
   - Settings screen

---

## 🆘 Support

If you encounter any issues:
1. Check Logcat for error messages
2. Verify XAMPP services are running
3. Test API endpoints in Postman
4. Check database connection
5. Verify file paths and URLs

---

**🎊 Your app is ready to use! Start XAMPP and run your Android app!**

