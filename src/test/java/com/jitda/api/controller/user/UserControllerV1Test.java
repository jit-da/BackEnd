package com.jitda.api.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jitda.api.controller.user.dto.response.UserResponse;
import com.jitda.api.service.user.UserService;
import com.jitda.domain.users.entity.Role;
import com.jitda.domain.users.entity.User;
import com.jitda.global.config.oauth2.CustomOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("UserControllerV1 테스트")
class UserControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private User createTestUser() {
        return User.builder()
                .email("test@example.com")
                .name("테스트")
                .phone("010-1234-5678")
                .role(Role.USER)
                .build();
    }

    private CustomOAuth2User createCustomOAuth2User(User user) {
        return new CustomOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey())),
                Collections.singletonMap("email", user.getEmail()),
                "email",
                user
        );
    }

    private void setAuthentication(User user) {
        CustomOAuth2User customOAuth2User = createCustomOAuth2User(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("회원 정보 조회 성공")
    void getUserInfo_Success() throws Exception {
        // given
        User user = createTestUser();
        setAuthentication(user);
        
        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();

        given(userService.getUserInfo(any(User.class))).willReturn(userResponse);

        // when & then
        mockMvc.perform(get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.name").value(user.getName()));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_Success() throws Exception {
        // given
        User user = createTestUser();
        setAuthentication(user);

        // when & then
        mockMvc.perform(post("/api/v1/users/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteUser_Success() throws Exception {
        // given
        User user = createTestUser();
        setAuthentication(user);

        // when & then
        mockMvc.perform(delete("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

