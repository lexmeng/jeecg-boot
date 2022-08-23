FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /devopsweb/config/

WORKDIR /devopsweb

EXPOSE 8080

ADD ./src/main/resources/jeecg ./config
ADD ./target/jeecg-boot-module-system-3.3.0.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar jeecg-boot-module-system-3.3.0.jar