# PowerShell script for Windows users
# Atinka Meds Pharmacy System - Docker Setup

Write-Host "=================================================" -ForegroundColor Blue
Write-Host "   ATINKA MEDS PHARMACY INVENTORY SYSTEM" -ForegroundColor Blue
Write-Host "      Docker Setup Script - Group 37" -ForegroundColor Blue
Write-Host "=================================================" -ForegroundColor Blue
Write-Host ""

# Check if Docker is installed
Write-Host "Checking prerequisites..." -ForegroundColor Yellow

try {
    $dockerVersion = docker --version
    Write-Host "✓ Docker found: $dockerVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker not found. Please install Docker Desktop first." -ForegroundColor Red
    Write-Host "Download from: https://docs.docker.com/desktop/windows/" -ForegroundColor Cyan
    exit 1
}

try {
    $composeVersion = docker-compose --version
    Write-Host "✓ Docker Compose found: $composeVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker Compose not found. Please install Docker Compose." -ForegroundColor Red
    exit 1
}

# Check if Docker is running
try {
    docker info | Out-Null
    Write-Host "✓ Docker daemon is running" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker daemon is not running. Please start Docker Desktop." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Creating necessary directories..." -ForegroundColor Yellow

# Create directories
$directories = @("logs", "backups", "sql\init")
foreach ($dir in $directories) {
    if (!(Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
        Write-Host "✓ Created directory: $dir" -ForegroundColor Green
    } else {
        Write-Host "✓ Directory already exists: $dir" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "Building Docker images..." -ForegroundColor Yellow
docker-compose build

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Docker images built successfully" -ForegroundColor Green
} else {
    Write-Host "✗ Failed to build Docker images" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Starting services..." -ForegroundColor Yellow
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Services started successfully" -ForegroundColor Green
} else {
    Write-Host "✗ Failed to start services" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Waiting for services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host ""
Write-Host "Current running containers:" -ForegroundColor Yellow
docker-compose ps

Write-Host ""
Write-Host "=================================================" -ForegroundColor Green
Write-Host "   SETUP COMPLETED SUCCESSFULLY!" -ForegroundColor Green
Write-Host "=================================================" -ForegroundColor Green

Write-Host ""
Write-Host "Available Commands:" -ForegroundColor Cyan
Write-Host "1. Run the main application:" -ForegroundColor White
Write-Host "   docker-compose exec atinka-meds java -cp build app.Main" -ForegroundColor Gray
Write-Host ""
Write-Host "2. Run in development mode:" -ForegroundColor White
Write-Host "   docker-compose run --rm atinka-meds-dev" -ForegroundColor Gray
Write-Host ""
Write-Host "3. View logs:" -ForegroundColor White
Write-Host "   docker-compose logs -f atinka-meds" -ForegroundColor Gray
Write-Host ""
Write-Host "4. Stop all services:" -ForegroundColor White
Write-Host "   docker-compose down" -ForegroundColor Gray
Write-Host ""
Write-Host "5. Create backup:" -ForegroundColor White
Write-Host "   docker-compose run --rm backup-service" -ForegroundColor Gray
Write-Host ""
Write-Host "6. Access container shell:" -ForegroundColor White
Write-Host "   docker-compose exec atinka-meds bash" -ForegroundColor Gray

Write-Host ""
Write-Host "Happy testing! 🏥💊" -ForegroundColor Green
