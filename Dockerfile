FROM openjdk:8-jdk-alpine
COPY ./target/test_trading-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java","-jar","/myapp.jar"]