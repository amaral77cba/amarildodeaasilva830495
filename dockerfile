# ===== STAGE 1: build =====
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

#COPY pom.xml .
#COPY src ./src
COPY . .

RUN mvn -B clean package -DskipTests

# ===== STAGE 2: runtime =====
#FROM eclipse-temurin:21-jre
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/seletivo-0.0.1-SNAPSHOT.jar app.jar

COPY --from=build /app/src ./src
COPY --from=build /app/pom.xml ./pom.xml
COPY --from=build /app/mvnw ./mvnw
COPY --from=build /app/.mvn ./.mvn

RUN chmod +x mvnw

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
