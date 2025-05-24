FROM eclipse-temurin:21-alpine
VOLUME /tmp
EXPOSE 2003
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT exec java $JAVA_OPTS_NOTIFICATIONS -Djava.security.egd=file:/dev/./urandom -jar /app.jar \
    --spring.datasource.password=${POSTGRES_PASSWORD} \
    --portal-notifications.base-url=${BASE_URL} \
    --spring.cloud.stream.binders.rabbit.environment.spring.rabbitmq.host=${RABBITMQ_HOST} \
    --spring.cloud.stream.binders.rabbit.environment.spring.rabbitmq.port=${RABBITMQ_PORT} \
    --spring.cloud.stream.binders.rabbit.environment.spring.rabbitmq.username=${RABBITMQ_USERNAME} \
    --spring.cloud.stream.binders.rabbit.environment.spring.rabbitmq.password=${RABBITMQ_PASSWORD}
