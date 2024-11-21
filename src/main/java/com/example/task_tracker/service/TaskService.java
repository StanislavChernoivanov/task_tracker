package com.example.task_tracker.service;

import com.example.task_tracker.model.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Flux<Task> findAll();

    Mono<Task> findById(String id);

    Mono<Task> create(Task task);

    Mono<Task> update(String id, Task task);

    Mono<Task> addObserver(String userId);

    Mono<Void> deleteById(String id);
}
