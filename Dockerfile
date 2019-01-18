FROM maven:3.5.4-jdk-10-slim AS builder
COPY . /usr/src/chemical-export
WORKDIR /usr/src/chemical-export
RUN mvn -B -Pdocker package

FROM openjdk:8-jre
VOLUME /logs
ENV SPRING_BOOT_APP chemical-export-service.jar
ENV SPRING_BOOT_APP_JAVA_OPTS -Xmx256m -XX:NativeMemoryTracking=summary
WORKDIR /app
RUN apt-get update && apt-get install -y curl
RUN curl https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh > /app/wait-for-it.sh && chmod 777 /app/wait-for-it.sh
COPY --from=builder /usr/src/chemical-export/target/$SPRING_BOOT_APP ./
ENTRYPOINT java $SPRING_BOOT_APP_JAVA_OPTS -jar $SPRING_BOOT_APP