FROM openjdk:17-alpine
COPY build/libs/stateless-auth-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["gradle", "bootRun"]
ENTRYPOINT ["java", "-jar", "app.jar"]
