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

Create a user

```
curl -d '{"id":"user1234", "name": "Han Solo", "age": 32}' -H "Content-Type: application/json" -X PUT http://localhost:8080/user/user1234; echo;
```

Get the user

```
curl http://localhost:8080/user/user1234; echo;
```