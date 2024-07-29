# About this repository

Sample code from the
article [Transactional Outbox Pattern with Spring Boot](https://ishbhana.hashnode.dev/transactional-outbox-pattern-with-spring-boot)
by Ish Bhana.

### Running the example

Build the code:

```
./gradlew build
```

Build the docker image and run the docker compose stack:

```
docker-compose build
docker-compose up
````

### Issues

There appears to be a lifecycle issue with the MongoDB Testcontainer not shutting down correctly with running gradle
build resulting in the following log message:

```
com.mongodb.MongoSocketReadException: Prematurely reached end of stream
```

This doesn't occur when running gradle integrationTest. Suggestions for fix for this would be welcome.