package Campus_test;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RedirectSpecification;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class US102_City_Test {
   RequestSpecification reqSpec;
    Faker randomUreteci = new Faker();
   String  Cityname ="";

   String id="";

    @BeforeClass
    public void LoginCampus() {
        baseURI = "https://test.mersys.io/";

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
    public void GetCities(){

        given()
                .spec(reqSpec)
                .when()
                .get("school-service/api/cities")
                .then()
                .log().body()
                .statusCode(200)
                ;
    }

    @Test
    public void CreateCity(){
        Cityname = randomUreteci.address().cityName();

        Map<String, String> newCity = new HashMap<>();
        newCity.put("name", Cityname);

       id=
                given()
                        .spec(reqSpec)
                        .body(newCity)
                        .when()
                        .post("/school-service/api/cities/search")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().asString();

    }

    @Test(dependsOnMethods ="CreateCity" )
    public void UpdateCity(){

        String updateCityName = "Maria"+ randomUreteci.address().cityName();

        Map<String, String> updateCity = new HashMap<>();
        updateCity.put("name", updateCityName);
        updateCity.put("id",id);

        given()
                .spec(reqSpec)
                .body(updateCity)
                .when()
                .put("/school-service/api/cities")
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(updateCityName));


    }

    @Test

    public void DeleteCity(){

        given()

                .spec(reqSpec)
                .when()
                .delete("/school-service/api/cities/"+id)
                .then()
                .statusCode(200)
                ;
    }
}
