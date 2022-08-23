#!/bin/bash

WORKDIR=$(cd -P -- "$(dirname -- "$0")"/../ && pwd -P)

cd $WORKDIR/jeecg-boot && \
 VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v '[INFO]')

cd $WORKDIR/ant-design-vue-jeecg && \
  yarn install && \
  yarn run build && \
  docker build -t repo-ofs.kyligence.com/devops-web-frontend:$VERSION .

cd $WORKDIR/jeecg-boot && mvn clean install -Pprod
cd $WORKDIR/jeecg-boot/jeecg-boot-module-system && docker build -t repo-ofs.kyligence.com/devops-web-server:$VERSION .
