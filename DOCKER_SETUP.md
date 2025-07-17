# Atinka Meds Pharmacy System - Docker Deployment Guide

## Quick Start for Group Members

### Prerequisites

- Docker Desktop installed and running
- Docker Compose (usually comes with Docker Desktop)
- Git (to clone the repository)

### Setup Steps

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd Group37
   ```

2. **Run the setup script**

   ```bash
   # On Linux/Mac
   chmod +x setup-docker.sh
   ./setup-docker.sh

   # On Windows (using Git Bash or WSL)
   bash setup-docker.sh
   ```

3. **Start using the application**
   ```bash
   docker-compose exec atinka-meds java -cp build app.Main
   ```

## Manual Setup (Alternative)

If the setup script doesn't work, follow these manual steps:

1. **Build the Docker image**

   ```bash
   docker-compose build
   ```

2. **Start all services**

   ```bash
   docker-compose up -d
   ```

3. **Run the application**
   ```bash
   docker-compose exec atinka-meds java -cp build app.Main
   ```

## Available Docker Commands

### Running the Application

```bash
# Interactive mode (recommended)
docker-compose exec atinka-meds java -cp build app.Main

# Development mode with debugging
docker-compose run --rm atinka-meds-dev

# One-time run
docker-compose run --rm atinka-meds java -cp build app.Main
```

### Managing Services

```bash
# View running containers
docker-compose ps

# View logs
docker-compose logs -f atinka-meds

# Stop all services
docker-compose down

# Restart services
docker-compose restart

# Rebuild and restart
docker-compose up --build -d
```

### Data Management

```bash
# Create backup
docker-compose run --rm backup-service

# Access container shell
docker-compose exec atinka-meds /bin/bash

# Copy files from container
docker-compose cp atinka-meds:/app/data/drugs.txt ./local-backup/

# Copy files to container
docker-compose cp ./new-data.txt atinka-meds:/app/data/
```

### Development Commands

```bash
# Run with development profile (includes debugging)
docker-compose -f docker-compose.yml run --rm atinka-meds-dev

# Connect debugger (port 5005)
# Use your IDE to connect to localhost:5005

# View database (if needed)
docker-compose exec postgres psql -U pharmacy -d pharmacy_db
```

## Services Overview

The Docker Compose setup includes:

1. **atinka-meds** - Main application service
2. **atinka-meds-dev** - Development service with debugging enabled
3. **postgres** - PostgreSQL database (for future features)
4. **backup-service** - Automated backup service

## Data Persistence

Data is persisted using Docker volumes:

- `pharmacy_data` - Application data files (drugs.txt, customers.txt, etc.)
- `pharmacy_reports` - Generated reports
- `pharmacy_db_data` - Database data

## Network Configuration

All services run on the `pharmacy-network` bridge network with the following ports:

- Application: Internal only (accessed via docker-compose exec)
- Development debugging: 5005 (external)
- PostgreSQL: 5432 (internal)

## Troubleshooting

### Common Issues

1. **Port already in use**

   ```bash
   # Check what's using the port
   netstat -tulpn | grep :5005

   # Stop other services or change port in docker-compose.yml
   ```

2. **Docker daemon not running**

   ```bash
   # Start Docker Desktop or Docker service
   sudo systemctl start docker  # Linux
   ```

3. **Permission denied**

   ```bash
   # On Linux, add user to docker group
   sudo usermod -aG docker $USER
   # Then logout and login again
   ```

4. **Build failures**
   ```bash
   # Clean build
   docker-compose down
   docker system prune -f
   docker-compose build --no-cache
   ```

### Application-Specific Issues

1. **No sample data**

   - The application will create sample data on first run
   - Or copy data files manually to the container

2. **File permissions**

   ```bash
   # Fix permissions in container
   docker-compose exec atinka-meds chmod 664 /app/data/*.txt
   ```

3. **Memory issues**
   ```bash
   # Increase Docker memory limit in Docker Desktop settings
   # Or modify docker-compose.yml memory limits
   ```

## Development Workflow

1. **Make code changes** in your local editor
2. **Rebuild the image** when needed:
   ```bash
   docker-compose build atinka-meds
   ```
3. **Test changes**:
   ```bash
   docker-compose run --rm atinka-meds java -cp build app.Main
   ```

## Team Collaboration

1. **Share environment**: All team members get identical runtime environment
2. **Version control**: Include Docker files in git repository
3. **Data sharing**: Use backup service to share test data
4. **Debugging**: Use development service for collaborative debugging

## Production Considerations

For production deployment:

1. Remove development services
2. Configure proper environment variables
3. Set up proper logging
4. Configure health checks
5. Use secrets management for sensitive data

## Getting Help

1. **Check logs**: `docker-compose logs -f atinka-meds`
2. **Inspect container**: `docker-compose exec atinka-meds /bin/bash`
3. **Test connectivity**: `docker-compose exec atinka-meds ping postgres`

For more help, contact the Group 37 team members or refer to the main README.md file.
