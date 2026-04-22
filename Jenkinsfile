pipeline {
    agent any

    environment {
        REGISTRY = "kwondeokjae"
    }

    stages {

        stage('Config') {
            steps {
                script {
                    env.ENV = (env.BRANCH_NAME == "dev") ? "dev" : "prod"
                    env.TAG = "${ENV}-${BUILD_NUMBER}"
                }
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Detect Changes') {
            steps {
                script {

                    def changeFiles = currentBuild.changeSets
                        .collectMany { it.items }
                        .collectMany { it.affectedFiles }
                        .collect { it.path }

                    if (changeFiles.isEmpty()) {
                        echo "No changeSets → full build mode"
                        changeFiles = ["__ALL__"]
                    }

                    def isAll = changeFiles.contains("__ALL__")

                    env.GATEWAY_CHANGED   = (isAll || changeFiles.any { it.contains("services/gateway") }).toString()
                    env.SHORTENER_CHANGED = (isAll || changeFiles.any { it.contains("services/shortener") }).toString()
                    env.REDIRECT_CHANGED  = (isAll || changeFiles.any { it.contains("services/redirect") }).toString()
                }
            }
        }

        stage('Build Images') {
            steps {
                script {
                    if (env.GATEWAY_CHANGED == "true") {
                        sh "docker build -t $REGISTRY/tsidly/gateway:${TAG} ./services/gateway"
                    }

                    if (env.SHORTENER_CHANGED == "true") {
                        sh "docker build -t $REGISTRY/tsidly/shortener:${TAG} ./services/shortener"
                    }

                    if (env.REDIRECT_CHANGED == "true") {
                        sh "docker build -t $REGISTRY/tsidly/redirect:${TAG} ./services/redirect"
                    }
                }
            }
        }

        stage('Push Images') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh '''
                            echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        '''

                        if (env.GATEWAY_CHANGED == "true") {
                            sh "docker push $REGISTRY/tsidly/gateway:${TAG}"
                        }
                        if (env.SHORTENER_CHANGED == "true") {
                            sh "docker push $REGISTRY/tsidly/shortener:${TAG}"
                        }
                        if (env.REDIRECT_CHANGED == "true") {
                            sh "docker push $REGISTRY/tsidly/redirect:${TAG}"
                        }

                        sh "docker logout"
                    }
                }
            }
        }
    }
}
