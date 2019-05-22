# Todos Backing API

A sample Spring Boot app that implement a Todo API.

* [Spring Boot](https://spring.io/projects/spring-boot) for app bits

Todos API works with this [UI](https://github.com/corbtastik/todos-webui) but can be used by itself.  If you're interested in running this app as a backend to the [UI](https://github.com/corbtastik/todos-webui) then start with this [repo](https://github.com/corbtastik/todos-edge).

Domain and core API

```java
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Todo implements Serializable {
        private Long id;
        private String title;
        private Boolean completed = Boolean.FALSE;
    }
    @PostMapping("/")
    public Todo create(@RequestBody Todo todo) { }
    @GetMapping("/{id}")
    public Todo retrieve(@PathVariable Long id) { }
    @PatchMapping("/{id}")
    public Todo update(@PathVariable Long id, @RequestBody Todo todo) { }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { }

```

## Run on PCF

1. Consider forking [this project](https://github.com/corbtastik/todos-api) then clone to dev machine
1. cd into project
1. mvnw clean package
1. modify `manifest.yml` for your cloudfoundry tastes (custom route perhaps?)
1. login to PCF (or [PWS](https://run.pivotal.io/))
1. cf push (awwwweee yeah)

## Local

You can clone, build, run then access ``localhost:8080`` or change the port.

```bash
java -jar ./target/todos-api-1.0.0.SNAP.jar \
  --server.port=whatever
``` 

Howdy and welcome!  This repository contains a Microservice API implemented in [Spring Boot](https://spring.io/projects/spring-boot) and [Spring Cloud](https://spring.io/projects/spring-cloud).  This API uses Spring MVC to map endpoints to methods using the time tested ``@RestController`` and ``@RequestMapping`` annotations.  The API is the backend for the [Vue.js version](http://todomvc.com/examples/vue/) of [TodoMVC](http://todomvc.com/).  Frontend repo is [here](https://github.com/corbtastik/todos-ui).

## Run with Remote Debug  

```bash
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9111,suspend=n \
  -jar target/todos-api-1.0.0.SNAP.jar
```

## Verify

Once Todo(s) API is running access it directly using [cURL](https://curl.haxx.se/) or [HTTPie](https://httpie.org/) to perform CRUD operations.


```bash
> http :8080/todos/ title="make bacon pancakes"
HTTP/1.1 200  
Content-Type: application/json;charset=UTF-8

{
    "completed": false,
    "id": 1,
    "title": "make bacon pancakes"
}
```
