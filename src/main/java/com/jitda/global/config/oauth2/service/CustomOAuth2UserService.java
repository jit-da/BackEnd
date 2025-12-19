package com.jitda.global.config.oauth2.service;

import com.jitda.domain.grade.entity.Grade;
import com.jitda.domain.grade.entity.GradeName;
import com.jitda.domain.grade.repository.GradeRepository;
import com.jitda.domain.users.entity.Provider;
import com.jitda.domain.users.entity.Role;
import com.jitda.domain.users.entity.User;
import com.jitda.domain.users.repository.UserRepository;
import com.jitda.global.config.oauth2.CustomOAuth2User;
import com.jitda.global.config.oauth2.OAuth2Attributes;
import com.jitda.global.config.oauth2.userinfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

import static com.jitda.global.config.oauth2.OAuth2Attributes.getProvider;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Provider provider = getProvider(registrationId);

        log.info("registrationId={}", registrationId);
        log.info("userNameAttributeName={}", userNameAttributeName);
        log.info("provider={}", provider);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(provider, userNameAttributeName, attributes);

        OAuth2UserInfo oauth2UserInfo = oAuth2Attributes.getOauth2UserInfo();
        String email = oauth2UserInfo.getEmail();
        String nickname = oauth2UserInfo.getNickname();

        log.info("email={}", email);
        log.info("nickname={}", nickname);

        // 소셜 타입과 소셜 ID 로 조회된다면 이전에 로그인을 한 유저
        // DB 에 조회되지 않는다면 User 객체 생성 후 회원가입 진행
        Grade grade = gradeRepository.findByName(GradeName.NONE)
                .orElseThrow(() -> new IllegalArgumentException("기본 등급이 존재하지 않습니다."));

        User user = userRepository.findByEmail(email)
                .orElse(User.builder()
                        .email(email)
                        .role(Role.GUEST)
                        .provider(provider)
                        .name(nickname)
                        .grade(grade)
                        .build());

        userRepository.save(user);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes,
                oAuth2Attributes.getNameAttributeKey(),
                user
        );
    }
}
