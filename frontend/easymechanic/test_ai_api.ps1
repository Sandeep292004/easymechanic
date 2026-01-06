# Test AI Troubleshooting API
Write-Host "`nTesting AI Troubleshooting API...`n" -ForegroundColor Cyan

$baseUrl = "http://localhost/easymechanic/api/ai/troubleshoot.php"

$testProblems = @(
    "break failure",
    "engine not starting",
    "battery dead",
    "overheating",
    "tire puncture"
)

foreach ($problem in $testProblems) {
    Write-Host "Testing: '$problem'" -ForegroundColor Yellow
    
    $body = @{
        problem_description = $problem
    } | ConvertTo-Json
    
    try {
        $response = Invoke-RestMethod -Uri $baseUrl -Method POST -Body $body -ContentType "application/json"
        
        if ($response.success) {
            Write-Host "  Issue Type: $($response.data.issue_type)" -ForegroundColor Green
            Write-Host "  Solutions: $($response.data.step_by_step_solution.Count)" -ForegroundColor Green
            Write-Host "  First Solution: $($response.data.step_by_step_solution[0])" -ForegroundColor Gray
        } else {
            Write-Host "  Error: $($response.message)" -ForegroundColor Red
        }
    } catch {
        Write-Host "  Failed: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    Write-Host ""
}

Write-Host "Test completed!`n" -ForegroundColor Cyan

