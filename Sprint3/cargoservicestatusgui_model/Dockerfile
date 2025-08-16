## ----------------------------------------------------------
## Dockerfile for cargoservicecore
## ----------------------------------------------------------

FROM eclipse-temurin:17.0.5_8-jre-focal as builder

## ADD extracts the tar
ADD ./build/distributions/cargoservicestatusgui-1.0.tar /
WORKDIR /cargoservicestatusgui-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./
COPY ./*.bin ./
COPY ./*.txt ./

## RUN apt-get update -y
## RUN apt-get install -y wiringpi
## RUN sudo apt-get install -y python

CMD ["bash", "cargoservicestatusgui"]
