// 1 - Bibliotecas

import static io.restassured.RestAssured.given; // Função Given 
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

// 2 - Classes
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPet {
    //2.1 atributos 
    static String ct = "application/json"; // Content type da request
    static String uriPet = "https://petstore.swagger.io/v2/pet"; // Base de base + o endpoint do User 
    static int petId = 394539101;
    String petName = "Pipoca";
    String categoryName = "cachorro";
    String tagName = "vacinado";
    String[] status = {"available","sold"};

    //2.2 funções e metodos 
    //2.2.1 Funções e métodos comuns / uteis 

    // Função de leitura do Json
    public static String lerArquivoJson(String arquivoJson) throws IOException{
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    // 2.2.2 Métodos de teste
    @Test @Order(1)
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
            .body("name", is(petName))                                 // verifique se o nome do cachorro é pipoca
            .body("id", is(petId))                                     // Verifique qual o código do Pet 
            .body("category.name", is(categoryName))                   // Verifique se é cachorro 
            .body("tags[0].name", is(tagName))                         // Verifique se está vacinado
            .body("status", is(status[0]))                             // Verifique o status do animal
        ;                                                                   // Fim do given 
    }

    @Test @Order(2)
    public void testGetPet(){
        // Configura 
        // Entrada - petId esta definido no nivel da classe 
        // Saídas / Resultados esperados

        given()
            .contentType(ct)
            .log().all()
            // Quando é get ou delete não tem a parte do body
        // Executa 
        .when()
            .get(uriPet + "/" + petId) // Aqui, estamos montando o Endpoint da URI juntamente com o petId
        // Valida 
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is(petName))
            .body("id", is(petId))
            .body("category.name", is(categoryName))
            .body("tags[0].name", is(tagName))
            .body("status", is(status[0]))
        ; // Fim do given
    }

    @Test @Order(3)
    public void testPutPet()throws IOException{
        // Configura 
        // Executa 
        // Verifica

        // Configura
        String jsonBody = lerArquivoJson("src/test/resources/json/pet2.json"); //Configura

        given() 
            .contentType(ct)
            .log().all()
            .body(jsonBody)

        .when() // Executa
            .put(uriPet)

        .then() //Verifica
            .log().all()
            .statusCode(200)
            .body("name", is(petName))
            .body("id", is(petId))
            .body("category.name", is(categoryName))
            .body("tags[0].name", is(tagName))
            .body("status", is(status[1]))
        ;
    }

    @Test @Order(4)
    public void testDeletePet(){
        // Configura 
        // Executa 
        // Verifica

        // Configuração
        given()
            .contentType(ct)
            .log().all()

        .when() // Executa
            .delete(uriPet + "/" + petId)

        .then() // Verifica
            .log().all()
            .statusCode(200)
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(petId)))

        
        ;
    }

}
