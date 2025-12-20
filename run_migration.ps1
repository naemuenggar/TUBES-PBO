# PowerShell script to run database migration
# This will add the 'role' column to user table and create demo users

Write-Host "Running MoneyMate Database Migration..." -ForegroundColor Cyan
Write-Host ""

# MySQL connection details (adjust if needed)
$mysqlUser = "root"
$mysqlPassword = ""  # Empty password for root
$mysqlHost = "localhost"
$mysqlPort = "3306"
$migrationFile = "database\update_add_role.sql"

# Check if migration file exists
if (-not (Test-Path $migrationFile)) {
    Write-Host "ERROR: Migration file not found at $migrationFile" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Migration file found: $migrationFile" -ForegroundColor Green

# Try to run migration using mysql command
try {
    Write-Host ""
    Write-Host "Executing SQL migration..." -ForegroundColor Yellow
    
    # Run mysql command
    if ($mysqlPassword -eq "") {
        # No password
        & mysql -u $mysqlUser -h $mysqlHost -P $mysqlPort < $migrationFile 2>&1
    }
    else {
        # With password
        & mysql -u $mysqlUser -p$mysqlPassword -h $mysqlHost -P $mysqlPort < $migrationFile 2>&1
    }
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✅ Migration completed successfully!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Database changes:" -ForegroundColor Cyan
        Write-Host "  - Added 'role' column to user table" -ForegroundColor White
        Write-Host "  - Created admin user: admin@moneymate.com / admin123" -ForegroundColor White
        Write-Host "  - Created demo user: user@moneymate.com / user123" -ForegroundColor White
        Write-Host ""
        Write-Host "You can now register new users or login with the demo accounts!" -ForegroundColor Yellow
    }
    else {
        Write-Host ""
        Write-Host "⚠️  Migration may have failed. Check the output above." -ForegroundColor Yellow
        Write-Host "You may need to run the SQL file manually in MySQL Workbench." -ForegroundColor Yellow
    }
}
catch {
    Write-Host ""
    Write-Host "ERROR: Could not execute mysql command" -ForegroundColor Red
    Write-Host "Error details: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please run the migration manually:" -ForegroundColor Yellow
    Write-Host "  1. Open MySQL Workbench" -ForegroundColor White
    Write-Host "  2. Execute file: $migrationFile" -ForegroundColor White
}

Write-Host ""
Read-Host "Press Enter to close"
