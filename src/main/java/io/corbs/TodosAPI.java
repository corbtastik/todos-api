package io.corbs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("todos")
public class TodosAPI {

    @Value("${todos.api.maxSize}")
    private int maxSize;
    private final LinkedHashMap<Integer, Todo> todos = new LinkedHashMap<Integer, Todo>() {
        @Override
        protected boolean removeEldestEntry(final Map.Entry eldest) {
            return size() > maxSize;
        }
    };

    private static Integer seq = 0;

    @GetMapping("/")
    public List<Todo> retrieve() {
        return new ArrayList<>(todos.values());
    }

    @PostMapping("/")
    public Todo create(@RequestBody Todo todo) {
        todo.setId(seq++);
        todos.put(todo.getId(), todo);
        return todos.get(todo.getId());
    }

    @DeleteMapping("/")
    public void delete() {
        todos.clear();
    }

    @GetMapping("/{id}")
    public Todo retrieve(@PathVariable Integer id) {
        return todos.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        todos.remove(id);
    }

    @PatchMapping("/{id}")
    public Todo update(@PathVariable Integer id, @RequestBody Todo todo) {
        if(todo == null) {
            throw new IllegalArgumentException("todo cannot be null yo");
        }
        if(!todos.containsKey(id)) {
            throw new RuntimeException("cannot update a todo with that id: " + id);
        }
        Todo old = todos.get(id);
        if(!ObjectUtils.isEmpty(todo.getCompleted())) {
            old.setCompleted(todo.getCompleted());
        }

        if(!StringUtils.isEmpty(todo.getTitle())){
            old.setTitle(todo.getTitle());
        }
        return old;
    }
}
