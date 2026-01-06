# Comprehensive API Connection Test
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "EASY MECHANIC - API Connection Test" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check if files exist
Write-Host "1. Checking API files..." -ForegroundColor Yellow
$apiPath = "C:\xampp\htdocs\easymechanic\api"
$keyFiles = @(
    "$apiPath\index.php",
    "$apiPath\auth\user_register.php",
    "$apiPath\config\config.php",
    "$apiPath\config\db.php"
)

$allExist = $true
foreach ($file in $keyFiles) {
    if (Test-Path $file) {
        Write-Host "   ✅ $(Split-Path $file -Leaf)" -ForegroundColor Green
    } else {
        Write-Host "   ❌ $(Split-Path $file -Leaf) - NOT FOUND" -ForegroundColor Red
        $allExist = $false
    }
}

if (-Not $allExist) {
    Write-Host "`n⚠️  Some files missing. Running copy script..." -ForegroundColor Yellow
    & ".\copy_to_xampp.ps1"
}

Write-Host ""

# Step 2: Test Apache
Write-Host "2. Testing Apache connection..." -ForegroundColor Yellow
$testUrls = @(
    "http://localhost/easymechanic/api/index.php",
    "http://127.0.0.1/easymechanic/api/index.php"
)

$apacheWorking = $false
foreach ($url in $testUrls) {
    try {
        $response = Invoke-WebRequest -Uri $url -Method GET -UseBasicParsing -TimeoutSec 5
        Write-Host "   ✅ $url - Status: $($response.StatusCode)" -ForegroundColor Green
        Write-Host "   Response: $($response.Content.Substring(0, [Math]::Min(100, $response.Content.Length)))..." -ForegroundColor Gray
        $apacheWorking = $true
        break
    } catch {
        Write-Host "   ❌ $url - $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""

# Step 3: Test Registration Endpoint
if ($apacheWorking) {
    Write-Host "3. Testing Registration Endpoint..." -ForegroundColor Yellow
    $registerUrl = "http://localhost/easymechanic/api/auth/user_register.php"
    
    $testData = @{
        name = "Test User"
        email = "test@example.com"
        phone = "1234567890"
        password = "test123"
    } | ConvertTo-Json
    
    try {
        $response = Invoke-WebRequest -Uri $registerUrl -Method POST -Body $testData -ContentType "application/json" -UseBasicParsing -TimeoutSec 5
        Write-Host "   ✅ Registration endpoint accessible" -ForegroundColor Green
        Write-Host "   Response: $($response.Content.Substring(0, [Math]::Min(150, $response.Content.Length)))..." -ForegroundColor Gray
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        if ($statusCode -eq 405) {
            Write-Host "   ✅ Endpoint exists (405 = Method not allowed for GET, which is correct)" -ForegroundColor Green
        } elseif ($statusCode -eq 404) {
            Write-Host "   ❌ 404 - Endpoint NOT FOUND" -ForegroundColor Red
            Write-Host "   Check if file exists: $apiPath\auth\user_register.php" -ForegroundColor Yellow
        } else {
            Write-Host "   ⚠️  Status: $statusCode - $($_.Exception.Message)" -ForegroundColor Yellow
        }
    }
} else {
    Write-Host "3. Skipping endpoint test (Apache not working)" -ForegroundColor Yellow
}

Write-Host ""

# Step 4: Get Computer IP
Write-Host "4. Network Information..." -ForegroundColor Yellow
$ipConfig = ipconfig | Select-String -Pattern "IPv4" | Select-Object -First 1
if ($ipConfig) {
    $ip = ($ipConfig -split ":")[1].Trim()
    Write-Host "   Your IP: $ip" -ForegroundColor Cyan
    Write-Host "   For Android device, use: http://$ip/easymechanic/api/" -ForegroundColor Cyan
} else {
    Write-Host "   ⚠️  Could not detect IP address" -ForegroundColor Yellow
}

Write-Host ""

# Step 5: Summary
Write-Host "========================================" -ForegroundColor Cyan
if ($apacheWorking) {
    Write-Host "✅ API IS ACCESSIBLE" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Next Steps:" -ForegroundColor Yellow
    Write-Host "1. Update ApiClient.kt with correct IP (if using physical device)" -ForegroundColor White
    Write-Host "2. Ensure database 'easymechanic' exists in phpMyAdmin" -ForegroundColor White
    Write-Host "3. Test registration from Android app" -ForegroundColor White
} else {
    Write-Host "❌ APACHE IS NOT WORKING" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "SOLUTION:" -ForegroundColor Yellow
    Write-Host "1. Open XAMPP Control Panel" -ForegroundColor White
    Write-Host "2. Start Apache (click 'Start' button)" -ForegroundColor White
    Write-Host "3. Wait for it to turn green" -ForegroundColor White
    Write-Host "4. Run this script again" -ForegroundColor White
}

Write-Host ""

