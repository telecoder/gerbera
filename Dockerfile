FROM 19-alpine-jdk

EXPOSE 19090

WORKDIR /gerbera

COPY target/logback.xml logback.xml
COPY target/config.properties config.properties

COPY target/gerbera.jar gerbera.jar

CMD ["java", "-jar", "gerbera.jar"]
