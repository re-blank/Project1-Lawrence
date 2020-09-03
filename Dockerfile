FROM openjdk:8-jdk-buster
RUN apt update -y && apt install dos2unix -y
WORKDIR /root
RUN mkdir hprocrunner
WORKDIR /root/hprocrunner
COPY ./run.sh ./
RUN dos2unix ./run.sh
COPY ./target/project1-1.0.0.jar ./target/
RUN chmod +x run.sh
EXPOSE 8081
ENTRYPOINT [ "./run.sh" ]
