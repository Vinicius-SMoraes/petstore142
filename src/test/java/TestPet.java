// 1 - Bibliotecas

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
 
import io.restassured.response.Response; // Classe resposta do RestAssured


import static io.restassured.RestAssured.given; // Função Given 
import static org.hamcrest.Matchers.*;

// 2 - Classes
public class TestPet {
    //2.1 atributos 
    static String ct = "application/json"; // Content type da request
    static String uriPet = "https://petstore.swagger.io/v2/pet"; // Base de base + o endpoint do User 
    static int petId = 394539101;


    //2.2 funções e metodos 
    //2.2.1 Funções e métodos comuns / uteis 

    // Função de leitura do Json
    public static String lerArquivoJson(String arquivoJson) throws IOException{
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    // 2.2.2 Métodos de teste
    @Test
    public void testPostPet() throws IOException{
        // Carregar os dados do arquivo Json do pet
       
        String jsonBody = lerArquivoJson("src/test/resources/json/pet1.json");
    
        // A partir daqui começa o teste via Rest-Assured
        given()                                                             // Dado que 
            .contentType(ct)                                                // O tipo do conteúdo é 
            .log().all()                                                    // Mostre tudo na lista
            .body(jsonBody)                                                 // Envie o corpo da execução 
        // Executa
        .when()                                                             // Quando 
            .post(uriPet)                                                   // Chamamos o endpoint usando o verbo POST
        // Valida
        .then()                                                             // Então 
            .log().all()                                                    // Mostre tudo no retorno
            .statusCode(200)                             // Verifique se o status code é = 200
            .body("name", is("Pipoca"))                          // verifique se o nome do cachorro é pipoca
            .body("id", is(petId))                                     // Verifique qual o código do Pet 
            .body("category.name", is("cachorro"))               // Verifique se é cachorro 
            .body("tags[0].name", is("vacinado"))                // Verifique se está vacinado 
        ;                                                                   // Fim do given 
    }

    @Test
    public void testGetPet(){
        // Configura 
        // Entrada 
        


    }

}
