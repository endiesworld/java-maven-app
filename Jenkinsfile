#!/user/bin/env groovy

@Library('jenkins-shared-library')
// The below is already configured in jenkins system
// library identifier: 'jenkins-shared-library@main', retriever: modernSCM([
//     $class: 'GitSCMSource',              // use Git source
//     id: 'jenkins-shared-library',        // unique ID for tracking
//     remote: 'https://github.com/endiesworld/jenkins-shared-library.git',
//     credentialsId: 'github-PAT',         // your GitHub token in Jenkins
// ])


def gv

pipeline{
	agent any
	tools{
		maven 'maven-3.9.11'
	}
	stages{
        stage("init"){
			steps{
				script{
					gv = load "script.groovy"
					sh 'mvn build-helper:parse-version versions:set \
					-DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
					versions:commit'
					def matcher = readFile('pom.xml') =~ '<version>(.+?)</version>'
					def version = matcher ? matcher[0][1] : 'unknown'
					env.IMAGE_NAME = "$version-$BUILD_NUMBER"
				}
				
			}
		}
		stage("build jar"){
			steps{
				script{
					sh 'mvn clean package'
					buildJar()
				}
				
			}
		}
		stage("build image"){
			steps{
				script{
					buildImage "okoro/demo-java-app:java-mav-${env.IMAGE_NAME}"
					dockerLogin()
					dockerPush "okoro/demo-java-app:java-mav-${env.IMAGE_NAME}"
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
		stage("commit version update"){
			steps{
                    withCredentials([
                    usernamePassword(credentialsId: 'github-PAT', 
                                     passwordVariable: 'PASS', 
                                     usernameVariable: 'USER') 
                ]) {
					sh 'git config --global user.email "jenkins@example"'
					sh 'git config --global user.name "Jenkins CI"'
					sh 'git status'
					sh 'git branch'
					sh 'git config --list'
                    sh "git remote set-url origin https://${USER}:${PASS}@github.com/endiesworld/java-maven-app.git"
					sh 'git add .'
					sh 'git commit -m "CI: Update version in pom.xml file"'
					sh 'git push origin HEAD:refs/heads/jenkins-jobs'
					
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
