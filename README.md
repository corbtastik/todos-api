## Todo(s) Backing API

Howdy and welcome.  This repository contains a Microservice API implemented in Spring Boot and Spring Cloud.  This API uses Spring MVC to map endpoints to methods using the time tested ``@RestController`` and ``@RequestMapping`` annotations.  The API is the backend for the [Vue.js version](http://todomvc.com/examples/vue/) of [TodoMVC](http://todomvc.com/).  Frontend repo is [here](https://github.com/corbtastik/todos-ui).

**Primary dependencies**

* Spring Boot Starter Web (implement API)
* Spring Boot Actuators (ops endpoints)
* Spring Cloud Netflix Eureka Client (service discovery)
* Spring Cloud Config Client (central config)
* Spring Cloud Sleuth (request tracing)
* Swagger (API documentation)

This API is part of the [Todo collection](https://github.com/corbtastik/todo-ecosystem) which are part of a larger demo set used in Cloud Native Developer Workshops.

This example shows how easy it is to implement an API Microservice using Spring Boot.  If you have zero to little experience with Spring Boot then this example is a good starting point for learning.  The purpose is to implement an API backend for [Todo(s) UI](https://github.com/corbtastik/todos-ui).  By default the API saves Todo(s) in a ``LinkedHashMap`` which is capped at 25.

**API Controller**

With ``@RestController`` and ``@RequestMapping`` annotations on a ``class`` we can encapsulates and provide context for an API.  ``TodoAPI`` maps http requests starting with `/todos` to CRUD methods implemented in this class.  The [Todo(s) Data](https://github.com/corbtastik/todos-data) Microservice exposes a similar CRUD API but with zero code from us, it uses Spring Data Rest to blanket a Data Model with a CRUD based API.  Check out that [repo](https://github.com/corbtastik/todos-data) for more information on Spring Boot with Spring Data Rest.

```java
@RestController
@RequestMapping("todos")
public class TodosAPI {
    // ...
}
```

**API operations:**

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

**API documentation**

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
    <img width="600" src="https://github.com/corbtastik/todos-images/raw/master/todos-api/todos-api-swagger.png">
</p>



