package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

public class Specs {

    public static RequestSpecification baseRequestSpec = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .log().uri()
            .log().headers()
            .log().body()
            .filter(withCustomTemplates())
            .contentType(ContentType.JSON);

    public static ResponseSpecification baseResponseSpecCode200 = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(200)
            .build();

    public static ResponseSpecification baseResponseSpecCode201 = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(201)
            .build();

    public static ResponseSpecification baseResponseSpecCode204 = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(204)
            .build();

}
