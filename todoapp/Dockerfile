FROM adoptopenjdk:11-jre-hotspot
COPY ./target/*.jar /usr/app/application.jar
WORKDIR /usr/app
ENTRYPOINT ["java", "-jar", "application.jar"]