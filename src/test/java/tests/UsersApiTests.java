package tests;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.CreateUpdateUserPayload;
import models.ListUsersResponse;
import models.SingleUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static helpers.Endpoints.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static specs.Specs.*;


@Tags({@Tag("api"), @Tag("user")})
@Owner(value = "Santalova Polina")
public class UsersApiTests {


    @Test
    @DisplayName("Check total users number")
    @Severity(SeverityLevel.NORMAL)
    void checkTotalUsersNumber() {

        step("Verify total users number", () -> {
            given()
                    .queryParam("page", "2")
                    .spec(baseRequestSpec)
                    .when()
                    .get(LIST_USERS)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .body("total", equalTo(12));
        });
    }


    @Test
    @DisplayName("Check single user data")
    @Severity(SeverityLevel.CRITICAL)
    void checkSingleUserData() {

        step("Verify single user by id, email, first and last name", () -> {
            SingleUserResponse data = given()
                    .spec(baseRequestSpec)
                    .when()
                    .get(SINGLE_USER)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(SingleUserResponse.class);

            assertEquals(2, data.getUser().getId());
            assertEquals("janet.weaver@reqres.in", data.getUser().getEmail());
            assertEquals("Janet", data.getUser().getFirstName());
            assertEquals("Weaver", data.getUser().getLastName());
        });
    }


    @Test
    @DisplayName("Check created user data")
    @Severity(SeverityLevel.CRITICAL)
    void checkCreatedUserData() {

        String userName = "Elon Musk";
        String userJob = "professional martian";

        step("Verify created user data", () -> {
            CreateUpdateUserPayload data = new CreateUpdateUserPayload();
            data.setName(userName);
            data.setJob(userJob);
            CreateUpdateUserPayload.CreateUserResponse response = given()
                    .spec(baseRequestSpec)
                    .body(data)
                    .when()
                    .post(USERS)
                    .then()
                    .spec(baseResponseSpecCode201)
                    .extract().as(CreateUpdateUserPayload.CreateUserResponse.class);

            assertThat(response.getName()).isEqualTo(userName);
            assertThat(response.getJob()).isEqualTo(userJob);
        });
    }


    @Test
    @DisplayName("Check updated user data")
    @Severity(SeverityLevel.NORMAL)
    void checkUpdatedUserData() {

        String userName = "Moe";
        String userJob = "Bartender";

        step("Verify updated user data", () -> {
            CreateUpdateUserPayload data = new CreateUpdateUserPayload();
            data.setName(userName);
            data.setJob(userJob);
            CreateUpdateUserPayload.UpdateUserResponse response = given()
                    .spec(baseRequestSpec)
                    .body(data)
                    .when()
                    .put(SINGLE_USER)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(CreateUpdateUserPayload.UpdateUserResponse.class);

            assertThat(response.getName()).isEqualTo(userName);
            assertThat(response.getJob()).isEqualTo(userJob);
        });
    }


    @Test
    @DisplayName("Check deleted user")
    @Severity(SeverityLevel.NORMAL)
    void checkDeletedUser() {

        step("Verify deleted user", () -> {
            given()
                    .spec(baseRequestSpec)
                    .when()
                    .delete(SINGLE_USER)
                    .then()
                    .spec(baseResponseSpecCode204);
        });
    }


    @Test
    @DisplayName("Check user emails end with domain")
    @Severity(SeverityLevel.NORMAL)
    void checkUserEmailsEndWithDomain() {

        step("Verify user emails end with domain", () -> {
            List<ListUsersResponse> users = given()
                    .queryParam("page", "2")
                    .spec(baseRequestSpec)
                    .when()
                    .get(LIST_USERS)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().body().jsonPath().getList("data", ListUsersResponse.class);

            users.forEach(x -> assertTrue(x.getEmail().endsWith("reqres.in")));
        });
    }


    @Test
    @DisplayName("Check user by email")
    @Severity(SeverityLevel.NORMAL)
    void checkUserByEmail() {

        step("Verify user by email", () -> {
            given()
                    .queryParam("page", "2")
                    .spec(baseRequestSpec)
                    .when()
                    .get(LIST_USERS)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                            hasItem("rachel.howell@reqres.in"));
        });
    }


    @Test
    @DisplayName("Check user by last name")
    @Severity(SeverityLevel.NORMAL)
    void checkUserByLastName() {

        step("Verify user last name", () -> {
            Response response = given()
                    .queryParam("page", "2")
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


    @Test
    @DisplayName("Check user email by id")
    @Severity(SeverityLevel.NORMAL)
    void checkUserEmailById() {

        step("Verify user email by id", () -> {
            given()
                    .queryParam("page", "2")
                    .spec(baseRequestSpec)
                    .when()
                    .get(LIST_USERS)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .body("data.find{it.id == 9}.email", is("tobias.funke@reqres.in"));
        });
    }


    @Test
    @DisplayName("Check year by name")
    @Severity(SeverityLevel.NORMAL)
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




