#!/user/bin/env groovy

@Library('jenkins-shared-library')

def gv

pipeline{
	agent any
	tools{
		maven 'maven-3.9'
	}
	stages{
        stage("init"){
			steps{
				script{
					gv = load "script.groovy"
				}
				
			}
		}
		stage("build jar"){
			steps{
				script{
					buildJar()
				}
				
			}
		}
		stage("build image"){
			steps{
				script{
					buildImage 'okoro/demo-java-app:java-mav-2.2'
					dockerLogin()
					dockerPush 'okoro/demo-java-app:java-mav-2.2'
				}

			}
		}
		stage("deploy"){
			steps{
				script{
					gv.deployApp()
				}
			}
		}
	}
}


//Added this update to force a change in the Jenkinsfile to trigger a build
// pipeline{
// 	agent any
// 	tools{
// 		maven 'maven-3.9'
// 	}
// 	stages{
// 		stage("test"){
// 			steps{
// 				script{
// 					echo "Testing the application..."
// 					echo "Executing pipeline for $BRANCH_NAME"
// 				}
				
// 			}
// 		}
// 		stage("build"){
// 			when{
// 				expression{
// 					BRANCH_NAME == "main"
// 				}
// 			}
// 			steps{
// 				script{
// 					echo "building the the docker image..."
					
// 				}
				
// 			}
// 		}
// 		stage("deploy"){
// 			when{
// 				expression{
// 					BRANCH_NAME == "main"
// 				}
// 			}
// 			steps{
// 				script{
// 					echo "deploying the application..."
// 				}
// 			}
// 		}
// 	}
// }