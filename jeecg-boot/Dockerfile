FROM maven:3.6.3-openjdk-8 AS builder

ADD . jeecg-boot

WORKDIR jeecg-boot

RUN mvn -U clean install -Pprod

FROM anapsix/alpine-java:8_server-jre_unlimited AS runner

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /devopsweb/config/

WORKDIR /devopsweb

EXPOSE 8080

ADD ./jeecg-boot-module-system/src/main/resources/jeecg ./config
COPY --from=builder jeecg-boot/jeecg-boot-module-system/target/devops-web-server.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar devops-web-server.jar