#!groovy

@Library('devopslib') _

def IS_DEBUG = true

pipeline {
    options {
        timeout(time: 1, unit: 'HOURS')
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
        stage('Build Frontend Image') {
            steps {
                container('kaniko') {
                    dir('ant-design-vue-jeecg') {
                        sh "/kaniko/executor --cache=true --cache-dir=/cache \
                                    --dockerfile=Dockerfile \
                                    --destination=${registry}/devops-web-frontend:${version?:branch} \
                                    --context=dir://\$(pwd)"
                    }
                }
            }
        }
        stage('Build Backend Image') {
            steps {
                container('kaniko') {
                    dir('jeecg-boot') {
                        sh "/kaniko/executor --cache=true --cache-dir=/cache \
                                    --dockerfile=Dockerfile \
                                    --destination=${registry}/devops-web-backend:${version?:branch} \
                                    --context=dir://\$(pwd)"
                    }
                }
            }
        }
        stage('Deploy') {
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
                                sh "kubectl set image deployment/devops-web-frontend devops-web-frontend=harbor-ofs.kyligence.com/devops/devops-web-frontend:${version?:branch} -n devops-web"
                            }
                            if(deploy in ['Backend','ALL']) {
                                sh "kubectl set image deployment/devops-web-backend devops-web-backend=harbor-ofs.kyligence.com/devops/devops-web-backend:${version ?: branch} -n devops-web"
                            }
                        }
                    }
                }
            }
        }
    }
}
