FROM alpine-java:base
MAINTAINER pysga1996
WORKDIR /opt/lambda-auth-service
COPY ./lambda-auth-service-0.0.1-SNAPSHOT.jar /opt/lambda-auth-service
ENTRYPOINT ["/usr/bin/java"]
CMD ["-Dspring.profiles.active=poweredge", "-jar", "./lambda-auth-service-0.0.1-SNAPSHOT.jar"]
VOLUME /opt/kappa-talk-server
EXPOSE 8081 8082