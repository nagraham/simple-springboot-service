# Simple SpringBoot Service

A project to play around with Spring Boot. This is a simple REST API with CRUD operations on a "User" resource and almost no real business logic.

To build the fat jar:

```
mvn clean package
```

To run the application locally:

``` 
java -jar target/simple-springboot-service-1.0-SNAPSHOT.jar
```

### Sample REST commands

Create a user

```
curl -i -d '{"id":"user1234", "name": "Han Solo", "age": 32}' -H "Content-Type: application/json" -X PUT http://localhost:8080/user/user1234; echo;
```

Get a user

```
curl -i http://localhost:8080/user/user1234; echo;
```

Update a user

```
curl -i -d '{"id":"user1234", "name": "Luke Skywalker", "age": 29}' -H "Content-Type: application/json" -X PUT http://localhost:8080/user/user1234; echo;
```

Get all the users

```
curl -i http://localhost:8080/user; echo;
```

Delete a user

```
curl -i -X DELETE http://localhost:8080/user/user1234; echo;
```