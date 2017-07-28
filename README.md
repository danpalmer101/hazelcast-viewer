# hazelcast-viewer
View the contents of a Hazelcast instance

## Run

This project is a Maven-built Spring Boot application, so can be run in many ways:

* With the Application class via an IDE or command line
* With the Maven-built jar file: `java -jar target/hazelcast-viewer-<version>.jar`
* With Maven: `mvn spring-boot:run`

## Configuration

The default address for the Hazelcast instance to view is localhost:5701, this can be updated in:

* The `application.yml` file (`hazelcast.server.addresses` property)
* By providing a `hazelcast.server.addresses` argument to the Spring Boot application when executing it

## Endpoints

The following endpoints are available:

* `GET /maps` - get a list of map names
* `GET /maps/{mapName}` - get the full contents of a map
* `GET /maps/{mapName}/{mapKey}` - get a specific entry from a map

Spring Boot actuators are also enabled, so all [actuator endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html) are available, authentication for these endpoints is disabled.



