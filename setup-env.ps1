# Setup Environment Variables for Auth Service
# Run this script in PowerShell to set the required environment variables

Write-Host "Setting up environment variables for Auth Service..." -ForegroundColor Green

# Email Configuration
$env:EMAIL_USERNAME = "bhagwanravi97@gmail.com"
$env:EMAIL_PASSWORD = Read-Host "Enter your Gmail App Password" -MaskInput
$env:EMAIL_FROM_ID = "bhagwanravi97@gmail.com"
$env:EMAIL_NOTIFICATION_EMAIL = "bhagwanravi97@gmail.com"
$env:EMAIL_NOTIFICATION_PASSWORD = $env:EMAIL_PASSWORD

# JWT Configuration - Generate a random secret
$jwtSecret = -join ((48..57) + (65..90) + (97..122) | Get-Random -Count 64 | ForEach-Object {[char]$_})
$env:JWT_SECRET = $jwtSecret

# Optional configurations
$env:EUREKA_SERVER_URL = "http://localhost:8761/eureka/"
$env:EUREKA_REGISTER = "true"
$env:EUREKA_FETCH = "true"
$env:HOSTNAME = "localhost"

Write-Host "Environment variables set successfully!" -ForegroundColor Green
Write-Host "JWT Secret generated: $jwtSecret" -ForegroundColor Yellow
Write-Host "Save this JWT secret securely!" -ForegroundColor Red
Write-Host ""
Write-Host "To make these variables permanent, add them to your system environment variables." -ForegroundColor Cyan
Write-Host "Or create a .env file and use a tool like dotenv to load them." -ForegroundColor Cyan
