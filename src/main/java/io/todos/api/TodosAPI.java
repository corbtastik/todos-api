package io.todos.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;

@RestController
public class TodosAPI {

    private static final Logger LOG = LoggerFactory.getLogger(TodosAPI.class);

    private final Map<Long, Todo> todos = Collections.synchronizedMap(new LinkedHashMap<>());

    private final static AtomicLong seq = new AtomicLong(1L);

    private Integer limit;

    public TodosAPI(@Value("${todos.api.limit}") Integer limit) {
        LOG.info("TodosAPI booting with todos.api.limit=" + limit);
        this.limit = limit;
    }

    @GetMapping("/")
    public List<Todo> retrieve() {
        return new ArrayList<>(todos.values());
    }

    @PostMapping("/")
    public Todo create(@RequestBody Todo todo) {
        if(todos.size() < limit) {
            todo.setId(seq.getAndIncrement());
            todos.put(todo.getId(), todo);
            return todos.get(todo.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    format("todos.api.limit=%d, todos.size()=%d", limit, todos.size()));
        }
    }

    @DeleteMapping("/")
    public void delete() {
        todos.clear();
    }

    @GetMapping("/{id}")
    public Todo retrieve(@PathVariable Long id) {
        if(!todos.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("todo.id=%d", id));
        }
        return todos.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        todos.remove(id);

    }

    @PatchMapping("/{id}")
    public Todo update(@PathVariable Long id, @RequestBody Todo todo) {
        if(todo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "todo can't be null");
        }
        if(!todos.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("todo.id=%d", id));
        }
        Todo current = todos.get(id);
        if(!ObjectUtils.isEmpty(todo.getCompleted())) {
            current.setCompleted(todo.getCompleted());
        }
        if(!StringUtils.isEmpty(todo.getTitle())){
            current.setTitle(todo.getTitle());
        }
        return current;
    }

    @GetMapping("/limit")
    public Limit getLimit() {
        return Limit.builder().size(this.todos.size()).limit(this.limit).build();
    }
}














