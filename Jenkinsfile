node {
    git 'https://github.com/andersraberg/MiscExamples.git'
    stage('Build') {
        sh './gradlew clean build -Pversion=$BUILD_NUMBER --profile --configuration-cache'
    }

    stage('Code coverage') {
        sh './gradlew jacocoTestReport -Pversion=$BUILD_NUMBER --info'
        jacoco( 
            execPattern: '**/build/jacoco/*.exec',
        )
    }

    stage('Sonar') {
        withSonarQubeEnv() {
            sh './gradlew sonar -Dsonar.projectKey=MiscExamples -Pversion=$BUILD_NUMBER'
        }
    }

//    stage('Report') {
//        junit '*/build/test-results/**/*.xml'
//        sh 'mv */build/reports/profile/*.html build/reports/profile/index.html'
//        publishHTML([allowMissing: true,
//                     alwaysLinkToLastBuild: false,
//                     keepAll: true,
//                     reportDir: 'build/reports/profile/',
//                     reportFiles: 'index.html',
//                     reportName: 'Gradle profile',
//                     reportTitles: ''])
//    }
    
}
