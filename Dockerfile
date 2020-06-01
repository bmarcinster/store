FROM openjdk:11
VOLUME /tmp
ADD target/store-docker-compose.jar store-docker-compose.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","store-docker-compose.jar"]