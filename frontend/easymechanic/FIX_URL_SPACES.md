# 🔧 Fix: Invalid URL host error with leading spaces

## ❌ Error
```
Caused by: java.lang.IllegalArgumentException: Invalid URL host: " 10.193.88.243"
Caused by: java.lang.IllegalArgumentException: Invalid URL host: " 192.168.137.1"
```

## ✅ Solution

The error shows there's a **leading space** in the URL. This happens when the BASE_URL has a space after `http://`.

### Files to Check:

1. **`app/src/main/java/com/example/easymechanic/data/api/ApiClient.kt`**
   - Check line with `BASE_URL`
   - Should be: `"http://10.183.237.243/easymechanic/api/"`
   - NOT: `"http:// 10.183.237.243/easymechanic/api/"` ❌

2. **If you have `RetrofitClient.kt`** (mentioned in error but not found):
   - Check the BASE_URL definition
   - Remove any spaces after `http://`

3. **Any other files with URL definitions:**
   - Search for `http://` in your project
   - Ensure no spaces after the protocol

### Quick Fix:

1. Open `ApiClient.kt`
2. Find the line: `private const val BASE_URL = "..."`
3. Make sure it's: `"http://YOUR_IP/easymechanic/api/"` (no space after `://`)
4. Rebuild the project

### For Different Devices:

**Android Emulator:**
```kotlin
private const val BASE_URL = "http://10.0.2.2/easymechanic/api/"
```

**Physical Device (same WiFi):**
```kotlin
private const val BASE_URL = "http://YOUR_COMPUTER_IP/easymechanic/api/"
```

**To find your computer's IP:**
- Windows: Open Command Prompt, run `ipconfig`
- Look for "IPv4 Address" (e.g., 192.168.1.100)

### Common Mistakes:

❌ `"http:// 192.168.1.100/easymechanic/api/"` (space after `://`)
❌ `"http://192.168.1.100 /easymechanic/api/"` (space before `/`)
✅ `"http://192.168.1.100/easymechanic/api/"` (correct)

