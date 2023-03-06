package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class CreateUpdateUserPayload {

    private String name;
    private String job;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static @Data class CreateUserResponse {

        private String name;
        private String job;
        private String id;
        private String createdAt;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static @Data class UpdateUserResponse {

        private String name;
        private String job;
        private String updatedAt;
    }
}

