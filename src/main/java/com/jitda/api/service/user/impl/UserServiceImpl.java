package com.jitda.api.service.user.impl;

import com.jitda.api.controller.auth.dto.request.LoginRequest;
import com.jitda.api.controller.auth.dto.request.SignUpRequest;
import com.jitda.api.controller.auth.dto.response.TokenResponse;
import com.jitda.api.controller.user.dto.response.UserResponse;
import com.jitda.api.service.user.UserService;
import com.jitda.domain.grade.entity.Grade;
import com.jitda.domain.grade.entity.GradeName;
import com.jitda.domain.grade.repository.GradeRepository;
import com.jitda.domain.users.entity.Provider;
import com.jitda.domain.users.entity.User;
import com.jitda.domain.users.repository.UserRepository;
import com.jitda.global.config.jwt.service.JwtService;
import com.jitda.global.config.redis.service.RedisService;
import com.jitda.global.response.exception.BadRequestException;
import com.jitda.global.response.exception.ConflictException;
import com.jitda.global.response.exception.NotFoundException;
import com.jitda.global.response.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;
import com.jitda.domain.users.entity.Role;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisService redisService;

    @Override
    public void signUp(SignUpRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            if (existingUser.getRole() == Role.GUEST) {
                existingUser.updateName(request.getName());
                existingUser.updatePhone(request.getPhone());
                existingUser.setRole(Role.USER);
                if (request.getPassword() != null && !request.getPassword().isBlank()) {
                    existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
                }
                userRepository.save(existingUser);
            } else {
                throw new ConflictException(ExceptionCode.DUPLICATE_EMAIL);
            }
            return;
        }

        // 비밀번호가 제공된 경우에만 검증
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (!request.isPasswordMatch()) {
                throw new BadRequestException(ExceptionCode.UNMATCHED_PASSWORD);
            }

            Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$");
            Matcher matcher = pattern.matcher(request.getPassword());
            if (!matcher.matches()) {
                throw new BadRequestException(ExceptionCode.INVALID_PASSWORD_FORMAT);
            }
        }

        Grade grade = gradeRepository.findByName(GradeName.NONE).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_GRADE));

        User newUser = request.toEntity(passwordEncoder, grade);
        userRepository.save(newUser);
    }

    @Override
    public TokenResponse login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionCode.UNMATCHED_PASSWORD);
        }

        String accessToken = jwtService.createAccessToken(user.getEmail(), Provider.LOCAL.name());
        String refreshToken = jwtService.createRefreshToken(user.getEmail());

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        redisService.setValues(refreshToken, user.getEmail());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshTokenFromCookie(request)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_REFRESH_TOKEN));

        String email = redisService.getValues(refreshToken);
        if (email == null) {
            throw new BadRequestException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));

        String newAccessToken = jwtService.createAccessToken(email, user.getProvider().name());
        String newRefreshToken = jwtService.createRefreshToken(email);

        jwtService.sendAccessAndRefreshToken(response, newAccessToken, newRefreshToken);
        redisService.setValues(newRefreshToken, email);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserInfo(User user) {
        // Grade를 함께 조회하기 위해 fetch join 사용
        User userWithGrade = userRepository.findByIdWithGrade(user.getId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));
        
        return UserResponse.of(userWithGrade);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_ACCESS_TOKEN));

    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
