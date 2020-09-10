pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "maven3"
    }

    stages {
        stage('Compile') {
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/re-blank/Project1-Lawrence.git'

                // Run Maven on a Unix agent.
                sh "mvn clean compile"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }
            post{
                failure{
                    error "Failed to compile."
                }
            }
        }
        stage('Test')
        {
            steps {
                // Run Maven on a Unix agent.
                sh "mvn -Dmaven.test.failure.ignore=false test"
            }
            post{
                failure{
                    error "One or more tests failed."
                }
            }
        }
        stage('Package')
        {
            steps {
                sh "mvn -DskipTests=true package"
            }
            post{
                failure{
                    error "Failed to package the project"
                }
            }
        }
    }
    post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
                
                
        }
}

