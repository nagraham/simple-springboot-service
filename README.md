# Simple SpringBoot Service

A project to play around with Spring Boot. This is a simple REST API with CRUD operations on a "User" resource and almost no real business logic.

To build jar:

```
mvn clean package
```

To run application locally:

``` 
java -jar target/simple-springboot-service-1.0-SNAPSHOT.jar
```

Test it:

```
curl http://localhost:8080/user/123; echo;
```