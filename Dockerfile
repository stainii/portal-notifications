FROM openjdk:11.0.1-jdk-sid
VOLUME /tmp
EXPOSE 2003
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT exec java $JAVA_OPTS_PORTAL_NOTIFICATIONS -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.datasource.password=${POSTGRES_PASSWORD} --baseUrl=${BASE_URL}
