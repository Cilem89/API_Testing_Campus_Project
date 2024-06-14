package Campus_test;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class US001_Login_Test {
    RequestSpecification reqSpec;

    @Test
    public void LoginTest() {
        baseURI = "https://test.mersys.io";

        Map<String, String> userCredMap = new HashMap<>();
        userCredMap.put("username", "turkeyts");
        userCredMap.put("password", "TechnoStudy123");
        userCredMap.put("rememberMe", "true");


        Cookies gelenCookies =
                given()
                        .body(userCredMap)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        reqSpec = new RequestSpecBuilder()
                .addCookies(gelenCookies)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void InvalidLoginTest(){

        Map<String, String> userCredMap = new HashMap<>();
        userCredMap.put("username", "turkeyt");
        userCredMap.put("password", "TechnoStudy1");
        userCredMap.put("rememberMe", "true");

                given()

                        .body(userCredMap)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(403);
    }

}
