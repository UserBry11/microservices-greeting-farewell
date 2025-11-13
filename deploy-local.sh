#!/bin/bash
set -e

echo "🌍 Deploying locally using Docker Compose..."

if [ ! -f docker-compose.yml ]; then
  echo "❌ docker-compose.yml not found!"
  exit 1
fi

docker compose down
docker compose pull
docker compose up -d

echo "✅ Local deployment complete!"
docker ps
