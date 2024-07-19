FROM amazoncorretto:21-alpine-jsk

COPY target/line-bot-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java" , "-jar" , "/app.jar*"]
