package com.jitda.api.service.user.impl;

import com.jitda.api.controller.auth.dto.request.LoginRequest;
import com.jitda.api.controller.auth.dto.request.SignUpRequest;
import com.jitda.api.controller.auth.dto.response.TokenResponse;
import com.jitda.api.controller.user.dto.response.UserResponse;
import com.jitda.domain.grade.entity.Grade;
import com.jitda.domain.grade.entity.GradeName;
import com.jitda.domain.grade.repository.GradeRepository;
import com.jitda.domain.users.entity.Provider;
import com.jitda.domain.users.entity.Role;
import com.jitda.domain.users.entity.User;
import com.jitda.domain.users.repository.UserRepository;
import com.jitda.domain.common.YN;
import com.jitda.global.config.jwt.service.JwtService;
import com.jitda.test.util.TestHelper;
import com.jitda.global.config.redis.service.RedisService;
import com.jitda.global.response.exception.BadRequestException;
import com.jitda.global.response.exception.ConflictException;
import com.jitda.global.response.exception.NotFoundException;
import com.jitda.global.response.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl 테스트")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RedisService redisService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private UserServiceImpl userService;

    private User createTestUser() {
        Grade grade = Grade.builder()
                .id("grade-1")
                .name(GradeName.NONE)
                .build();

        return User.builder()
                .email("test@example.com")
                .password("encoded-password")
                .name("테스트")
                .phone("010-1234-5678")
                .provider(Provider.LOCAL)
                .role(Role.USER)
                .grade(grade)
                .build();
    }

    private SignUpRequest createSignUpRequest() {
        return TestHelper.createSignUpRequest(
                "test@example.com",
                "Test1234!@#",
                "Test1234!@#",
                "테스트",
                "010-1234-5678",
                YN.Y,
                YN.Y,
                YN.Y
        );
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_Success() {
        // given
        SignUpRequest request = createSignUpRequest();
        Grade grade = Grade.builder()
                .id("grade-1")
                .name(GradeName.NONE)
                .build();

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(gradeRepository.findByName(GradeName.NONE)).willReturn(Optional.of(grade));
        given(passwordEncoder.encode(anyString())).willReturn("encoded-password");
        given(userRepository.save(any(User.class))).willReturn(createTestUser());

        // when
        userService.signUp(request);

        // then
        verify(userRepository).findByEmail(request.getEmail());
        verify(gradeRepository).findByName(GradeName.NONE);
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void signUp_Fail_DuplicateEmail() {
        // given
        SignUpRequest request = createSignUpRequest();
        User existingUser = createTestUser();

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(existingUser));

        // when & then
        assertThatThrownBy(() -> userService.signUp(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining(ExceptionCode.DUPLICATE_EMAIL.getMessage());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 불일치")
    void signUp_Fail_PasswordMismatch() {
        // given
        SignUpRequest request = TestHelper.createSignUpRequest(
                "test@example.com",
                "Test1234!@#",
                "Different123!@#",  // 비밀번호 불일치
                "테스트",
                "010-1234-5678",
                YN.Y,
                YN.Y,
                YN.Y
        );

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.signUp(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionCode.UNMATCHED_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        LoginRequest request = TestHelper.createLoginRequest("test@example.com", "Test1234!@#");

        User user = createTestUser();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtService.createAccessToken(anyString(), anyString())).willReturn("access-token");
        given(jwtService.createRefreshToken(anyString())).willReturn("refresh-token");

        // when
        TokenResponse response = userService.login(request, httpServletResponse);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(jwtService).createAccessToken(user.getEmail(), Provider.LOCAL.name());
        verify(jwtService).createRefreshToken(user.getEmail());
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    void login_Fail_UserNotFound() {
        // given
        LoginRequest request = TestHelper.createLoginRequest("test@example.com", "Test1234!@#");

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.login(request, httpServletResponse))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(ExceptionCode.NOT_FOUND_USER.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_Fail_PasswordMismatch() {
        // given
        LoginRequest request = TestHelper.createLoginRequest("test@example.com", "WrongPassword123!@#");

        User user = createTestUser();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.login(request, httpServletResponse))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionCode.UNMATCHED_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("회원 정보 조회 성공")
    void getUserInfo_Success() {
        // given
        User user = createTestUser();
        User userWithGrade = createTestUser();

        given(userRepository.findByIdWithGrade(user.getId())).willReturn(Optional.of(userWithGrade));

        // when
        UserResponse response = userService.getUserInfo(user);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getName()).isEqualTo(user.getName());
        verify(userRepository).findByIdWithGrade(user.getId());
    }

    @Test
    @DisplayName("회원 정보 조회 실패 - 사용자 없음")
    void getUserInfo_Fail_UserNotFound() {
        // given
        User user = createTestUser();
        given(userRepository.findByIdWithGrade(user.getId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserInfo(user))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(ExceptionCode.NOT_FOUND_USER.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteUser_Success() {
        // given
        User user = createTestUser();
        willDoNothing().given(userRepository).delete(any(User.class));

        // when
        userService.deleteUser(user);

        // then
        verify(userRepository).delete(user);
    }
}

