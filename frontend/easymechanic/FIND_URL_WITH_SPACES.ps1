# Script to find URLs with spaces in Kotlin files
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Finding URLs with spaces..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$found = $false
Get-ChildItem -Path "app\src\main\java" -Recurse -Filter "*.kt" | ForEach-Object {
    $content = Get-Content $_.FullName -Raw
    $lines = Get-Content $_.FullName
    
    for ($i = 0; $i -lt $lines.Count; $i++) {
        $line = $lines[$i]
        if ($line -match 'http://\s+[\d.]') {
            Write-Host "❌ FOUND SPACE IN URL:" -ForegroundColor Red
            Write-Host "File: $($_.FullName)" -ForegroundColor Yellow
            Write-Host "Line $($i + 1): $line" -ForegroundColor White
            Write-Host ""
            $found = $true
        }
    }
}

if (-Not $found) {
    Write-Host "✅ No URLs with spaces found in visible files" -ForegroundColor Green
    Write-Host ""
    Write-Host "However, the error mentions 'RetrofitClient.kt' which might be:" -ForegroundColor Yellow
    Write-Host "1. A file you created separately" -ForegroundColor White
    Write-Host "2. A generated file" -ForegroundColor White
    Write-Host "3. In a different location" -ForegroundColor White
    Write-Host ""
    Write-Host "MANUAL CHECK REQUIRED:" -ForegroundColor Cyan
    Write-Host "Search your entire project for 'RetrofitClient' and check for spaces in URLs" -ForegroundColor White
}

Write-Host ""

