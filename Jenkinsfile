node {
    git 'https://github.com/andersraberg/MiscExamples.git'
    stage('Build') {
        sh './gradlew clean build -Pversion=$BUILD_NUMBER --profile --configuration-cache --build-cache'
	junit '**/build/test-results/test/*.xml'
    }

    stage('Code coverage') {
        sh './gradlew jacocoTestReport -Pversion=$BUILD_NUMBER --info'

	recordCoverage(tools: [[parser: 'JACOCO', pattern: '**/build/reports/jacoco/test/jacocoTestReport.xml']],
            sourceDirectories: [
                [path: 'example-plugin/src/main/java'],
                [path: 'jacorb-plugin/src/main/java']
            ])
    }

    stage('Sonar') {
        withSonarQubeEnv() {
            sh './gradlew sonar -Dsonar.projectKey=MiscExamples -Pversion=$BUILD_NUMBER --configuration-cache --build-cache'
        }
    }

}
