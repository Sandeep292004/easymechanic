# EASY MECHANIC - Connectivity Test Script
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "EASY MECHANIC - Connectivity Test" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check XAMPP Apache Status
Write-Host "1. Checking XAMPP Apache..." -ForegroundColor Yellow
$apacheProcess = Get-Process -Name "httpd" -ErrorAction SilentlyContinue
if ($apacheProcess) {
    Write-Host "   ✅ Apache is running" -ForegroundColor Green
} else {
    Write-Host "   ❌ Apache is NOT running - Please start XAMPP Apache" -ForegroundColor Red
}

# Check XAMPP MySQL Status
Write-Host "2. Checking XAMPP MySQL..." -ForegroundColor Yellow
$mysqlProcess = Get-Process -Name "mysqld" -ErrorAction SilentlyContinue
if ($mysqlProcess) {
    Write-Host "   ✅ MySQL is running" -ForegroundColor Green
} else {
    Write-Host "   ❌ MySQL is NOT running - Please start XAMPP MySQL" -ForegroundColor Red
}

# Check API Files Location
Write-Host "3. Checking API files location..." -ForegroundColor Yellow
$apiPath = "C:\xampp\htdocs\easymechanic\api"
if (Test-Path $apiPath) {
    Write-Host "   ✅ API directory exists: $apiPath" -ForegroundColor Green
    
    $requiredFiles = @(
        "index.php",
        "auth\user_register.php",
        "auth\user_login.php",
        "config\config.php",
        "config\db.php"
    )
    
    $allFilesExist = $true
    foreach ($file in $requiredFiles) {
        $filePath = Join-Path $apiPath $file
        if (Test-Path $filePath) {
            Write-Host "   ✅ $file exists" -ForegroundColor Green
        } else {
            Write-Host "   ❌ $file NOT found" -ForegroundColor Red
            $allFilesExist = $false
        }
    }
} else {
    Write-Host "   ❌ API directory NOT found: $apiPath" -ForegroundColor Red
    Write-Host "   Please copy API files to: $apiPath" -ForegroundColor Yellow
}

# Test API Endpoint
Write-Host "4. Testing API endpoint..." -ForegroundColor Yellow
$testUrl = "http://localhost/easymechanic/api/index.php"
try {
    $response = Invoke-WebRequest -Uri $testUrl -Method GET -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        Write-Host "   ✅ API is accessible!" -ForegroundColor Green
        Write-Host "   Response: $($response.Content.Substring(0, [Math]::Min(100, $response.Content.Length)))..." -ForegroundColor Gray
    } else {
        Write-Host "   ⚠️  API returned status: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "   ❌ API is NOT accessible" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   Please check:" -ForegroundColor Yellow
    Write-Host "   - Apache is running in XAMPP" -ForegroundColor Yellow
    Write-Host "   - Files are in C:\xampp\htdocs\easymechanic\api\" -ForegroundColor Yellow
    Write-Host "   - URL: $testUrl" -ForegroundColor Yellow
}

# Test Registration Endpoint
Write-Host "5. Testing Registration endpoint..." -ForegroundColor Yellow
$registerUrl = "http://localhost/easymechanic/api/auth/user_register.php"
$testBody = @{
    name = "Test User"
    email = "test$(Get-Random)@test.com"
    phone = "1234567890"
    password = "test123"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri $registerUrl -Method POST -Body $testBody -ContentType "application/json" -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        $jsonResponse = $response.Content | ConvertFrom-Json
        if ($jsonResponse.success) {
            Write-Host "   ✅ Registration endpoint working!" -ForegroundColor Green
        } else {
            Write-Host "   ⚠️  Registration endpoint responded: $($jsonResponse.message)" -ForegroundColor Yellow
        }
    }
} catch {
    Write-Host "   ❌ Registration endpoint error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Test Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

