pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''cd MiscExamples
./gradlew build
'''
      }
    }
  }
}