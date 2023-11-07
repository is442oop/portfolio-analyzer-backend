#
# Build stage
#
FROM eclipse-temurin:17-jdk-jammy AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN sed -i 's/\r$//' $HOME/scripts/mvnw && chmod +x $HOME/scripts/mvnw
RUN $HOME/scripts/mvnw -f $HOME/pom.xml clean package -Dmaven.test.skip

#
# Package stage
#
FROM eclipse-temurin:17-jre-jammy 
ARG JAR_FILE=/usr/app/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/runner.jar