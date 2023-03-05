package models;

import lombok.Data;

public @Data class CreateUpdateUserPayload {

    private String name;
    private String job;

    public static @Data class CreateUserResponse {

        private String name;
        private String job;
        private String id;
        private String createdAt;
    }

    public static @Data class UpdateUserResponse {

        private String name;
        private String job;
        private String updatedAt;
    }
}

