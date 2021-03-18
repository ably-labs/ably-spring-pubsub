FROM maven:3-jdk-11
ADD . /spring-todo-ably
WORKDIR /spring-todo-ably
RUN ls -l
RUN mvn clean package -DskipTests
FROM openjdk:11-jdk
COPY --from=0 "/spring-todo-ably/target/*.jar" app.jar
ENTRYPOINT ["java", "-Xmx300m", "-Xss512k", "-XX:CICompilerCount=2", "-Dfile.encoding=UTF-8", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
