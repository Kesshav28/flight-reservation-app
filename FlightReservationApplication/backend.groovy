pipeline    {
    agent any
    stages{
        stage('Code-pull') {
            steps {
                git branch: 'main', url: 'https://github.com/Kesshav28/flight-reservation-app.git'
            }
        }
    }
}