pipeline {
    agent none

    environment {
        DOCKER_CREDENTIALS_ID  = 'dockerhub-credentials'
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
    }

    stages {

        stage('Checkout SRC') {
            agent { label 'docker' }
            steps {
                echo "Checking out Backend Code"
                git url:'https://github.com/UnnamalaiAnantharaman/Shiftotech-Project.git', branch:'master'
                echo "Checkout Completed"
            }
        }
        
        stage('Backend CI/CD Pipeline') {
            when {
                beforeAgent true
                changeset "backend/**"
            }
            stages {

                stage('Build Backend Image') {
                    agent { label 'docker' } 
                    steps {
                        dir('backend') {
                            sh '''
                              echo "Build Started"
                              docker build -t unnadocker/coding-cloud-backend:${BUILD_NUMBER} .
                              echo 'Build Completed'
                            '''
                        }
                         script {
                            docker.withRegistry("https://index.docker.io/v1/", "${DOCKER_CREDENTIALS_ID}") {
                            sh '''
                             echo "Pushing Image to Dockerhub"
                             docker push unnadocker/coding-cloud-backend:${BUILD_NUMBER}
                             echo "Image Pushed Successfully"
                            '''
                          }
                       }
                    }
                }


                stage('Update Backend Deployment') {
                    agent { label 'k8s-master' }
                    steps {
                        sh '''
                          echo "Deployment Update Initiated"
                          kubectl set image deployment backend-deployment backend=unnadocker/coding-cloud-backend:${BUILD_NUMBER} -n coding-cloud
                          echo "Deployment Update Completed"
                        '''
                    }
                }
            }
        }
    }

    post {

        success {
           node('docker') {
                echo "Backend CI/CD Completed Successfully"
           }
        }

        failure {
            node('docker') {
                echo "Backend CI/CD Failed"
            }
        }

        always {
            node('docker') {
                echo "Cleaning Up Images"
                sh 'docker image prune -a -f'
                cleanWs()
                echo "Cleaning Up Completed"
            }
        }
    }
}