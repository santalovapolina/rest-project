package reqresin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;


public class ApiTests {

    public static final String BASE_URL = "https://reqres.in/";

    @DisplayName("Verify the total users number")
    @Test
    void checkUsersNumber() {

        given()
                .log().uri()
                .when()
                .get(BASE_URL + "api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12));

    }


    @DisplayName("Verify single user data")
    @Test
    void checkSingleUser() {

        given()
                .log().uri()
                .when()
                .get(BASE_URL + "api/users/2")
                .then()
                .log().status()
                .log().body()
                .body("data.id", equalTo(2),
                        "data.email", equalTo("janet.weaver@reqres.in"),
                        "data.first_name", equalTo("Janet"),
                        "data.last_name", equalTo("Weaver"));
    }

    @DisplayName("Verify created user data")
    @Test
    void createUser() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post(BASE_URL + "api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", equalTo("morpheus"),
                "job", equalTo("leader"));
    }

    @DisplayName("Verify updated user data")
    @Test
    void updateUser() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .put(BASE_URL + "api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", equalTo("morpheus"),
                "job", equalTo("zion resident"));
    }

    @DisplayName("Verify registration token")
    @Test
    void successfulRegistration() {

        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
        String token = "QpwL5tke4Pnpja7X4";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post(BASE_URL + "api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is(token));
    }

}




