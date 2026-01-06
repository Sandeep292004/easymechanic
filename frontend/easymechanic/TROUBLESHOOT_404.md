# 🔧 Troubleshooting 404 Error - EASY MECHANIC API

## ❌ Problem
Getting `HTTP 404 Not Found` when accessing API endpoints.

## ✅ Solution Steps

### Step 1: Verify XAMPP Apache is Running

1. **Open XAMPP Control Panel**
2. **Check Apache Status:**
   - Should show "Running" (green)
   - If not running, click "Start" button
   - Wait for it to turn green

3. **Check MySQL Status:**
   - Should also be running
   - Click "Start" if not running

### Step 2: Verify Files Location

Files should be in:
```
C:\xampp\htdocs\easymechanic\api\
```

**Check if files exist:**
- Open File Explorer
- Navigate to: `C:\xampp\htdocs\easymechanic\api\`
- Verify these files exist:
  - `index.php`
  - `auth/user_register.php`
  - `auth/user_login.php`
  - `config/config.php`
  - `config/db.php`

### Step 3: Test in Browser

1. **Open your web browser**
2. **Test these URLs:**
   - `http://localhost/easymechanic/api/index.php`
   - `http://localhost/easymechanic/api/auth/user_register.php`

3. **Expected Results:**
   - `index.php` should return JSON response
   - `user_register.php` should return error about POST method (not 404)

### Step 4: Check Apache Configuration

1. **Open XAMPP Control Panel**
2. **Click "Config" next to Apache**
3. **Select "httpd.conf"**
4. **Verify DocumentRoot:**
   ```
   DocumentRoot "C:/xampp/htdocs"
   ```
5. **Verify Directory:**
   ```
   <Directory "C:/xampp/htdocs">
       Options Indexes FollowSymLinks
       AllowOverride All
       Require all granted
   </Directory>
   ```

### Step 5: Restart Apache

1. **In XAMPP Control Panel:**
   - Click "Stop" on Apache
   - Wait 5 seconds
   - Click "Start" on Apache
   - Wait for it to turn green

### Step 6: Check Firewall/Antivirus

- **Windows Firewall** might be blocking Apache
- **Antivirus** might be blocking localhost connections
- **Temporarily disable** to test (re-enable after)

### Step 7: Verify Port 80 is Available

1. **Open Command Prompt as Administrator**
2. **Run:**
   ```cmd
   netstat -ano | findstr :80
   ```
3. **If port 80 is in use:**
   - Check what process is using it
   - Stop that process or change Apache port

### Step 8: Test with Simple PHP File

1. **Create test file:**
   - Location: `C:\xampp\htdocs\test.php`
   - Content: `<?php echo "PHP Works!"; ?>`

2. **Access in browser:**
   - `http://localhost/test.php`
   - Should display: "PHP Works!"

3. **If this doesn't work:**
   - Apache is not running or misconfigured
   - Follow Steps 1-5 again

### Step 9: Check IP Address (For Physical Device)

If testing from Android device:

1. **Find your computer's IP:**
   - Open Command Prompt
   - Run: `ipconfig`
   - Find "IPv4 Address" (e.g., 192.168.1.100)

2. **Update ApiClient.kt:**
   ```kotlin
   private const val BASE_URL = "http://YOUR_IP/easymechanic/api/"
   ```

3. **Verify phone and computer are on same WiFi**

### Step 10: Alternative - Use Different Port

If port 80 is blocked:

1. **Change Apache port to 8080:**
   - Edit `httpd.conf`
   - Change: `Listen 80` to `Listen 8080`
   - Change: `ServerName localhost:80` to `ServerName localhost:8080`

2. **Update URLs:**
   - Browser: `http://localhost:8080/easymechanic/api/index.php`
   - Android: `http://YOUR_IP:8080/easymechanic/api/`

## 🎯 Quick Fix Checklist

- [ ] Apache is running in XAMPP (green status)
- [ ] MySQL is running in XAMPP (green status)
- [ ] Files are in `C:\xampp\htdocs\easymechanic\api\`
- [ ] Can access `http://localhost/test.php` (if created)
- [ ] Can access `http://localhost/easymechanic/api/index.php`
- [ ] Firewall is not blocking Apache
- [ ] Port 80 is not in use by another application
- [ ] Apache has been restarted after any changes

## 📞 Still Not Working?

1. **Check XAMPP Error Logs:**
   - Location: `C:\xampp\apache\logs\error.log`
   - Look for any error messages

2. **Check Apache Service:**
   - Open Services (services.msc)
   - Find "Apache2.4" or similar
   - Check if it's running

3. **Reinstall XAMPP:**
   - If nothing else works, reinstall XAMPP
   - Make sure to backup your files first

## ✅ Success Indicators

When working correctly:
- ✅ `http://localhost/easymechanic/api/index.php` returns JSON
- ✅ `http://localhost/easymechanic/api/auth/user_register.php` returns method error (not 404)
- ✅ Android app can connect to API
- ✅ No 404 errors in Logcat

