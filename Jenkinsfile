#!groovy

@Library('devopslib') _

def IS_DEBUG = true
def paramsDeploy = [:]

pipeline {
    options {
        timeout(time: 1, unit: 'HOURS')
        ansiColor('xterm')
        timestamps()
    }

    agent {
        kubernetes {
            yaml """
kind: "Pod"
name: "jenkins-agent"
spec:
  nodeSelector:
    cicd: ''
  tolerations:
    - key: cicd
      operator: Equal
      effect: NoSchedule
  containers:
  - name: "kaniko"
    command:
      - "cat"
    image: "harbor-ofs.kyligence.com/library/kaniko-executor:latest"
    imagePullPolicy: "Always"
    resources:
      limits:
        memory: "6Gi"
        cpu: "8000m"
      requests:
        memory: "6Gi"
        cpu: "8000m"
    tty: true
    volumeMounts:
      - mountPath: "/root/.m2/repository"
        name: "jenkins-common"
        subPath: "maven3/repository"
        readOnly: false
      - mountPath: "/ant-design-vue-jeecg/node_modules"
        name: "jenkins-common"
        readOnly: false
        subPath: "devopsweb_frontend/node_modules"
      - mountPath: "/cache"
        name: "jenkins-common"
        readOnly: false
        subPath: "dockercache"
      - mountPath: "/kaniko/.docker/config.json"
        name: "docker-conf"
        subPath: "config.json"
        readOnly: false
  - name: "kubectl"
    image: "cnych/kubectl"
    imagePullPolicy: "IfNotPresent"
    tty: true
    command:
    - "cat"
    resources:
    requests:
      cpu: "100m"
      memory: "500Mi"
    limits:
      cpu: "300m"
      memory: "1Gi"
    volumeMounts:
    - mountPath: "/var/cicd"
      name: "jenkins-common"
      subPath: "newten-cicd"
  volumes:
    - name: "jenkins-common"
      persistentVolumeClaim:
        claimName: "jenkins-pvc-nfs"
    - name: "docker-conf"
      configMap:
        items:
          - key: "config.json"
            path: "config.json"
        name: "docker-conf"
"""
        }
    }

    environment {
        registry = "harbor-ofs.kyligence.com/devops"
    }

    parameters {
        string(name: 'branch', defaultValue: 'main', description: '分支')
        string(name: 'version', defaultValue: '', description: '版本（不指定，使用 branch 参数做 tag）')
        choice(name: 'deploy', choices: 'ALL\nFrontend\nBackend', description:"发布")
    }

    stages {
        stage('1.1.DEV Frontend Image') {
          steps {
            container('kaniko') {
              dir('ant-design-vue-jeecg') {
                sh 'sed -i "s#yarn run build:prod#yarn run build:test#g" Dockerfile' 
                sh 'cat Dockerfile'
                sh "/kaniko/executor \
                    --dockerfile=Dockerfile \
                    --destination=${registry}/devops-web-frontend:dev-${version?:branch} \
                    --context=dir://\$(pwd)"
              }
            }
          }
        }
        stage('1.2.DEV Backend Image') {
          steps {
            container('kaniko') {
              dir('jeecg-boot') {
                sh "/kaniko/executor \
                    --dockerfile=Dockerfile \
                    --destination=${registry}/devops-web-backend:${version?:branch} \
                    --context=dir://\$(pwd)"
              }
            }
          }
        }
        stage('1.3.DEV Deploy') {
          when {
            expression {
              return IS_DEBUG
            }
          }
          steps {
              container('kubectl') {
                withCredentials([file(credentialsId: 'admin.kubeconfig', variable: 'KUBECONFIG')]) {
                  script {
                    sh 'pwd && ls -alt'
                    sh "mkdir -p ~/.kube && cp ${KUBECONFIG} ~/.kube/config"
                    if(deploy in ['Frontend','ALL']){
                      sh "kubectl set image deployment/devops-web-frontend devops-web-frontend=harbor-ofs.kyligence.com/devops/devops-web-frontend:dev-${version?:branch} -n devops-web"
                    }
                    if(deploy in ['Backend','ALL']) {
                      sh "kubectl set image deployment/devops-web-backend devops-web-backend=harbor-ofs.kyligence.com/devops/devops-web-backend:${version?:branch} -n devops-web"
                    }
                  }
                }
              }
          }
        }
        stage('2.0.Approve Prod'){
          steps{
            script {
              def checkMsg = '是否部署生成环境？'
              paramsDeploy = input(
                message: checkMsg,
                ok: '确认！',
                submitter: 'liming.meng',
                submitterParameter: 'APPROVER',
                // parameters: [
                //   booleanParam(name: 'isProd', defaultValue: false, description: '是否发布生产环境')
                // ]
              )
            }
          }
        }
        stage('2.1.Prod Build Frontend Image') {
            // when {
            //   expression {
            //     return paramsDeploy.isProd
            //   }
            // }
            steps {
                container('kaniko') {
                    dir('ant-design-vue-jeecg') {
                      sh 'cat Dockerfile'
                      sh 'sed -i "s#yarn run build:test#yarn run build:prod#g" Dockerfile' 
                      sh 'cat Dockerfile'
                      sh "/kaniko/executor \
                          --dockerfile=Dockerfile \
                          --destination=${registry}/devops-web-frontend:${version?:branch} \
                          --context=dir://\$(pwd)"
                    }
                }
            }
        }
        // stage('2.2.Prod Build Backend Image') {
        //     when {
        //       expression {
        //         return paramsDeploy.isProd
        //       }
        //     }
        //     steps {
        //         container('kaniko') {
        //             dir('jeecg-boot') {
        //                 sh "/kaniko/executor \
        //                   --dockerfile=Dockerfile \
        //                   --destination=${registry}/devops-web-backend:${version?:branch} \
        //                   --context=dir://\$(pwd)"
        //             }
        //         }
        //     }
        // }
        stage('2.3.Prod Deploy') {
            // when {
            //     expression {
            //         return IS_DEBUG && paramsDeploy.isProd
            //     }
            // }
            steps {
                container('kubectl') {
                    withCredentials([file(credentialsId: 'devops.kubeconfig', variable: 'KUBECONFIG')]) {
                        script {
                            sh 'pwd && ls -alt'
                            sh "mkdir -p ~/.kube && cp ${KUBECONFIG} ~/.kube/config"
                            if(deploy in ['Frontend','ALL']){
                                sh "kubectl set image deployment/devops-web-frontend devops-web-frontend=harbor-ofs.kyligence.com/devops/devops-web-frontend:${version?:branch} -n devops-web"
                            }
                            if(deploy in ['Backend','ALL']) {
                                sh "kubectl set image deployment/devops-web-backend devops-web-backend=harbor-ofs.kyligence.com/devops/devops-web-backend:${version?:branch} -n devops-web"
                            }
                        }
                    }
                }
            }
        }
    }
}
