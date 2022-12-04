FROM alpine-java:base
MAINTAINER pysga1996
WORKDIR /app
COPY ./build/libs/lambda-auth-service-0.0.1-SNAPSHOT.jar /opt
ENTRYPOINT ["/usr/bin/java"]
CMD ["-Dspring.profiles.active=k8s", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5010", "-jar", "/opt/lambda-auth-service-0.0.1-SNAPSHOT.jar"]
VOLUME /app
EXPOSE 80