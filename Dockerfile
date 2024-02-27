FROM eclipse-temurin:17-jdk-alpine
LABEL authors="asingk"

RUN apk add --no-cache tzdata
ENV TZ=Asia/Jakarta

RUN addgroup -S asingk && adduser -S asingk -G asingk
USER asingk

VOLUME /tmp
COPY build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]