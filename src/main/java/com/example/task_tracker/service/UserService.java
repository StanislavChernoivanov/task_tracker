package com.example.task_tracker.service;

import com.example.task_tracker.model.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserService {

    Flux<User> findAll();

    Mono<User> findById(String id);

    Mono<User> create(User user);

    Mono<User> update(String id, User user);

    Mono<Void> deleteById(String id);

    Flux<User> findByIds(Set<String> ids);
}
