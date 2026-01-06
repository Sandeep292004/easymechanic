# PowerShell Script to Copy EASY MECHANIC API to XAMPP
# Run this script from the project root directory

$sourceDir = "C:\Users\Sandeep\AndroidStudioProjects\easymechanic\api"
$targetDir = "C:\xampp\htdocs\easymechanic\api"

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "EASY MECHANIC API - Copy to XAMPP" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

# Check if source directory exists
if (-Not (Test-Path $sourceDir)) {
    Write-Host "ERROR: Source directory not found: $sourceDir" -ForegroundColor Red
    exit 1
}

# Create target directory if it doesn't exist
if (-Not (Test-Path $targetDir)) {
    Write-Host "Creating target directory: $targetDir" -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
}

Write-Host "Source: $sourceDir" -ForegroundColor Green
Write-Host "Target: $targetDir" -ForegroundColor Green
Write-Host ""

# Copy all files and folders recursively
Write-Host "Copying files..." -ForegroundColor Yellow
Copy-Item -Path "$sourceDir\*" -Destination $targetDir -Recurse -Force

Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Copy completed successfully!" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Start Apache and MySQL in XAMPP" -ForegroundColor White
Write-Host "2. Import database schema from: $targetDir\database\schema.sql" -ForegroundColor White
Write-Host "3. Test API: http://localhost/easymechanic/api/index.php" -ForegroundColor White
Write-Host ""

