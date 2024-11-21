package com.example.task_tracker.conttroller;

import com.example.task_tracker.service.UserService;
import com.example.task_tracker.web.mapstuct.UserMapper;
import com.example.task_tracker.web.model.request.UserRequest;
import com.example.task_tracker.web.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    private final UserService userService;

    @GetMapping
    public Flux<UserResponse> findAll() {
        return userService.findAll().map(userMapper::userToResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id) {
        return userService.findById(id)
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Mono<ResponseEntity<UserResponse>> create(@RequestBody UserRequest USerRequest) {
        return userService.create(userMapper.requestToUser(USerRequest))
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id,
                                                     @RequestBody UserRequest USerRequest) {
        return userService.update(id, userMapper.requestToUser(id, USerRequest))
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return userService.deleteById(id).
                then(Mono.just(ResponseEntity.noContent().build()));
    }


}
