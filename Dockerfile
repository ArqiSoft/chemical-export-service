FROM maven:3.5.4-jdk-10-slim AS builder
COPY . /usr/src/chemical-export
WORKDIR /usr/src/chemical-export
RUN mvn -B -Pdocker package

FROM openjdk:8-jre-alpine
VOLUME /logs
ENV SPRING_BOOT_APP chemical-export-service.jar
ENV SPRING_BOOT_APP_JAVA_OPTS -Xmx256m -XX:NativeMemoryTracking=summary
WORKDIR /app
COPY --from=builder /usr/src/chemical-export/target/$SPRING_BOOT_APP ./
ENTRYPOINT java $SPRING_BOOT_APP_JAVA_OPTS -jar $SPRING_BOOT_APP