# Multi-stage build for Atinka Meds Pharmacy Inventory System
# Stage 1: Build the application
FROM openjdk:11-jdk-slim AS builder

# Set working directory
WORKDIR /app

# Copy source code
COPY src/ ./src/
COPY data/ ./data/

# Create build directory
RUN mkdir -p build

# Compile the Java application
RUN javac -cp . src/**/*.java -d build

# Stage 2: Runtime environment
FROM openjdk:11-jre-slim

# Install necessary packages for better terminal experience
RUN apt-get update && apt-get install -y \
    procps \
    vim \
    less \
    && rm -rf /var/lib/apt/lists/*

# Create app user for security
RUN useradd -m -u 1000 pharmacist && \
    mkdir -p /app/data /app/reports && \
    chown -R pharmacist:pharmacist /app

# Set working directory
WORKDIR /app

# Copy compiled classes from builder stage
COPY --from=builder /app/build ./build
COPY --from=builder /app/data ./data

# Copy additional files
COPY reports/ ./reports/
COPY README.md ./
COPY .dockerignore ./

# Ensure data and reports directories have correct permissions
RUN chown -R pharmacist:pharmacist /app

# Switch to app user
USER pharmacist

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV APP_HOME="/app"

# Expose port (though this is a CLI app, useful for future web interface)
EXPOSE 8080

# Health check to ensure Java is working
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD java -version || exit 1

# Default command to run the application
CMD ["java", "-cp", "build", "app.Main"]
