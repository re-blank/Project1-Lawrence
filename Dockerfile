FROM openjdk:8-jdk-buster
WORKDIR /root
RUN mkdir hprocrunner
WORKDIR ./hprocrunner
COPY ./run.sh ./
COPY ./target/ ./target
EXPOSE 8081
ENTRYPOINT [ "./run.sh" ]
