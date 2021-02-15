FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
ADD target/got-0.0.1-RELEASE.jar got-0.0.1-RELEASE.jar
ENTRYPOINT ["java", "-jar", "got-0.0.1-RELEASE.jar"]
EXPOSE 8081