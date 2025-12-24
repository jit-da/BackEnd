package com.jitda.api.controller.user.dto.response;

import com.jitda.domain.users.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {
    private String email;
    private String name;
    private String phone;
    private String gradeName;
    private String imageUrl;
    private int point;

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }

    public static class UserResponseBuilder {
        private String email;
        private String name;
        private String phone;
        private String gradeName;
        private String imageUrl;
        private int point;

        public UserResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserResponseBuilder gradeName(String gradeName) {
            this.gradeName = gradeName;
            return this;
        }

        public UserResponseBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public UserResponseBuilder point(int point) {
            this.point = point;
            return this;
        }

        public UserResponse build() {
            UserResponse userResponse = new UserResponse();
            userResponse.email = this.email;
            userResponse.name = this.name;
            userResponse.phone = this.phone;
            userResponse.gradeName = this.gradeName;
            userResponse.imageUrl = this.imageUrl;
            userResponse.point = this.point;
            return userResponse;
        }
    }

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .gradeName(user.getGrade() != null && user.getGrade().getName() != null 
                        ? user.getGrade().getName().name() : null)
                .imageUrl(user.getImageUrl())
                .point(user.getPoint())
                .build();
    }
}
