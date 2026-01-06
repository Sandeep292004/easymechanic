# 🚨 URGENT: Fix "Invalid URL host" Error

## ❌ Error Message
```
Caused by: java.lang.IllegalArgumentException: Invalid URL host: " 10.193.88.243"
Caused by: java.lang.IllegalArgumentException: Invalid URL host: " 192.168.137.1"
```

**Notice the leading space before the IP address!**

## 🔍 Root Cause

The error trace shows:
```
at com.example.easymechanic.data.api.RetrofitClient.retrofit_delegate$lambda$1(RetrofitClient.kt:31)
```

This means you have a file called **`RetrofitClient.kt`** that has a URL with a **leading space**.

## ✅ Solution Steps

### Step 1: Find RetrofitClient.kt

1. **In Android Studio:**
   - Press `Ctrl + Shift + F` (or `Cmd + Shift + F` on Mac)
   - Search for: `RetrofitClient`
   - This will show you where the file is

2. **Or search in File Explorer:**
   - Search for `RetrofitClient.kt` in your project folder

### Step 2: Fix the URL

Open `RetrofitClient.kt` and find the line with `BASE_URL` or `baseUrl`.

**❌ WRONG (has space):**
```kotlin
private const val BASE_URL = "http:// 10.193.88.243/easymechanic/api/"
//                                    ^ SPACE HERE!
```

**✅ CORRECT (no space):**
```kotlin
private const val BASE_URL = "http://10.193.88.243/easymechanic/api/"
//                                   ^ NO SPACE!
```

### Step 3: Check All URL Definitions

Search your entire project for `http://` and check:

1. **ApiClient.kt** - Already checked, looks good ✅
2. **RetrofitClient.kt** - **THIS IS THE PROBLEM** ❌
3. **Any other files** with URL definitions

### Step 4: Common URL Patterns to Fix

**❌ Wrong:**
```kotlin
"http:// 192.168.1.100/easymechanic/api/"  // Space after ://
"http://192.168.1.100 /easymechanic/api/"  // Space before /
"http:// 192.168.1.100 /easymechanic/api/" // Spaces everywhere
```

**✅ Correct:**
```kotlin
"http://192.168.1.100/easymechanic/api/"   // Perfect!
```

### Step 5: Rebuild

1. **Clean Project:** `Build > Clean Project`
2. **Rebuild:** `Build > Rebuild Project`
3. **Run:** Install and test on device

## 🔧 Quick Fix Script

If you can't find the file, run this in PowerShell from your project root:

```powershell
Get-ChildItem -Path "app\src" -Recurse -Filter "*.kt" | 
    Select-String -Pattern 'http://\s+' | 
    Select-Object Path, LineNumber, Line
```

This will show you all files with spaces after `http://`.

## 📝 Alternative: Use ApiClient.kt

If you have both `ApiClient.kt` and `RetrofitClient.kt`, consider:

1. **Remove `RetrofitClient.kt`** (if it's duplicate)
2. **Use only `ApiClient.kt`** (which is already correct)

## ✅ Verification

After fixing, the URL should look like:
```kotlin
private const val BASE_URL = "http://YOUR_IP/easymechanic/api/"
```

**No spaces anywhere!**

---

## 🆘 Still Not Working?

1. **Check Logcat** for the exact error
2. **Search entire project** for `RetrofitClient`
3. **Check build.gradle** for any URL configurations
4. **Clean and rebuild** the project

The error is definitely a **leading space in a URL**. Find and remove it!

