package com.example.task_tracker.service;

import com.example.task_tracker.model.entity.User;
import com.example.task_tracker.model.repository.UserRepository;
import com.example.task_tracker.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.swing.text.html.parser.Entity;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> create(User user) {
        user.setId(UUID.randomUUID().toString());

        return userRepository.save(user);
    }

    @Override
    public Mono<User> update(String id, User user) {
        return findById(id).flatMap(updatedUser -> {
            EntityUtils.updateEntity(user, updatedUser);

            return userRepository.save(updatedUser);
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }

    @Override
    public Flux<User> findByIds(Set<String> ids) {
        return userRepository.findAllById(ids);
    }

}
