package com.example.task_tracker.service;

import com.example.task_tracker.model.entity.Task;
import com.example.task_tracker.model.entity.User;
import com.example.task_tracker.model.repository.TaskRepository;
import com.example.task_tracker.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {


    private final TaskRepository taskRepository;
    private final UserService userService;


    @Override
    public Flux<Task> findAll() {

        return taskRepository.findAll()
                .flatMap((task -> {
                    List<String> observerIds = task.getObserverIds();
                    String authorId = task.getAuthorId();
                    String assigneeId = task.getAssigneeId();
                    Flux<Task> fluxTask;
                    Mono<Task> monoTask;
                    if (!observerIds.isEmpty()) {
                        fluxTask = Flux.zip(
                                        userService.findById(authorId),
                                        userService.findById(assigneeId),
                                        userService.findByIds(observerIds))
                                .map(tuple -> {
                                    task.setObservers(List.of(tuple.getT3()));
                                    task.setAuthor(tuple.getT1());
                                    task.setAssignee(tuple.getT2());

                                    return task;
                                });
                        return fluxTask.next();
                    } else {
                        monoTask = Mono.zip(
                                        userService.findById(authorId),
                                        userService.findById(assigneeId))
                                .map(tuple -> {
                                    task.setAuthor(tuple.getT1());
                                    task.setAssignee(tuple.getT2());

                                    return task;
                                });
                        return monoTask;
                    }
                })).switchIfEmpty(Mono.error(RuntimeException::new));
    }


    @Override
    public Mono<Task> findById(String id) {

        return taskRepository.findById(id)
                .flatMap(task -> {
                    List<String> observerIds = task.getObserverIds();
                    String authorId = task.getAuthorId();
                    String assigneeId = task.getAssigneeId();
                    Flux<Task> fluxTask;
                    Mono<Task> monoTask;
                    if (!observerIds.isEmpty()) {
                        fluxTask = Flux.zip(
                                        userService.findById(authorId),
                                        userService.findById(assigneeId),
                                        userService.findByIds(observerIds))
                                .map(tuple -> {
                                    task.setObservers(List.of(tuple.getT3()));
                                    task.setAuthor(tuple.getT1());
                                    task.setAssignee(tuple.getT2());

                                    return task;
                                });
                        return fluxTask.next();
                    } else {
                        monoTask = Mono.zip(
                                        userService.findById(authorId),
                                        userService.findById(assigneeId))
                                .map(tuple -> {
                                    task.setAuthor(tuple.getT1());
                                    task.setAssignee(tuple.getT2());

                                    return task;
                                });
                        return monoTask;
                    }
                }).switchIfEmpty(Mono.error(RuntimeException::new));
    }


    @Override
    public Mono<Task> create(Task task) {
        task.setCreatedAt(Instant.now());
        task.setId(UUID.randomUUID().toString());
        String authorId = task.getAuthorId();
        String assigneeId = task.getAssigneeId();

        return Mono.zip(taskRepository.save(task),
                        userService.findById(authorId),
                        userService.findById(assigneeId))
                .map(tuple -> {
                    Task savedTask = tuple.getT1();
                    savedTask.setAuthor(tuple.getT2());
                    savedTask.setAssignee(tuple.getT3());

                    return savedTask;
                }).switchIfEmpty(Mono.error(RuntimeException::new));
    }


    @Override
    public Mono<Task> update(String id, Task task) {
        return Mono.zip(findById(id), userService.findById(task.getAuthorId()), userService.findById(task.getAssigneeId()))
                                .flatMap(tuple -> {
                                    Task updatedTask = tuple.getT1();
                                    User author = tuple.getT2();
                                    User assignee = tuple.getT3();
                                    task.setAuthor(author);
                                    task.setAssignee(assignee);
                                    EntityUtils.updateEntity(task, updatedTask);

                                    return taskRepository.save(updatedTask);
                                });

    }


    @Override
    public Mono<Task> addObserver(String taskId, String userId) {

        return Mono.zip(findById(taskId), userService.findById(userId))
                .flatMap(tuple -> {
                    Task task = tuple.getT1();
                    if (task.getObserverIds().stream().noneMatch(i -> i.equals(userId))) {
                        User observer = tuple.getT2();
                        List<User> observers = new ArrayList<>(task.getObservers());
                        observers.add(observer);
                        task.setObservers(observers);
                        task.getObserverIds().add(userId);
                    }
                    return taskRepository.save(task);
                }).switchIfEmpty(Mono.error(RuntimeException::new));
    }


    @Override
    public Mono<Void> deleteById(String id) {
        Flux<Task> tasks = findAll();

        return taskRepository.deleteById(id);
    }
}
