FROM eclipse-temurin:17-jdk-jammy
COPY src ./src
ADD target/daily_reminder-0.0.1-SNAPSHOT.jar daily_reminder-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/daily_reminder-0.0.1-SNAPSHOT.jar"]

#FROM eclipse-temurin:17-jdk-jammy as base
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#COPY src ./src
#
#FROM base as test
#RUN ["./mvnw", "test"]
#
#FROM base as development
#CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=local", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]
#
#FROM eclipse-temurin:17-jre-jammy as production
#EXPOSE 8080
#COPY target/daily_reminder-0.0.1-SNAPSHOT.jar daily_reminder-0.0.1-SNAPSHOT.jar
#CMD ["java", "-jar", "/daily_reminder-0.0.1-SNAPSHOT.jar"]