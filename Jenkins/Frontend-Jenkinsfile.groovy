pipeline {
    agent none

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
    }

    stages {

        stage('Checkout SRC') {
            agent { label 'docker' }
            steps {
                echo "Checking out Frontend Code"
                git url:'https://github.com/UnnamalaiAnantharaman/Shiftotech-Project.git', branch:'master'
                echo "Checkout Completed"
            }
        }
        
        stage('Frontend-CI/CD') {
            when {
                beforeAgent true
                changeset "frontend/**"
            }
            stages {

                stage('Building Frontend Image') {
                    agent { label 'docker' }
                    steps {
                        dir('frontend') {
                            sh '''
                              echo "Build Started"
                              docker build -t unnadocker/coding-cloud-frontend:${BUILD_NUMBER} .
                              echo "Build Completed"
                            '''
                        }
                        script {
                            docker.withRegistry("https://index.docker.io/v1/", "${DOCKER_CREDENTIALS_ID}") {
                            sh '''
                             echo "Pushing Image to Dockerhub"
                             docker push unnadocker/coding-cloud-frontend:${BUILD_NUMBER}
                             echo "Image Pushed Successfully"
                            '''
                          }
                       }
                    }
                }


                stage('Updating Frontend Deployment') {
                    agent { label 'k8s-master' }
                    steps {
                        sh '''
                          echo "Deployment Update Initiated"
                          kubectl set image deployment frontend-deployment frontend=unnadocker/coding-cloud-frontend:${BUILD_NUMBER} -n coding-cloud
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
                echo "Frontend CI/CD Completed Successfullly"
            }
        }

        failure {
            node('docker') {
                echo "Frontend CI/CD Failed"
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