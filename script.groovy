def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t okoro/demo-java-app:java-mav-1.3 .'
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push okoro/demo-java-app:java-mav-1.2'
    }
}

def deployApp() {
    echo 'deploying the application...'
}

return this
