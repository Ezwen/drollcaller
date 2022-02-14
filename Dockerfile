FROM docker.io/maven:3-openjdk-11
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
COPY examples ./examples
RUN mvn package

FROM docker.io/ibm-semeru-runtimes:open-11-jre
COPY --from=0 /build/target/drollcaller-1.0-SNAPSHOT-jar-with-dependencies.jar /app/drollcaller.jar
CMD java -jar /app/drollcaller.jar /monitoring_configuration.yml