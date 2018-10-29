FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY target/todos-api-1.0.0.SNAP.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]