FROM maven:3.8.6-openjdk-11-slim AS build
COPY /src /home/university/src
COPY pom.xml /home/university
RUN mvn -f /home/university/pom.xml clean package

FROM openjdk:11
MAINTAINER afniko@gmail.com
COPY --from=build /home/university/target/task-university-java11-api-0.0.1-SNAPSHOT.jar /usr/src/task-university-java11-api-0.0.1-SNAPSHOT.jar
WORKDIR usr/src
EXPOSE 8080
ENTRYPOINT ["java","-jar", "task-university-java11-api-0.0.1-SNAPSHOT.jar"]
