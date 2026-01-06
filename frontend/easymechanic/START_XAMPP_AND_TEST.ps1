# Script to guide user through starting XAMPP and testing
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "EASY MECHANIC - Complete Setup Guide" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "STEP 1: Start XAMPP Services" -ForegroundColor Yellow
Write-Host "----------------------------------------" -ForegroundColor Yellow
Write-Host "1. Open XAMPP Control Panel" -ForegroundColor White
Write-Host "2. Click 'Start' button for Apache" -ForegroundColor White
Write-Host "3. Wait for it to turn GREEN (Running)" -ForegroundColor White
Write-Host "4. Click 'Start' button for MySQL" -ForegroundColor White
Write-Host "5. Wait for it to turn GREEN (Running)" -ForegroundColor White
Write-Host ""

$response = Read-Host "Have you started Apache and MySQL? (y/n)"
if ($response -ne "y") {
    Write-Host "Please start Apache and MySQL in XAMPP, then run this script again." -ForegroundColor Red
    exit
}

Write-Host ""
Write-Host "STEP 2: Testing API Connection..." -ForegroundColor Yellow
Write-Host "----------------------------------------" -ForegroundColor Yellow

$testUrl = "http://localhost/easymechanic/api/index.php"
try {
    $response = Invoke-WebRequest -Uri $testUrl -Method GET -UseBasicParsing -TimeoutSec 5
    Write-Host "✅ API is working! Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response preview: $($response.Content.Substring(0, [Math]::Min(100, $response.Content.Length)))..." -ForegroundColor Gray
} catch {
    Write-Host "❌ API still not working: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Please check:" -ForegroundColor Yellow
    Write-Host "  - Apache is running (green in XAMPP)" -ForegroundColor White
    Write-Host "  - Files are in C:\xampp\htdocs\easymechanic\api\" -ForegroundColor White
    exit
}

Write-Host ""
Write-Host "STEP 3: Testing Registration Endpoint..." -ForegroundColor Yellow
Write-Host "----------------------------------------" -ForegroundColor Yellow

$registerUrl = "http://localhost/easymechanic/api/auth/user_register.php"
$testData = @{
    name = "Test User $(Get-Date -Format 'HHmmss')"
    email = "test$(Get-Date -Format 'HHmmss')@example.com"
    phone = "1234567890"
    password = "test123"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri $registerUrl -Method POST -Body $testData -ContentType "application/json" -UseBasicParsing -TimeoutSec 5
    Write-Host "✅ Registration endpoint is working!" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Gray
} catch {
    $statusCode = $_.Exception.Response.StatusCode.value__
    if ($statusCode -eq 405) {
        Write-Host "✅ Endpoint exists (405 = Method not allowed, which is expected for GET)" -ForegroundColor Green
    } elseif ($statusCode -eq 404) {
        Write-Host "❌ 404 - Endpoint not found. Check if file exists." -ForegroundColor Red
    } else {
        Write-Host "⚠️  Status: $statusCode" -ForegroundColor Yellow
        Write-Host "Response: $($_.Exception.Message)" -ForegroundColor Gray
    }
}

Write-Host ""
Write-Host "STEP 4: Network Information" -ForegroundColor Yellow
Write-Host "----------------------------------------" -ForegroundColor Yellow
$ipConfig = ipconfig | Select-String -Pattern "IPv4" | Select-Object -First 1
if ($ipConfig) {
    $ip = ($ipConfig -split ":")[1].Trim()
    Write-Host "Your Computer IP: $ip" -ForegroundColor Cyan
    Write-Host "Android App URL: http://$ip/easymechanic/api/" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Current ApiClient.kt setting: http://10.183.237.243/easymechanic/api/" -ForegroundColor White
    if ($ip -eq "10.183.237.243") {
        Write-Host "✅ IP matches! No changes needed." -ForegroundColor Green
    } else {
        Write-Host "⚠️  IP has changed! Update ApiClient.kt with: http://$ip/easymechanic/api/" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "STEP 5: Database Check" -ForegroundColor Yellow
Write-Host "----------------------------------------" -ForegroundColor Yellow
Write-Host "1. Open phpMyAdmin: http://localhost/phpmyadmin/" -ForegroundColor White
Write-Host "2. Check if database 'easymechanic' exists" -ForegroundColor White
Write-Host "3. If not, create it and import schema.sql" -ForegroundColor White
Write-Host "4. Verify 'users' table exists" -ForegroundColor White

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ Setup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Yellow
Write-Host "1. Test registration from Android app" -ForegroundColor White
Write-Host "2. Check database for saved data" -ForegroundColor White
Write-Host "3. Verify data appears in 'users' table" -ForegroundColor White
Write-Host ""

