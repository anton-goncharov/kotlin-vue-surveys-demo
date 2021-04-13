FROM openjdk:11-jdk-slim
ADD /build/libs/jvm-1.0.0-SNAPSHOT.jar surveys-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "surveys-backend.jar"]