package tests;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.LoginPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static helpers.Endpoints.REGISTER;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.Specs.baseRequestSpec;
import static specs.Specs.baseResponseSpecCode200;

@Tags({@Tag("api"),@Tag("auth")})
@Owner(value = "Santalova Polina")
public class AuthApiTests {

    @Test
    @DisplayName("Check registration token")
    @Severity(SeverityLevel.BLOCKER)
    void checkRegistrationToken() {

        step("Verify registration token", () -> {
            LoginPayload data = new LoginPayload();
            data.setEmail("eve.holt@reqres.in");
            data.setPassword("pistol");
            LoginPayload.LoginResponse response = given()
                    .spec(baseRequestSpec)
                    .body(data)
                    .when()
                    .post(REGISTER)
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(LoginPayload.LoginResponse.class);

            assertThat(response.getToken()).isNotNull();
        });
    }



}
