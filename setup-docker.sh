#!/bin/bash

# Atinka Meds Pharmacy System - Docker Setup Script
# Group 37 - Pharmacy Inventory Management System
# ================================================

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Banner
echo -e "${BLUE}"
echo "=================================================="
echo "   ATINKA MEDS PHARMACY INVENTORY SYSTEM"
echo "        Docker Setup Script - Group 37"
echo "=================================================="
echo -e "${NC}"

# Check prerequisites
print_status "Checking prerequisites..."

if ! command_exists docker; then
    print_error "Docker is not installed. Please install Docker first."
    echo "Visit: https://docs.docker.com/get-docker/"
    exit 1
fi

if ! command_exists docker-compose; then
    print_error "Docker Compose is not installed. Please install Docker Compose first."
    echo "Visit: https://docs.docker.com/compose/install/"
    exit 1
fi

print_success "Docker and Docker Compose are installed"

# Check if Docker daemon is running
if ! docker info >/dev/null 2>&1; then
    print_error "Docker daemon is not running. Please start Docker first."
    exit 1
fi

print_success "Docker daemon is running"

# Create necessary directories
print_status "Creating necessary directories..."
mkdir -p logs backups sql/init

# Set appropriate permissions
chmod 755 logs backups

print_success "Directories created successfully"

# Build and start services
print_status "Building Docker images..."
docker-compose build

print_success "Docker images built successfully"

print_status "Starting services..."
docker-compose up -d

print_success "Services started successfully"

# Wait for services to be ready
print_status "Waiting for services to be ready..."
sleep 10

# Show running containers
print_status "Current running containers:"
docker-compose ps

echo -e "\n${GREEN}=================================================="
echo "   SETUP COMPLETED SUCCESSFULLY!"
echo "==================================================${NC}"

echo -e "\n${BLUE}Available Commands:${NC}"
echo "1. Run the main application:"
echo "   docker-compose exec atinka-meds java -cp build app.Main"
echo ""
echo "2. Run in development mode:"
echo "   docker-compose run --rm atinka-meds-dev"
echo ""
echo "3. View logs:"
echo "   docker-compose logs -f atinka-meds"
echo ""
echo "4. Stop all services:"
echo "   docker-compose down"
echo ""
echo "5. Create backup:"
echo "   docker-compose run --rm backup-service"
echo ""
echo "6. Access container shell:"
echo "   docker-compose exec atinka-meds /bin/bash"

echo -e "\n${BLUE}Data Persistence:${NC}"
echo "- Application data: Docker volume 'pharmacy_data'"
echo "- Reports: Docker volume 'pharmacy_reports'"
echo "- Database: Docker volume 'pharmacy_db_data'"

echo -e "\n${BLUE}Accessing the Application:${NC}"
echo "The application will start automatically in the container."
echo "To interact with it, use:"
echo "docker-compose exec atinka-meds java -cp build app.Main"

echo -e "\n${YELLOW}Note:${NC} The application is designed for interactive terminal use."
echo "Make sure to allocate a TTY when running commands."

echo -e "\n${GREEN}Happy testing! 🏥💊${NC}"
