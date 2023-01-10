pipeline {
    environment {
        PATH = "$PATH:/usr/local/bin"
    }
    tools {
       maven 'Maven2'
    }
    agent any
    stages {
        stage('Build') {
            steps {
              sh 'mvn clean install'
              dir("${env.WORKSPACE}/restful-api") {
                  sh 'mvn package -Dspring.profiles.active=docker'
              }
            }
        }
//         stage('Create Network') {
//             steps {
//                sh 'docker network create vas-network'
//             }
//         }
        stage('Deploy') {
            steps {
                dir("${env.WORKSPACE}") {
                     sh 'docker-compose down'
                     sh 'docker-compose up --build -d'
                 }
            }
        }
    }
}
