package com.example.task_tracker.model.repository;

import com.example.task_tracker.model.entity.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<Task, String> {

    Mono<Task> findByName(String name);
}
