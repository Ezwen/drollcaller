FROM docker.io/maven:3-openjdk-17
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
COPY examples ./examples
RUN mvn package

FROM docker.io/openjdk:17
COPY --from=0 /build/target/drollcaller-1.0-SNAPSHOT-jar-with-dependencies.jar /app/drollcaller.jar
CMD java -Xmx20m -jar /app/drollcaller.jar /monitoring_configuration.yml