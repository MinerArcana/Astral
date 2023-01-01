#!/usr/bin/env groovy

pipeline {
    agent any
    tools {
        jdk "jdk-17.0.1"
    }
    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }
        stage('Build and Deploy') {
            steps {
                echo 'Building and Deploying to Maven'
                script {
                    sh './gradlew build -Pbranch=' + env.BRANCH_NAME + ' uploadArchives'
                }
            }
        }
    }
    post {
        always {
            archive 'build/libs/**.jar'
        }
    }
}
