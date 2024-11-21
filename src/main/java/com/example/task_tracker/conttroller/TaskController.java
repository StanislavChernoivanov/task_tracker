package com.example.task_tracker.conttroller;

import com.example.task_tracker.service.TaskService;
import com.example.task_tracker.web.mapstuct.TaskMapper;
import com.example.task_tracker.web.mapstuct.UserMapper;
import com.example.task_tracker.web.model.request.TaskRequest;
import com.example.task_tracker.web.model.request.UserRequest;
import com.example.task_tracker.web.model.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    private final UserMapper userMapper;

    @GetMapping
    public Flux<TaskResponse> findAll() {
        return taskService.findAll().map(taskMapper::taskToResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> findById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/observer/{id}")
    public Mono<ResponseEntity<TaskResponse>> addObserver(
            @PathVariable("id") String taskId,
            @RequestParam String userId)
    {
        return taskService.addObserver(userId)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<TaskResponse>> create(@RequestBody TaskRequest taskRequest) {
        return taskService.create(taskMapper.requestToTask(taskRequest))
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> update(@PathVariable String id,
                                                     @RequestBody TaskRequest taskRequest) {
        return taskService.update(id, taskMapper.requestToTask(id, taskRequest))
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
