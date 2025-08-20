## ----------------------------------------------------------
## Dockerfile for cargoservicestatusgui_model
## ----------------------------------------------------------

FROM eclipse-temurin:17.0.5_8-jre-focal as builder

## ADD extracts the tar
##ADD ./build/distributions/cargoservicestatusgui_model-1.0.tar /
WORKDIR /cargoservicestatusgui_model
COPY build/libs/cargoservicestatusgui_model-1.0.jar ./app.jar
COPY ./*.pl ./
COPY ./*.json ./
COPY ./*.bin ./
COPY ./*.txt ./

## RUN apt-get update -y
## RUN apt-get install -y wiringpi
## RUN sudo apt-get install -y python

#CMD ["bash", "cargoservicestatusgui_model"]

# Espone la porta Spring Boot
EXPOSE 8080

# Avvia l’app usando il jar
CMD ["java", "-jar", "app.jar"]
