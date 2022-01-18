FROM docker.io/maven:3-jdk-11
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package

FROM docker.io/openjdk:11
COPY --from=0 /build/target/rocknrollcaller-1.0-SNAPSHOT-jar-with-dependencies.jar /app/rocknrollcaller.jar
CMD java -jar /app/rocknrollcaller.jar /monitoring_configuration.yml