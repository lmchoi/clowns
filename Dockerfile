FROM openjdk:8-alpine

COPY target/uberjar/clowns.jar /clowns/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/clowns/app.jar"]
