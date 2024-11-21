package com.example.task_tracker.web.mapstuct;

import com.example.task_tracker.model.entity.Task;
import com.example.task_tracker.web.model.request.TaskRequest;
import com.example.task_tracker.web.model.response.TaskListResponse;
import com.example.task_tracker.web.model.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task requestToTask(TaskRequest taskRequest);

    @Mapping(source = "taskId", target = "id")
    Task requestToTask(String taskId, TaskRequest taskRequest);

    TaskResponse taskToResponse(Task task);

    List<TaskResponse> taskListToListResponse(List<Task> tasks);

    default TaskListResponse taskListToTaskListResponse(List<Task> tasks) {
        var taskListResponse = new TaskListResponse();
        taskListResponse.setTasks(taskListToListResponse(tasks));

        return taskListResponse;
    }
}
