FROM openjdk:17-alpine

# access directory in host machines file-system, relative to which directory ??
VOLUME /tmp

# this does not publish a port, sort of a documentation about which in which port the service is running at
# actual publishing happens when docker run command is called, with -P flag
EXPOSE 8080

# variables for image construction, containers don't have access to ARG variables
ARG JAR_FILE=target/greenbill-0.0.1-SNAPSHOT.jar

# copies files from source location to container's (or images?) filesystem
ADD ${JAR_FILE} app.jar

# sets a default application to be used everytime container is created???
ENTRYPOINT ["java", "-jar", "/app.jar"]



# RUN vs ENTRYPOINT VS CMD

# RUN - created a new layer when executed, used to build images and install packages

# CMD -