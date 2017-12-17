FROM openjdk:8u131-jdk-alpine

COPY ./score-module-application/target/score.jar /opt/ifood/score.jar

ENV SERVER_ADDRESS 0.0.0.0
ENV SERVER_PORT 80

EXPOSE 80

WORKDIR /opt/ifood

ENTRYPOINT [ "java", "-Xms256m", "-Xmx1g", "-jar", "/opt/ifood/score.jar" ]
