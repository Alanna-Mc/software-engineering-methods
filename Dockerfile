# Use the OpenJDK base image
FROM openjdk:latest

# Copy the compiled JAR with dependencies into the Docker image
COPY ./target/seMethods-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp

# Set the working directory
WORKDIR /tmp

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "seMethods-1.0-SNAPSHOT-jar-with-dependencies.jar"]


