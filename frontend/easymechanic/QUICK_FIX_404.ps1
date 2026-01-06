# Quick Fix Script for 404 Error
# Run this script to diagnose and fix common issues

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "EASY MECHANIC - 404 Error Fix" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if XAMPP directory exists
$xamppPath = "C:\xampp"
if (-Not (Test-Path $xamppPath)) {
    Write-Host "❌ XAMPP not found at: $xamppPath" -ForegroundColor Red
    Write-Host "Please install XAMPP first!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ XAMPP directory found" -ForegroundColor Green

# Check if API files exist
$apiPath = "C:\xampp\htdocs\easymechanic\api"
if (Test-Path $apiPath) {
    Write-Host "✅ API directory exists: $apiPath" -ForegroundColor Green
    
    # Check key files
    $keyFiles = @(
        "$apiPath\index.php",
        "$apiPath\auth\user_register.php",
        "$apiPath\auth\user_login.php",
        "$apiPath\config\config.php",
        "$apiPath\config\db.php"
    )
    
    $allFilesExist = $true
    foreach ($file in $keyFiles) {
        if (Test-Path $file) {
            Write-Host "  ✅ $(Split-Path $file -Leaf)" -ForegroundColor Green
        } else {
            Write-Host "  ❌ $(Split-Path $file -Leaf) - NOT FOUND" -ForegroundColor Red
            $allFilesExist = $false
        }
    }
    
    if (-Not $allFilesExist) {
        Write-Host "`n⚠️  Some files are missing. Running copy script..." -ForegroundColor Yellow
        & ".\copy_to_xampp.ps1"
    }
} else {
    Write-Host "❌ API directory NOT found: $apiPath" -ForegroundColor Red
    Write-Host "Running copy script to create it..." -ForegroundColor Yellow
    & ".\copy_to_xampp.ps1"
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Testing Apache Connection" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Test localhost connection
$testUrls = @(
    "http://localhost/easymechanic/api/index.php",
    "http://127.0.0.1/easymechanic/api/index.php"
)

$apacheWorking = $false
foreach ($url in $testUrls) {
    try {
        $response = Invoke-WebRequest -Uri $url -Method GET -UseBasicParsing -TimeoutSec 3
        Write-Host "✅ $url - Status: $($response.StatusCode)" -ForegroundColor Green
        $apacheWorking = $true
        break
    } catch {
        Write-Host "❌ $url - $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""
if (-Not $apacheWorking) {
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "❌ APACHE IS NOT WORKING!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "SOLUTION:" -ForegroundColor Yellow
    Write-Host "1. Open XAMPP Control Panel" -ForegroundColor White
    Write-Host "2. Check if Apache shows 'Running' (green)" -ForegroundColor White
    Write-Host "3. If not, click 'Start' button" -ForegroundColor White
    Write-Host "4. Wait for it to turn green" -ForegroundColor White
    Write-Host "5. Run this script again to verify" -ForegroundColor White
    Write-Host ""
    Write-Host "If Apache won't start:" -ForegroundColor Yellow
    Write-Host "- Check if port 80 is in use" -ForegroundColor White
    Write-Host "- Check XAMPP error logs" -ForegroundColor White
    Write-Host "- Restart your computer" -ForegroundColor White
} else {
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "✅ APACHE IS WORKING!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Your API should be accessible at:" -ForegroundColor Cyan
    Write-Host "http://localhost/easymechanic/api/index.php" -ForegroundColor White
    Write-Host ""
    Write-Host "For Android device, use your computer's IP:" -ForegroundColor Cyan
    Write-Host "http://YOUR_IP/easymechanic/api/" -ForegroundColor White
    Write-Host ""
    Write-Host "To find your IP, run: ipconfig" -ForegroundColor Yellow
}

Write-Host ""

