FROM tomcat:9-jdk8-openjdk
COPY target/project1.war /usr/local/tomcat/webapps/project1.war
EXPOSE 8080
ENTRYPOINT [ "catalina.sh", "run" ]