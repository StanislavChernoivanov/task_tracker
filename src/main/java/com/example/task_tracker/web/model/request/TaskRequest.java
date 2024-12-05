package com.example.task_tracker.web.model.request;

import com.example.task_tracker.model.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    private String name;
    private String description;
    private TaskStatus status;
    private String authorId;
    private String assigneeId;
}
