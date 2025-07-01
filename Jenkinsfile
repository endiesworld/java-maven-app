#!/user/bin/env groovy

// @Library('jenkins-shared-library')
library identifier: 'jenkins-shared-library@main', retriever: modernSCM([
    $class: 'GitSCMSource',              // use Git source
    id: 'jenkins-shared-library',        // unique ID for tracking
    remote: 'https://github.com/endiesworld/jenkins-shared-library.git',
    credentialsId: 'github-PAT',         // your GitHub token in Jenkins
    traits: [
        [$class: 'jenkins.plugins.git.traits.BranchDiscoveryTrait']  // This is the fix!
    ]
])

// Test to auto trigger a build
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
					buildImage 'okoro/demo-java-app:java-mav-2.3'
					dockerLogin()
					dockerPush 'okoro/demo-java-app:java-mav-2.3'
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