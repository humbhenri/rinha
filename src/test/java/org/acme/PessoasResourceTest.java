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
        var apelido = RandomStringGenerator.generateRandomString(32);
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
    public void nomeDeveSerMenorQue100() {
        var apelido = RandomStringGenerator.generateRandomString(30);
        var nome = RandomStringGenerator.generateRandomString(101);
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"" + nome + "\", \"nascimento\" : \"1985-01-01\", \"stack\" : [\"PHP\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(422);
    }

    @Test
    public void apelidoDeveSerMenor32() {
        var apelido = RandomStringGenerator.generateRandomString(33);
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"nome\", \"nascimento\" : \"1985-01-01\", \"stack\" : [\"PHP\", \"JAVA\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(422);
    }

    @Test
    public void apelidoObrigatorio() {
        var apelido = "";
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"nome\", \"nascimento\" : \"1985-01-01\", \"stack\" : [\"PHP\", \"JAVA\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(422);
    }

    @Test
    public void nomeObrigatorio() {
        var apelido = RandomStringGenerator.generateRandomString(30);
        var nome = "";
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"" + nome + "\", \"nascimento\" : \"1985-01-01\", \"stack\" : [\"PHP\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(422);
    }

    @Test
    public void nascimentoObrigatorio() {
        var apelido = RandomStringGenerator.generateRandomString(30);
        var nome = RandomStringGenerator.generateRandomString(45);
        var nascimento = "";
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"" + nome + "\", \"nascimento\" : \"" + nascimento + "\", \"stack\" : [\"PHP\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(422);
    }

    @Test
    public void nascimentoFormatoValido() {
        var apelido = RandomStringGenerator.generateRandomString(30);
        var nome = RandomStringGenerator.generateRandomString(45);
        var nascimento = "2000-23-01";
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"" + nome + "\", \"nascimento\" : \"" + nascimento + "\", \"stack\" : [\"PHP\"] }")
            .when().post("/pessoas")
            .then()
            .statusCode(422);
    }

    @Test
    public void stackCadaElementeDeveTerNoMax32() {
        var apelido = RandomStringGenerator.generateRandomString(30);
        var nome = RandomStringGenerator.generateRandomString(45);
        var nascimento = "2000-03-01";
        var stack = "[\"" + RandomStringGenerator.generateRandomString(33) + "\"]";
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"" + nome + "\", \"nascimento\" : \"" + nascimento + "\", \"stack\" : " + stack + " }")
            .when().post("/pessoas")
            .then()
            .statusCode(422);
    }

    @Test
    public void stackEhOpcional() {
        var apelido = RandomStringGenerator.generateRandomString(30);
        var nome = RandomStringGenerator.generateRandomString(45);
        var nascimento = "2000-03-01";
        var stack = "[]";
        given()
            .contentType("application/json")
            .body("{ \"apelido\" : \"" + apelido + "\", \"nome\" : \"" + nome + "\", \"nascimento\" : \"" + nascimento + "\", \"stack\" : " + stack + " }")
            .when().post("/pessoas")
            .then()
            .statusCode(201);
    }
}
