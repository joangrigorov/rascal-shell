node {
  def mvnHome = tool 'M3'
  env.JAVA_HOME="${tool 'jdk-oracle-8'}"
  env.PATH="${env.JAVA_HOME}/bin:${mvnHome}/bin:${env.PATH}"

  try {
	  stage 'Clone'
	  checkout scm
	
	  stage 'Build and Test'
	  sh "mvn -B clean install"
	
	  stage 'Deploy'
	  sh "mvn -s ${env.HOME}/usethesource-maven-settings.xml -B deploy"
	  
	  build job: '../rascal-eclipse/master', wait: false
  } catch(e) {
	  slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
      throw e
  }  
}


