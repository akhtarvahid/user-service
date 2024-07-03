package com.example.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutDto {
    private String token;

    @Setter
    @Getter
    public static class SignUpResponseDto {
        private String name;
        private String email;
        private boolean isEmailVerified;
    }
}