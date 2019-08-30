//Jenkinsfile两种写法：Declarative pipeline和Scripted pipeline
//区别：https://www.cnblogs.com/YatHo/p/7856556.html
//此demo是Scripted pipeline 写法
node {
 	// 做任何操作前清除工作空间
    deleteDir()

     //定义变量
    //def GRADLE_HOME = tool 'M3'

    currentBuild.result = "SUCCESS"
    env.BUILD_MSG = ''

    try {
        stage ('Clone') {
            sh "echo '========开始克隆代码========'"
        	checkout scm
        }

        stage ('Tests') {

                if (isUnix()){
                    sh ./gradlew test
                  }else{
                    bat gradlew.bat test
                  }

            }

        stage ('Build') {
        	sh "echo '=======开始构建代码======='"
        	//这里没有配置gradle环境变量,因为如果不存在gradle,那么执行gradlew 会自动下载项目中gradle-wrapper.properties内配置的gradle,并解压生效
        	if (isUnix()){
        	    sh ./gradlew build
        	}else{
        	    bat gradlew.bat build
        	}
        }

      	stage ('Deploy') {
            sh "echo '=======将构建后的代码部署到服务器上======='"
            sh "cd target/libs/"
      	}
    } catch (err) {
        currentBuild.result = 'FAILED'
        throw err
    } finally{
        sendEmail()
    }
}


def sendEmail() {
  return {
    mail body: "project build ${currentBuild.result} is here: ${env.BUILD_URL}" ,
        from: "${env.EMAIL_REPO}",
        replyTo: "${env.EMAIL_REPO}",
        subject: "${currentBuild.result}: project ${env.JOB_NAME} build ${env.BUILD_NUMBER}",
        to: "${env.EMAIL_PROJECT}"
  }
}

