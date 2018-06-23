package io.corbs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
public class TodosApiConfig {

    @Value("${todos.size}")
    Integer size;
}
