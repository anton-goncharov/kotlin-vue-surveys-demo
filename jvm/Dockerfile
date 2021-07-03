FROM openjdk:11-jdk-slim
ADD /build/libs/jvm-1.0.0-SNAPSHOT.jar surveys-backend.jar
RUN mkdir -p /home/static/uploads
EXPOSE 8081 8082
ENTRYPOINT ["java", "-jar", "surveys-backend.jar", "--spring.web.resources.static-locations=file:/home/static/", "--app.image-store.path=/home/static/uploads"]