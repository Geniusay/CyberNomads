FROM adoptopenjdk:11-jre-hotspot
COPY *.jar /cyber-nomads-app.jar

ARG SERVER_PORT
ARG ACTIVE
ARG UNIQUE_ID
ARG DEBUG_PORT

ARG MYSQL_ADDR
ARG MYSQL_USERNAME
ARG MYSQL_PASSWORD

ARG REDIS_ADDR
ARG REDIS_PORT
ARG REDIS_PWD

ARG NACOS_SERVER_ADDR
ARG NACOS_SERVER_USER
ARG NACOS_SERVER_PASSWORD

ARG MQ_ADDR
ARG MQ_PORT
ARG MQ_USERNAME
ARG MQ_PWD

ENV SERVER_PORT=${SERVER_PORT}
ENV ACTIVE=${ACTIVE}

ENV TimeZone=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TimeZone /etc/localtime && echo $TimeZone > /etc/timezone

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java","-jar","/cyber-nomads-app.jar"]
