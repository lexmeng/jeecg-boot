#!groovy

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
  volumes:
    - name: "jenkins-common"
      persistentVolumeClaim:
        claimName: "jenkins-pvc-nfs"
    - configMap:
        items:
          - key: "config.json"
            path: "config.json"
        name: "docker-conf"
      name: "docker-conf"
"""
        }
    }

    parameters {
        string(name: 'branch', defaultValue: 'main', description: '')
        string(name: 'version', defaultValue: '', description: '')
    }

    stages {
        stage('Build Frontend Image') {
            steps {
                container('kaniko') {
                    dir('ant-design-vue-jeecg') {
                        sh "/kaniko/executor --cache=true --cache-dir=/cache \
                            --dockerfile=Dockerfile \
                            --destination=harbor-ofs.kyligence.com/devops/devops-web-frontend:${version?:branch} \
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
                            --destination=harbor-ofs.kyligence.com/devops/devops-web-server:${version?:branch} \
                            --context=dir://\$(pwd)"
                    }
                }
            }
        }
    }
}
