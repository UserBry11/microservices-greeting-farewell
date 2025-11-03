pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'userbry/microservices-greeting-farewell'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "🔹 Cloning repository..."
                checkout scm
            }
        }

        stage('Build JARs') {
            steps {
                echo "🔹 Building all Maven projects..."
                sh """
                        cd Eureka-Server && mvn -B clean package -DskipTests && cd ..
                        cd Eureka-ClientA && mvn -B clean package -DskipTests && cd ..
                        cd Eureka-ClientB && mvn -B clean package -DskipTests && cd ..
                    """
            }
        }

        stage('Run Tests (Optional)') {
            steps {
                echo "🧪 Running integration tests... (skipped for now)"
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKERHUB_USER',
                        passwordVariable: 'DOCKERHUB_PASS'
                    )]) {
                        sh """
                            echo "🚀 Logging into Docker Hub..."
                            docker login -u $DOCKERHUB_USER -p $DOCKERHUB_PASS
                            echo "📦 Pushing images..."
                            docker compose push
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build & push complete! Ready for CD pipeline."
        }
        failure {
            echo "❌ Build failed. Check logs in Jenkins console."
        }
    }
}
