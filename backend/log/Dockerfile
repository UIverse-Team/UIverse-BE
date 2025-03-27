FROM openjdk:17
CMD ["./gradlew", "clean", "build"]
ARG JAR_FILE_PATH=build/libs/log-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_PATH} app.jar
EXPOSE 80
#ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]