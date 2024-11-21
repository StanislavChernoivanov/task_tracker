package com.example.task_tracker.web.model.response;

import com.example.task_tracker.model.entity.TaskStatus;
import com.example.task_tracker.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Array;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private String id;
    private String name;
    private String description;
    private TaskStatus status;
    private User author;
    private User assignee;
    private Set<User> observers = new HashSet<>();
}
