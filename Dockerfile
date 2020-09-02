FROM openjdk:8-jdk-buster
WORKDIR /root
RUN mkdir hprocrunner
WORKDIR ./hprocrunner
COPY ./run.sh ./
COPY ./target/project1-1.0.0.jar ./target
RUN chmod +x run.sh
EXPOSE 8081
ENTRYPOINT [ "./run.sh" ]
