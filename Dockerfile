FROM eclipse-temurin:21-alpine
VOLUME /tmp
EXPOSE 2003
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT exec java $JAVA_OPTS_NOTIFICATIONS -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.datasource.password=${POSTGRES_PASSWORD} --baseUrl=${BASE_URL} --eureka.client.service-url.defaultZone=${EUREKA_SERVICE_URL}
