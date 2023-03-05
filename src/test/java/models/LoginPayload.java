package models;

import lombok.Data;

public @Data class LoginPayload {

    private String email;
    private String password;

    public static @Data class LoginResponse {

        private String id;
        private String token;

    }
}
