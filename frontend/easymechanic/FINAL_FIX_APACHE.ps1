# Final Apache Fix Script
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "FINAL APACHE FIX" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Diagnosing Apache issue..." -ForegroundColor Yellow
Write-Host ""

# Check if XAMPP dashboard works
Write-Host "1. Testing XAMPP dashboard..." -ForegroundColor Yellow
try {
    $dashboard = Invoke-WebRequest -Uri "http://localhost" -Method GET -UseBasicParsing -TimeoutSec 3
    Write-Host "   ✅ XAMPP dashboard is accessible" -ForegroundColor Green
    Write-Host "   This means Apache IS running and serving files" -ForegroundColor Green
    Write-Host ""
    Write-Host "2. The issue is likely with the URL path..." -ForegroundColor Yellow
    Write-Host "   Testing different URL formats..." -ForegroundColor Yellow
    
    $testUrls = @(
        "http://localhost/easymechanic/api/test_connection.php",
        "http://localhost/easymechanic/api/index.php",
        "http://127.0.0.1/easymechanic/api/test_connection.php"
    )
    
    $found = $false
    foreach ($url in $testUrls) {
        try {
            $r = Invoke-WebRequest -Uri $url -Method GET -UseBasicParsing -TimeoutSec 3
            Write-Host "   ✅ $url - WORKS!" -ForegroundColor Green
            Write-Host "   Response: $($r.Content.Substring(0, [Math]::Min(100, $r.Content.Length)))" -ForegroundColor Gray
            $found = $true
            break
        } catch {
            # Continue testing
        }
    }
    
    if (-Not $found) {
        Write-Host "   ❌ All URLs failed" -ForegroundColor Red
        Write-Host ""
        Write-Host "3. Checking file structure..." -ForegroundColor Yellow
        if (Test-Path "C:\xampp\htdocs\easymechanic\api\test_connection.php") {
            Write-Host "   ✅ Files exist in correct location" -ForegroundColor Green
            Write-Host ""
            Write-Host "4. Possible solutions:" -ForegroundColor Yellow
            Write-Host "   a) Apache needs .htaccess support enabled" -ForegroundColor White
            Write-Host "   b) Directory permissions issue" -ForegroundColor White
            Write-Host "   c) Apache virtual host configuration" -ForegroundColor White
            Write-Host ""
            Write-Host "   Try accessing directly via file path:" -ForegroundColor Cyan
            Write-Host "   http://localhost/easymechanic/api/test_connection.php" -ForegroundColor White
        } else {
            Write-Host "   ❌ Files not in correct location" -ForegroundColor Red
            Write-Host "   Running copy script..." -ForegroundColor Yellow
            & ".\copy_to_xampp.ps1"
        }
    }
    
} catch {
    Write-Host "   ❌ XAMPP dashboard NOT accessible" -ForegroundColor Red
    Write-Host "   This means Apache is NOT running correctly" -ForegroundColor Red
    Write-Host ""
    Write-Host "SOLUTION:" -ForegroundColor Yellow
    Write-Host "1. Open XAMPP Control Panel" -ForegroundColor White
    Write-Host "2. Stop Apache (if running)" -ForegroundColor White
    Write-Host "3. Wait 5 seconds" -ForegroundColor White
    Write-Host "4. Start Apache again" -ForegroundColor White
    Write-Host "5. Wait for green 'Running' status" -ForegroundColor White
    Write-Host "6. Run this script again" -ForegroundColor White
}

Write-Host ""

