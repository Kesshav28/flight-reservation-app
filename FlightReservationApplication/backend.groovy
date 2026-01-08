pipeline{
    agent any
    stages{
        stage("Code-pull") {
           steps {
                git branch: 'main', url: 'https://github.com/Kesshav28/flight-reservation-app.git'
           } 
        }
        stage("Code-Build") {
            steps {
                sh '''
                cd  FlightReservationApplication
                mvn clean package
                '''
            }
        }
        stage("QA-Test") {
            steps {
                withSonarQubeEnv(installationName:'sonar', credentialsId: 'Sonar-token') {
                    sh '''
                    cd  FlightReservationApplication
                    mvn sonar:sonar -Dsonar.projectKey=flight-reservation
                    '''
                }
            }
        }
        stage("Docker-build") {
            steps {
                sh '''
                cd  FlightReservationApplication
                docker build -t kesshav28/flightreservation-new:latest .
                docker push kesshav28/flightreservation-new:latest
                docker rmi 'docker image list -aq'
                '''
            }
        }
        stage ("Deploy") {
            steps {
                sh '''
                cd FlightReservationApplication
                kubectl apply -f k8s/
                '''
            }
        }
    }
}