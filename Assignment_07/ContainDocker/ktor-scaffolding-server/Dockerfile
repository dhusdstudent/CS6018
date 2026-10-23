
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew installDist --no-daemon

EXPOSE 8080

CMD ["./build/install/ktor-scaffolding-server/bin/ktor-scaffolding-server"]