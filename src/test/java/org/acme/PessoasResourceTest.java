package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import java.util.*;

@QuarkusTest
public class PessoasResourceTest {
  
    @Test
    public void testNomeDeveSerString() {
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"apelido\", \"nome\" : 1, \"nascimento\" : \"1985-01-01\", \"stack\" : null }")
            .when().post("/pessoas")
            .then()
            .statusCode(400);
    }

    @Test
    public void testStackDeveSerArrayStrings() {
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"apelido\", \"nome\" : \"nome\", \"nascimento\" : \"1985-01-01\", \"stack\" : [1, \"PHP\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(400);
    }

    @Test
    public void testNomeNaoPodeSerNulo() {
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"apelido\", \"nome\" : null, \"nascimento\" : \"1985-01-01\", \"stack\" : [1, \"PHP\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(400);
    }

    @Test
    public void testApelidoNaoPodeSerRepetido() {
        var apelido = UUID.randomUUID().toString();
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"nome\", \"nascimento\" : \"1985-01-01\", \"stack\" : [\"PHP\", \"JAVA\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(201);

        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"nome\", \"nascimento\" : \"1985-01-01\", \"stack\" : [\"PHP\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(422);
    }

    @Test 
    public void test404PessoaNaoEncontrada() {
      var uuid = UUID.randomUUID();
      given()
        .pathParam("uuid", uuid)
        .when().get("/pessoas/{uuid}")
        .then()
        .statusCode(404);
    }

    @Test
    public void testPesquisa() {
        var apelido = UUID.randomUUID().toString();
        //insert
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"nome\", \"nascimento\" : \"1985-01-01\", \"stack\" : [\"PHP\", \"JAVA\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(201);
        given()
            .queryParam("t", apelido)
            .when().get("/pessoas")
            .then()
            .statusCode(200);
    }

}
