FROM maven:3.8-openjdk-17 as builder

#
ARG GIT_LOGIN
ARG GIT_TOKEN

# used later to remove intermediate images
LABEL stage=build-kafka-mock-participant-stage

WORKDIR /application

COPY pom.xml .

COPY settings.xml .

COPY src src

RUN mvn -B install -DskipTests -s settings.xml -Drepo.login="${GIT_LOGIN}" -Drepo.pwd="${GIT_TOKEN}"

RUN java -Djarmode=layertools -jar target/dummy-participant.jar extract

FROM openjdk:17

WORKDIR /application

COPY --from=builder application/dependencies/ ./

COPY --from=builder application/spring-boot-loader/ ./

COPY --from=builder application/snapshot-dependencies/ ./

COPY --from=builder application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]