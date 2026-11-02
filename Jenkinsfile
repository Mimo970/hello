pipeline {
    agent any
    environment {
        IMAGE = 'adr9702297/hello'
        TAG   = "${BUILD_NUMBER}"
        EC2   = 'ubuntu@ec2-3-139-233-44.us-east-2.compute.amazonaws.com'
        PORT  = '8080'
    }
    stages {
        stage('Docker Build') {
            steps { bat "docker build -t %IMAGE%:%TAG% -t %IMAGE%:latest ." }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub',
                        usernameVariable: 'DH_USER', passwordVariable: 'DH_TOKEN')]) {
                    bat 'docker login -u %DH_USER% -p %DH_TOKEN%'
                    bat 'docker push %IMAGE%:%TAG%'
                    bat 'docker push %IMAGE%:latest'
                }
            }
        }
        stage('Deploy to EC2') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh', keyFileVariable: 'KEY')]) {
                    bat 'icacls "%KEY%" /inheritance:r /grant:r "SYSTEM:(R)"'
                    bat 'ssh -i "%KEY%" -o StrictHostKeyChecking=no %EC2% "docker pull %IMAGE%:latest; docker rm -f hello || true; docker run -d --name hello --restart unless-stopped -p %PORT%:%PORT% %IMAGE%:latest"'
                }
            }
        }
    }
    post {
        success { echo "Deployed: http://ec2-3-139-233-44.us-east-2.compute.amazonaws.com:${PORT}" }
        always  { bat 'docker logout' }
    }
}