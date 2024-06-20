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
import static org.hamcrest.Matchers.equalTo;

public class US101_StatesTest {

    RequestSpecification reqSpec;
    Faker randomUreteci = new Faker();
    String name="";
    String shortName="";
    String id="";
    String countryID="{\"id\": \"63363e0665423443c6d1300e\"}";


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
    public void getStates() {

        given()
                .spec(reqSpec)
                .when()
                .get("/school-service/api/states")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "getStates")
    public void createStates() {

        name = randomUreteci.address().country() + randomUreteci.address().countryCode()
                + randomUreteci.address().latitude();
        shortName = randomUreteci.address().countryCode();

        Map<String, String> newState = new HashMap<>();
        newState.put("name", name);
        newState.put("shortName", shortName);
        newState.put("country_id", countryID);


        id=
                given()
                        .spec(reqSpec)
                        .body(newState)
                        .when()
                        .post("/school-service/api/states")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().asString()
        ;
    }

    @Test(dependsOnMethods = "createStates")
    public void updateStates(){

        String  updateName = "alev" +randomUreteci.address().country() + randomUreteci.address().countryCode()
                + randomUreteci.address().latitude();

        Map<String, String> updStates = new HashMap<>();
        updStates.put("id", id);
        updStates.put("name", updateName);
        updStates.put("shortName", shortName);

        given()
                .spec(reqSpec)
                .body(updStates)
                .when()
                .put("/school-service/api/states")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(updateName))
        ;
    }

    @Test(dependsOnMethods = "updateStates")
    public void deleteStates(){

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/states/"+id)

                .then()
                .statusCode(200)
        ;




    }

}
