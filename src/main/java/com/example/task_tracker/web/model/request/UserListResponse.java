package com.example.task_tracker.web.model.request;

import com.example.task_tracker.web.model.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListResponse {

    List<UserResponse> users = new ArrayList<>();


}
