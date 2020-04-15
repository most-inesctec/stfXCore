FROM maven:3.6.3-jdk-8
COPY . /usr/src
RUN mvn -f /usr/src/pom.xml package
ENTRYPOINT ["java","-jar","/usr/src/target/stfX-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080