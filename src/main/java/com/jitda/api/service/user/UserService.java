package com.jitda.api.service.user;

import com.jitda.api.controller.auth.dto.request.LoginRequest;
import com.jitda.api.controller.auth.dto.request.SignUpRequest;
import com.jitda.api.controller.auth.dto.response.TokenResponse;
import com.jitda.api.controller.user.dto.response.UserResponse;
import com.jitda.domain.users.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface UserService {
    void signUp(SignUpRequest request);

    TokenResponse login(LoginRequest request, HttpServletResponse response);

    TokenResponse reissue(HttpServletRequest request, HttpServletResponse response);

    UserResponse getUserInfo(User user);

    void logout(HttpServletRequest request);

    void deleteUser(User user);
}
