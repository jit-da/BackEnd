package com.jitda.api.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jitda.api.controller.auth.dto.response.TokenResponse;
import com.jitda.api.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthControllerV1 테스트")
class AuthControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_Success() throws Exception {
        // given
        String requestBody = """
            {
                "email": "test@example.com",
                "password": "Test1234!@#",
                "passwordConfirm": "Test1234!@#",
                "name": "테스트",
                "phone": "010-1234-5678",
                "agreePrivacy": "Y",
                "agreeUniqueInfo": "Y",
                "agreeService": "Y"
            }
            """;

        willDoNothing().given(userService).signUp(any());

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 형식 오류")
    void signUp_Fail_InvalidEmail() throws Exception {
        // given
        String requestBody = """
            {
                "email": "invalid-email",
                "name": "테스트",
                "agreePrivacy": "Y",
                "agreeUniqueInfo": "Y",
                "agreeService": "Y"
            }
            """;

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() throws Exception {
        // given
        String requestBody = """
            {
                "email": "test@example.com",
                "password": "Test1234!@#"
            }
            """;

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        given(userService.login(any(), any())).willReturn(tokenResponse);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 형식 오류")
    void login_Fail_InvalidEmail() throws Exception {
        // given
        String requestBody = """
            {
                "email": "invalid-email",
                "password": "Test1234!@#"
            }
            """;

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_Success() throws Exception {
        // given
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .build();

        given(userService.reissue(any(), any())).willReturn(tokenResponse);

        // when & then
        mockMvc.perform(post("/api/v1/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());
    }
}

