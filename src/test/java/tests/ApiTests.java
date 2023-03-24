package tests;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.CreateUpdateUserPayload;
import models.ListUsersResponse;
import models.LoginPayload;
import models.SingleUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;
import java.util.Map;

import static helpers.Endpoints.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static specs.Specs.*;


public class ApiTests {


    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check total users number")
    @Test
    void checkTotalUsersNumber() {
        step("Verify total users number", () -> {
            given()
                    .spec(baseRequestSpec)
                    .when()
                    .get(LIST_USERS)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .body("total", equalTo(12));
        });
    }

    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check single user data")
    @Test
    void checkSingleUserData() {
        step("Verify single user by id, email, first and last name", () -> {
            SingleUserResponse data = given()
                    .spec(baseRequestSpec)
                    .when()
                    .get(INTERACT_WITH_USER)
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
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check created user data")
    @Test
    void checkCreatedUserData() {
        step("Verify created user data", () -> {
            CreateUpdateUserPayload data = new CreateUpdateUserPayload();
            data.setName("Elon Musk");
            data.setJob("professional martian");
            CreateUpdateUserPayload.CreateUserResponse response = given()
                    .spec(baseRequestSpec)
                    .body(data)
                    .when()
                    .post(USERS)
                    .then()
                    .spec(baseResponseSpecCode201)
                    .extract().as(CreateUpdateUserPayload.CreateUserResponse.class);

            assertThat(response.getName()).isEqualTo("Elon Musk");
            assertThat(response.getJob()).isEqualTo("professional martian");
        });
    }

    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check updated user data")
    @Test
    void checkUpdatedUserData() {
        step("Verify updated user data", () -> {
            CreateUpdateUserPayload data = new CreateUpdateUserPayload();
            data.setName("Moe");
            data.setJob("Bartender");
            CreateUpdateUserPayload.UpdateUserResponse response = given()
                    .spec(baseRequestSpec)
                    .body(data)
                    .when()
                    .put(INTERACT_WITH_USER)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(CreateUpdateUserPayload.UpdateUserResponse.class);

            assertThat(response.getName()).isEqualTo("Moe");
            assertThat(response.getJob()).isEqualTo("Bartender");
        });
    }

    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Check registration token")
    @Test
    void checkRegistrationToken() {
        step("Verify registration token", () -> {
            LoginPayload data = new LoginPayload();
            data.setEmail("eve.holt@reqres.in");
            data.setPassword("pistol");
            LoginPayload.LoginResponse response = given()
                    .spec(baseRequestSpec)
                    .body(data)
                    .when()
                    .post(REGISTER_USER)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(LoginPayload.LoginResponse.class);

            assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        });
    }

    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check deleted user")
    @Test
    void checkDeletedUser() {
        step("Verify deleted user", () -> {
            given()
                    .spec(baseRequestSpec)
                    .when()
                    .delete(INTERACT_WITH_USER)
                    .then()
                    .spec(baseResponseSpecCode204);
        });
    }

    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check user emails end with domain")
    @Test
    void checkUserEmailsEndWithDomain() {
        step("Verify user emails end with domain", () -> {
            List<ListUsersResponse> users = given()
                    .spec(baseRequestSpec)
                    .when()
                    .get(LIST_USERS)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().body().jsonPath().getList("data", ListUsersResponse.class);

            users.forEach(x -> assertTrue(x.getEmail().endsWith("reqres.in")));
        });
    }

    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check user by email")
    @Test
    void checkUserByEmail() {
        step("Verify user by email", () -> {
            given()
                    .spec(baseRequestSpec)
                    .when()
                    .get(LIST_USERS)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                            hasItem("rachel.howell@reqres.in"));
        });
    }

    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check user by last name")
    @Test
    void checkUserByLastName() {
        step("Verify user last name", () -> {
            Response response = given()
                    .spec(baseRequestSpec)
                    .when()
                    .get(LIST_USERS)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().response();
            JsonPath jsonPath = response.jsonPath();
            List<String> emails = jsonPath.get("data.last_name");
            assertTrue(emails.contains("Ferguson"));
        });
    }

    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check user email by id")
    @Test
    void checkUserEmailById() {
        step("Verify user email by id", () -> {
            given()
                    .spec(baseRequestSpec)
                    .when()
                    .get(LIST_USERS)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .body("data.find{it.id == 9}.email", is("tobias.funke@reqres.in"));
        });
    }

    @Tag("api")
    @Owner(value = "Santalova Polina")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check year by name")
    @Test
    void checkYearByName() {
        step("Verify year by name", () -> {
            Response response = given()
                    .spec(baseRequestSpec)
                    .when()
                    .get(UNKNOWN)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().response();
            Map<String, Object> tigerlily = response.path("data.find{it.name == 'tigerlily'}");
            assertEquals(2004, tigerlily.get("year"));
        });
    }

}




