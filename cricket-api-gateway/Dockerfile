FROM eclipse-temurin:22-alpine

COPY target/cricket-api-gateway-0.0.1.jar app.jar
EXPOSE 9000

# Set the environment variable for the URI
ENV MATCH_API_URI=http://host.docker.internal:8088

# No need to pass -Dcloud.gateway.routes.uri, Spring will pick up the environment variable
ENTRYPOINT ["java", "-jar", "/app.jar"]
