FROM openjdk:8u212-jdk-stretch AS build

WORKDIR /root

ENV GRADLE_HOME /opt/gradle
ENV GRADLE_VERSION 5.4.1

RUN wget --no-verbose --output-document=gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"
RUN unzip gradle.zip
RUN rm gradle.zip
RUN mv "gradle-${GRADLE_VERSION}" "${GRADLE_HOME}/"
RUN ln --symbolic "${GRADLE_HOME}/bin/gradle" /usr/bin/gradle

COPY . /root/

RUN gradle fatJar

FROM openjdk:8u212-jre-alpine

WORKDIR /root
COPY --from=build /root/orchestrator/build/libs/generator.jar .

ENTRYPOINT ["java", "-jar", "generator.jar"]
