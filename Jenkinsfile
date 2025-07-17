node {
    git 'https://github.com/andersraberg/MiscExamples.git'
    stage('Build') {
        sh './gradlew clean build -Pversion=$BUILD_NUMBER --profile --configuration-cache --build-cache'
    }

    stage('Code coverage') {
        sh './gradlew jacocoTestReport -Pversion=$BUILD_NUMBER --info'

        recordCoverage tools: [
            [parser: 'JACOCO', pattern: '**/build/reports/jacoco/test/jacocoTestReport.xml'] 
        ]	

    }

    stage('Sonar') {
        withSonarQubeEnv() {
            sh './gradlew sonar -Dsonar.projectKey=MiscExamples -Pversion=$BUILD_NUMBER --configuration-cache --build-cache'
        }
    }

}
