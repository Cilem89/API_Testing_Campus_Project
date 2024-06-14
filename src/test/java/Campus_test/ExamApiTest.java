package Campus_test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ExamApiTest {

    private String accessToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://test.mersys.io";

        Response response = given()
                .contentType("application/json")
                .body("{ \"username\": \"turkeyts\", \"password\": \"TechnoStudy123\" }")
                .post("/auth/login");

        response.then().statusCode(200);

        accessToken = response.jsonPath().getString("access_token");
    }

    @Test
    public void testCreateExam() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body("{ \"name\": \"Midterm Exam\", \"date\": \"2023-11-01\", \"description\": \"This is a midterm exam\" }")
                .when()
                .post("/school-service/api/exam")
                .then()
                .statusCode(201)
                .body("name", equalTo("Midterm Exam"))
                .body("date", equalTo("2023-11-01"))
                .body("description", equalTo("This is a midterm exam"));
    }

    @Test
    public void testEditExam() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body("{ \"name\": \"Final Exam\", \"date\": \"2023-12-01\", \"description\": \"This is a final exam\" }")
                .when()
                .put("/school-service/api/exam/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Final Exam"))
                .body("date", equalTo("2023-12-01"))
                .body("description", equalTo("This is a final exam"));
    }

    @Test
    public void testDeleteExam() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("/school-service/api/exam/1")
                .then()
                .statusCode(204);
    }

    @Test
    public void testCreateExamMissingFields() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body("{ \"name\": \"Midterm Exam\" }")  // Missing date and description
                .when()
                .post("/school-service/api/exam")
                .then()
                .statusCode(400);
    }

    @Test
    public void testEditExamInvalidId() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body("{ \"name\": \"Final Exam\", \"date\": \"2023-12-01\", \"description\": \"This is a final exam\" }")
                .when()
                .put("/school-service/api/exam/invalid-id")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteExamInvalidId() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("/school-service/api/exam/invalid-id")
                .then()
                .statusCode(404);
    }
}