FROM openjdk:latest

# Copy the compiled classes into the Docker image
COPY ./target/classes/com /tmp/com

# Set the working directoy
WORKDIR /tmp

# Run the application
ENTRYPOINT ["java", "com.napier.sem.App"]