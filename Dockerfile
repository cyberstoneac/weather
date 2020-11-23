FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8081
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
