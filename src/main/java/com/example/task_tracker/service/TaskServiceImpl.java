package com.example.task_tracker.service;

import com.example.task_tracker.model.entity.Task;
import com.example.task_tracker.model.entity.User;
import com.example.task_tracker.model.repository.TaskRepository;
import com.example.task_tracker.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Flow;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{


    private final TaskRepository taskRepository;
    private final UserService userService;


    @Override
    public Flux<Task> findAll() {

        return taskRepository.findAll()
                .flatMap(task -> {
                    Set<String> observerIds = task.getObserverIds();
                    String authorId = task.getAuthorId();
                    String assigneeId = task.getAssigneeId();

                    Flux.zip(userService.findById(authorId),
                            userService.findById(assigneeId),
                            userService.findByIds(observerIds))
                            .flatMap(tuple -> {
                                List<User> users = List.of(tuple.getT1(), tuple.getT2(), tuple.getT3());
                                users.forEach(user -> {
                                    if(user != null && user.getId().equals(authorId)) {
                                        task.setAuthor(user);
                                    } else if (user != null && user.getId().equals(assigneeId)) {
                                        task.setAssignee(user);
                                    } else if (user != null && observerIds.contains(user.getId())) {
                                        task.getObservers().add(user);
                                    }
                                });;
                                return Mono.just(tuple);
                            });
                    return Mono.just(task);
                });
    }

    @Override
    public Mono<Task> findById(String id) {

        return taskRepository.findById(id)
                .flatMap(task -> {
                    Set<String> observerIds = task.getObserverIds();
                    String authorId = task.getAuthorId();
                    String assigneeId = task.getAssigneeId();

                    Flux.zip(userService.findById(authorId),
                                    userService.findById(assigneeId),
                                    userService.findByIds(observerIds))
                            .flatMap(tuple -> {
                                List<User> users = List.of(tuple.getT1(), tuple.getT2(), tuple.getT3());
                                users.forEach(user -> {
                                    if(user.getId().equals(authorId)) {
                                        task.setAuthor(user);
                                    } else if (user.getId().equals(assigneeId)) {
                                        task.setAssignee(user);
                                    } else if (observerIds.contains(user.getId())) {
                                        task.getObservers().add(user);
                                    }
                                });;
                                return Mono.just(tuple);
                            });
                    return Mono.just(task);
                });
    }





    @Override
    public Mono<Task> create(Task task) {

        task.setCreatedAt(Instant.now());
        task.setId(UUID.randomUUID().toString());

        return taskRepository.save(task)
                .map(t -> {
                    String authorId = t.getAuthorId(); // author's id
                    String assigneeId = t.getAssigneeId(); // assignee's id

                    Mono.zip(userService.findById(authorId), userService.findById(assigneeId)) // zip 2 queries, find user-author and find user-assignee
                            .map(tuple -> {
                                List<User> users = List.of(tuple.getT1(), tuple.getT2()); // list of user-author and user-assignee
                                users.forEach(user -> {
                                    if (user.getId().equals(authorId)) {
                                        task.setAuthor(user); // set author in task
                                    }

                                    if (user.getId().equals(assigneeId)) {
                                        task.setAssignee(user); // set assignee in task
                                    }
                                });
                                return tuple;
                            });
                    return t; // It must be task with set author and assignee, but it's not
                });
    }



    @Override
    public Mono<Task> update(String id, Task task) {
        return findById(id)
                .flatMap(task1 -> {
                    EntityUtils.updateEntity(task, task1);
                    task1.setUpdatedAt(Instant.now());

                    return taskRepository.save(task1);
                });

    }




    @Override
    public Mono<Task> addObserver(String userId) {
        User user = userService.findById(userId).block();

        return findById(userId)
                .flatMap(task -> {
                    task.getObservers().add(user);
                    assert user != null;
                    task.getObserverIds().add(user.getId());

                return taskRepository.save(task);
                });
    }




    @Override
    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }
}
