pipeline {

    /************************************************************
     * Build Parameters (toggle where this pipeline deploys)
     ************************************************************/
    parameters {
        booleanParam(name: 'DEPLOY_LOCAL', defaultValue: false, description: 'Deploy to local Docker Compose environment')
        booleanParam(name: 'DEPLOY_EC2', defaultValue: false, description: 'Deploy to AWS EC2 instance')
        booleanParam(name: 'DEPLOY_ECS', defaultValue: false, description: 'Deploy to AWS ECS cluster')
    }

    agent any

    /************************************************************
     * Global Environment Variables
     ************************************************************/
    environment {
        DOCKER_HUB_REPO = 'userbry/microservices-greeting-farewell'
        SERVICES = "Eureka-Server Eureka-ClientA Eureka-ClientB"
        VERSION = "v${env.BUILD_NUMBER}"
    }

    stages {

        /************************************************************
         * PRE-CHECK (tools, docker socket, environment validation)
         ************************************************************/
        stage('Pre-Check Environment') {
            steps {
                echo '🧰 Checking environment setup...'
                sh '''
                echo "🔹 Verifying installed tools..."

                if ! command -v git &> /dev/null; then
                  echo "❌ Git is not installed." >&2; exit 1
                fi

                if ! command -v mvn &> /dev/null; then
                  echo "❌ Maven is not installed." >&2; exit 1
                fi

                if ! command -v docker &> /dev/null; then
                  echo "❌ Docker CLI missing." >&2; exit 1
                fi

                if [ ! -S /var/run/docker.sock ]; then
                  echo "❌ Docker socket not mounted! Mount it in docker-compose.yml" >&2; exit 1
                fi

                echo "✅ Environment OK — ready to build."
                '''
            }
        }

        /************************************************************
         * CHECKOUT
         ************************************************************/
        stage('Checkout') {
            steps {
                echo "🔹 Cloning repository..."
                checkout scm
            }
        }

        /************************************************************
         * MAVEN BUILD (produce JAR artifacts for all microservices)
         ************************************************************/
        stage('Build JARs with Maven') {
            steps {
                echo "🏗️ Building microservices..."
                sh '''
                for service in $SERVICES; do
                  echo "➡️ Building $service ..."
                  cd $service
                  mvn -B clean package -DskipTests
                  cd ..
                done
                '''
            }
        }

        /************************************************************
         * DOCKER IMAGE BUILD & TAG
         ************************************************************/
        stage('Build & Tag Docker Images') {
            steps {
                echo "🐳 Creating Docker images..."
                sh '''
                for service in $SERVICES; do
                  echo "📦 Building Docker image for $service..."
                  docker build -t $DOCKER_HUB_REPO:$service-latest $service
                done
                '''
            }
        }

        /************************************************************
         * PUSH IMAGES TO DOCKER HUB
         ************************************************************/
        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKERHUB_USER',
                        passwordVariable: 'DOCKERHUB_PASS'
                    )]) {
                        sh '''
                        echo "🚀 Logging into Docker Hub..."
                        echo "$DOCKERHUB_PASS" | docker login -u "$DOCKERHUB_USER" --password-stdin
                        
                        for service in $SERVICES; do
                          echo "📤 Pushing image: $DOCKER_HUB_REPO:$service-latest"
                          docker push $DOCKER_HUB_REPO:$service-latest
                        done
                        '''
                    }
                }
            }
        }

        /************************************************************
         * DEPLOY TO LOCAL DOCKER COMPOSE
         ************************************************************/
        stage('Deploy to Local Docker Compose') {
            when { expression { return params.DEPLOY_LOCAL } }
            steps {
                echo "🌍 Deploying via Docker Compose..."
                sh 'chmod +x deploy-local.sh'
                sh './deploy-local.sh'
            }
        }

        /************************************************************
         * DEPLOY TO EC2 (SSH → git pull → docker compose up)
         ************************************************************/
        // Used for running virtual servers in the cloud to host websites, aplications, and services.
        //Allows businesses to scale computing power up or down as needed.
        //Key uses: web hosting, high-performance computing for Big Data and AI, application development, and data backup and recovery
        //It provides scalable, secure, and customizable computing capacity that users can manage remotely from anywhere in the world.
        //Machine learning and AI: Training and running AI applications, including chatbots and deep learning models
        stage('Deploy to EC2') {
            when { expression { return params.DEPLOY_EC2 } }
            steps {
                echo "☁️ Deploying to EC2 instance..."
                sh 'chmod +x deploy-ec2.sh'
                sh './deploy-ec2.sh'
            }
        }

        /************************************************************
         * DEPLOY TO AWS ECS (rolling update)
         ************************************************************/
        stage('Deploy to AWS ECS') {
            when { expression { return params.DEPLOY_ECS } }
            steps {
                echo "🚀 Triggering ECS rolling deployment..."
                sh 'chmod +x deploy-ecs.sh'
                sh './deploy-ecs.sh'
            }
        }
    }

    /************************************************************
     * POST CONDITIONS (success/failure messages)
     ************************************************************/
    post {
        success {
            echo "✅ Pipeline completed successfully!"
        }
        failure {
            echo "❌ Pipeline failed — investigate logs above."
        }
    }
}
