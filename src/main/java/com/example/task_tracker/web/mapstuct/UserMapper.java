package com.example.task_tracker.web.mapstuct;

import com.example.task_tracker.model.entity.User;
import com.example.task_tracker.web.model.request.UserListResponse;
import com.example.task_tracker.web.model.request.UserRequest;
import com.example.task_tracker.web.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UserRequest USerRequest);

    @Mapping(source = "userId", target = "id")
    User requestToUser(String userId, UserRequest USerRequest);

    UserResponse userToResponse(User user);


    List<UserResponse> userListToListResponse(List<User> users);

    default UserListResponse userListToUserListResponse(List<User> users) {
        var userListResponse = new UserListResponse();
        userListResponse.setUsers(userListToListResponse(users));

        return userListResponse;
    }

}
