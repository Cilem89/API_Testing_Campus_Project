package Campus_test;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class US002_CreateCountryWithStates {

    RequestSpecification reqSpec;
    Faker random = new Faker();
    String name="";
    String code="";

    @BeforeClass
    public void LoginCampus() {
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
    public void CreateCountryWithStates(){

        name = random.address().country() + random.address().countryCode()
                + random.address().latitude();
        code = random.address().countryCode();

        Map<String, String> newCountry = new HashMap<>();
        newCountry.put("name", name);
        newCountry.put("code", code);

        given()
                .spec(reqSpec)
                .body(newCountry)
                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                ;
    }





}
