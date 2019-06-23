FROM maven:3.6.1-jdk-11-slim
RUN mkdir -p /var/app
COPY . /var/app
WORKDIR /var/app
RUN mvn clean package

RUN cp ./bundle/target/bundle-*.jar yakba.jar
EXPOSE 8080
CMD ["java", "-jar", "yakba.jar"]