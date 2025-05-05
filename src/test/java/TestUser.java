// 1- Bibliotecas

import  io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;


public class TestUser {
    static String ct = "application/json";
    static String uriUser = "https://petstore.swagger.io/v2/user";
    static String token;

    @Test
    public static String testLogin(){
        // Configura 
        // Executa 
        // Verifica

        // Configuração
        String username = "Vinicius";
        String password = "12345678";

        String resultadoEsperado = "logged in user session:";

        Response resposta = (Response) given()
            .contentType(ct)
            .log().all()
        .when() // Executar 
            .get(uriUser + "/login?username=" + username + "&password=" + password)
        .then()
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", containsString(resultadoEsperado)) // Contem 
            .body("message", hasLength(36))
        .extract();
        ;

        // extração 
        String token = resposta.jsonPath().getString("message").substring(23);
        System.out.println("Conteudo do token: " + token);
        return(token);
    }
}
