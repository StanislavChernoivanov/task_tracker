package com.example.task_tracker.web.model.response;

import com.example.task_tracker.model.entity.TaskStatus;
import com.example.task_tracker.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<User> observers = new ArrayList<>();
}
