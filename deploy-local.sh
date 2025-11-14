#!/bin/bash
set -e

echo "🌍 Deploying locally using Docker Compose (excluding Jenkins)..."

if [ ! -f docker-compose.yml ]; then
  echo "❌ docker-compose.yml not found!"
  exit 1
fi

# Only bring down the microservices network & containers
docker compose stop eureka-server greeting-service farewell-service rabbitmq || true
docker compose rm -f eureka-server greeting-service farewell-service rabbitmq || true

echo "🔄 Pulling updated images..."
docker compose pull eureka-server greeting-service farewell-service rabbitmq

echo "🚀 Starting microservices..."
docker compose up -d eureka-server greeting-service farewell-service rabbitmq

echo "✅ Local deployment complete!"
docker ps
