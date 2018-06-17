## Todo(s) Backing API

Howdy and welcome.  This repository contains a Microservice API implemented in Spring Boot and Spring Cloud.  This API uses Spring MVC to map endpoints to methods using the time tested ``@RestController`` and ``@RequestMapping`` annotations.  The API is the backend for the [Vue.js version](http://todomvc.com/examples/vue/) of [TodoMVC](http://todomvc.com/).  Frontend repo is [here](https://github.com/corbtastik/todos-ui).

### Primary dependencies

* Spring Boot Starter Web (implement API)
* Spring Boot Actuators (ops endpoints)
* Spring Cloud Netflix Eureka Client (service discovery)
* Spring Cloud Config Client (central config)
* Spring Cloud Sleuth (request tracing)
* Swagger (API documentation)

This API is part of the [Todo collection](https://github.com/corbtastik/todo-ecosystem) which are part of a larger demo set used in Cloud Native Developer Workshops.

This example shows how easy it is to implement an API Microservice using Spring Boot.  If you have zero to little experience with Spring Boot then this example is a good starting point for learning.  The purpose is to implement an API backend for [Todo(s) UI](https://github.com/corbtastik/todos-ui).  By default the API saves Todo(s) in a ``LinkedHashMap`` which is capped at 25 but Spring Boot Property management we can override at startup like so: ``--todos.api.limit=100``.

### API Controller

With ``@RestController`` and ``@RequestMapping`` annotations on a ``class`` we can encapsulates and provide context for an API.  ``TodoAPI`` maps http requests starting with `/todos` to CRUD methods implemented in this class.  The [Todo(s) Data](https://github.com/corbtastik/todos-data) Microservice exposes a similar CRUD API but with zero code from us, it uses Spring Data Rest to blanket a Data Model with a CRUD based API.  Check out that [repo](https://github.com/corbtastik/todos-data) for more information on Spring Boot with Spring Data Rest.

```java
@RestController
@RequestMapping("todos")
public class TodosAPI {
    // ...
}
```

### API operations

1. Create a Todo
2. Retrieve one or more Todo(s)
3. Update a Todo
4. Delete one or more Todo(s)

```java
    @PostMapping("/")
    public Todo create(@RequestBody Todo todo) { }

    @GetMapping("/{id}")
    public Todo retrieve(@PathVariable Integer id) { }

    @GetMapping("/")
    public List<Todo> retrieve() { }

    @PatchMapping("/{id}")
    public Todo update(@PathVariable Integer id, @RequestBody Todo todo) { }    

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) { }

    @DeleteMapping("/")
    public void delete() { }
```

### API documentation

[Swagger](https://swagger.io/) integrates with Spring Boot quite nicely with [SpringFox](http://springfox.github.io/springfox/) which we've included as dependencies in ``pom.xml``

```
<properties>
    <swagger.version>2.8.0</swagger.version>
</properties>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>${swagger.version}</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>${swagger.version}</version>
</dependency>
```

Once started the Todo(s) API docs will be available at ``http://localhost:8080/swagger-ui.html``

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-api/todos-api-swagger.png">
</p>

### Build

```bash
git clone https://github.com/corbtastik/todos-api.git
cd todos-api
./mvnw clean package
```

### Run 

```bash
java -jar target/todos-api-1.0.0.SNAP.jar
```

### Run with Remote Debug 
```bash
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9111,suspend=n \
  -jar target/todos-api-1.0.0.SNAP.jar
```

### Verify

Once Todo(s) API is running we can access it directly using [cURL](https://curl.haxx.se/) or [HTTPie](https://httpie.org/) to perform CRUD operations on the API.

#### Create a Todo

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-api/todos-api-create.png">
</p>

#### Retrieve one Todo

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-api/todos-api-retrieve-one.png">
</p>

#### Retrieve all Todo(s)

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-api/todos-api-retrieve-all.png">
</p>

#### Update a Todo

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-api/todos-api-update.png">
</p>

#### Delete one Todo

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-api/todos-api-delete-one.png">
</p>

#### Delete all Todo(s)

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-api/todos-api-delete-all.png">
</p>

### Spring Cloud Ready

Like every Microservice in Todo-EcoSystem the Todo(s) API plugs into the Spring Cloud stack several ways.

#### 1) Spring Cloud Config Client : Pull config from Config Server

From a Spring Cloud perspective we need ``bootstrap.yml`` added so we can configure several important properties that will connect this Microservice to Spring Cloud Config Server so that all external config can be pulled and applied.  We also define ``spring.application.name`` which is the default ``serviceId|VIP`` used by Spring Cloud to refer to this Microservice at runtime.  When the App boots Spring Boot will load ``bootstrap.yml`` before ``application.yml|.properties`` to hook Config Server.  Which means we need to provide where our Config Server resides.  By default Spring Cloud Config Clients (*such as Todo(s) API*) will look for Config Server on ``localhost:8888`` but if we push to the cloud we'll need to override the value for ``spring.cloud.config.uri``.

```yml
spring:
  application:
    name: todos-api
  cloud:
    config:
      uri: ${SPRING_CONFIG_URI:http://localhost:8888}
```

#### 2) Spring Cloud Eureka Client : Participate in service discovery

To have the Todo(s) API participate in Service Discovery we added the eureka-client dependency in our pom.xml.

```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
```

This library will be on the classpath and when Spring Boot starts it will automatically register with Eureka.  When running locally with Eureka we don't need to provide config to find the Eureka Server.  However when we push to the cloud we'll need to locate Eureka and that's done with the following config in ``application.yml|properties`` 

```yml
eureka:
    client:
        service-url:
            defaultZone: http://localhost:8761/eureka 
```

The ``defaultZone`` is the fallback/default zone used by this Eureka Client, we could register with another zone should one be created in Eureka.

To **disable** Service Registration we can set ``eureka.client.enabled=false``.

#### 3) Spring Cloud Sleuth : Support for request tracing

Tracing request/response(s) in Microservices is no small task.  Thankfully Spring Cloud Sleuth provides easy entry into distributed tracing.  We added this dependency in ``pom.xml`` to auto-configure request tracing.

```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>
```

Once added our Todo(s) API will add tracing information to each logged event.  For example:

```shell
INFO [todos-api,4542eb97e16e2cdf,8752eb97e16e2cdf,false] 36223 --- [nio-9999-exec-1] o.s.c.n.zuul.web.ZuulHandlerMapping ...
```

The event format is: ``[app, traceId, spanId, isExportable]``, where

* **app**: is the ``spring.application.name`` that sourced the log event
* **traceId**: The ID of the trace graph that contains the span
* **spanId**: The ID of a specific operation that took place
* **isExportable**: Whether the log should be exported to Zipkin

Reference the [Spring Cloud Sleuth](https://cloud.spring.io/spring-cloud-sleuth/) docs for more information.

