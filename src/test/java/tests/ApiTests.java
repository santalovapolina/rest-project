package tests;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.CreateUpdateUserPayload;
import models.ListUsersResponse;
import models.LoginPayload;
import models.SingleUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static specs.Specs.*;


public class ApiTests {

    @Tag("api")
    @DisplayName("Verify total users number")
    @Test
    void checkUsersNumber() {
        step("Verify total users number", () -> {
            given()
                    .spec(baseRequestSpec)
                    .when()
                    .get("/users?page=2")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .body("total", equalTo(12));
        });
    }

    @Tag("api")
    @DisplayName("Verify single user data")
    @Test
    void getSingleUser() {
        step("Verify single user data by id, email, first and last name", () -> {
            SingleUserResponse data = given()
                    .spec(baseRequestSpec)
                    .when()
                    .get("/users/2")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(SingleUserResponse.class);

            assertEquals(2, data.getUser().getId());
            assertEquals("janet.weaver@reqres.in", data.getUser().getEmail());
            assertEquals("Janet", data.getUser().getFirstName());
            assertEquals("Weaver", data.getUser().getLastName());
        });
    }


    @Tag("api")
    @DisplayName("Verify created user data")
    @Test
    void createNewUser() {
        step("Verify created user data", () -> {
            CreateUpdateUserPayload data = new CreateUpdateUserPayload();
            data.setName("morpheus");
            data.setJob("leader");
            CreateUpdateUserPayload.CreateUserResponse response = given()
                    .spec(baseRequestSpec)
                    .body(data)
                    .when()
                    .post("/users")
                    .then()
                    .spec(baseResponseSpecCode201)
                    .extract().as(CreateUpdateUserPayload.CreateUserResponse.class);

            assertThat(response.getName()).isEqualTo("morpheus");
            assertThat(response.getJob()).isEqualTo("leader");
            assertThat(response.getCreatedAt()).isNotNull();
            assertThat(response.getId()).isNotNull();
        });
    }

    @Tag("api")
    @DisplayName("Verify updated user data")
    @Test
    void updateUser() {
        step("Verify updated user data", () -> {
            CreateUpdateUserPayload data = new CreateUpdateUserPayload();
            data.setName("morpheus");
            data.setJob("zion resident");
            CreateUpdateUserPayload.UpdateUserResponse response = given()
                    .spec(baseRequestSpec)
                    .body(data)
                    .when()
                    .put("/users/2")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(CreateUpdateUserPayload.UpdateUserResponse.class);

            assertThat(response.getName()).isEqualTo("morpheus");
            assertThat(response.getJob()).isEqualTo("zion resident");
            assertThat(response.getUpdatedAt()).isNotNull();
        });
    }

    @Tag("api")
    @DisplayName("Verify registration token")
    @Test
    void successfulRegistration() {
        step("Verify registration token", () -> {
            LoginPayload data = new LoginPayload();
            data.setEmail("eve.holt@reqres.in");
            data.setPassword("pistol");
            LoginPayload.LoginResponse response = given()
                    .spec(baseRequestSpec)
                    .body(data)
                    .when()
                    .post("/register")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(LoginPayload.LoginResponse.class);

            assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        });
    }

    @Tag("api")
    @DisplayName("Verify deleted user")
    @Test
    void deleteUser() {
        step("Verify deleted user", () -> {
            given()
                    .spec(baseRequestSpec)
                    .when()
                    .delete("/users/2")
                    .then()
                    .spec(baseResponseSpecCode204);
        });
    }

    @DisplayName("Verify user's email using groovy")
    @Test
    void emailTestUsingGroovy() {
        step("Verify user's email using groovy", () -> {
            given()
                    .spec(baseRequestSpec)
                    .when()
                    .get("/users?page=2")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                            hasItem("rachel.howell@reqres.in"));
        });
    }

    @DisplayName("Verify user's email using jsonPath")
    @Test
    void checkResponseUsingJsonPath() {
        step("Verify user's email using jsonPath", () -> {
            Response response = given()
                    .spec(baseRequestSpec)
                    .when()
                    .get("/users?page=2")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().response();
            JsonPath jsonPath = response.jsonPath();
            List<String> emails = jsonPath.get("data.email");
            assertTrue(emails.contains("rachel.howell@reqres.in"));
        });
    }


    @DisplayName("Verify user's emails end with domain")
    @Test
    void checkUserEmailsEndWithDomain() {
        step("Verify user's emails end with domain", () -> {
            List<ListUsersResponse> users = given()
                    .spec(baseRequestSpec)
                    .when()
                    .get("/users?page=2")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().body().jsonPath().getList("data", ListUsersResponse.class);

            users.forEach(x -> assertTrue(x.getEmail().endsWith("reqres.in")));
        });
    }


}




