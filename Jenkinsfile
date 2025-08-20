def gv

pipeline{
	agent any
	tools{
		maven 'maven-3.9.11'
	}
	stages{
		stage("test"){
			steps{
				script{
					echo "Testing the application..."
					echo "Executing pipeline for $BRANCH_NAME"
				}
				
			}
		}
		stage("build"){
			when{
				expression{
					BRANCH_NAME == "main"
				}
			}
			steps{
				script{
					echo "building the the docker image..."
					echo "building the the docker image..."
					
				}
				
			}
		}
		stage("deploy"){
			when{
				expression{
					BRANCH_NAME == "main"
				}
			}
			steps{
				script{
					echo "deploying the application..."
				}
			}
		}
	}
}
