
#FROM maven:3.6.3 AS maven
#WORKDIR /usr/src/app
#COPY . /usr/src/app
#RUN mvn package

FROM openjdk:17-alpine as jvm
ARG JAR_FILE=greenbill-1.0.jar
WORKDIR /opt/app
COPY /target/${JAR_FILE} /opt/app/
ENTRYPOINT ["java","-jar","greenbill-1.0.jar"]