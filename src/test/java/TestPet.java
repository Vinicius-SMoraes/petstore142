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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.google.gson.Gson;





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
            .header("","api_key: " + TestUser.testLogin())
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

    // Data driven Testing (DDT) / Teste direcionado por dados / Teste com massa
    // Teste com Json parametrizado 

    @ParameterizedTest @Order(5)
    @CsvFileSource(resources = "/CSV/petMassa.csv", numLinesToSkip = 1, delimiter = ',')
    public void testPostPetDDT(
        int petId,
        String petName,
        int categoryId,
        String categoryName,
        String status1,
        String status2
    ) //Fim dos parametros 
    { // Inicio do código do método testPostPetDDT

        //Criar a classe pet para receber os dados do CSV
        Pet pet = new Pet(); //Instancia a classe User
        Pet.Category category = pet.new Category(); // instanciar a subclasse Category
        Pet.tag[] tags = new Pet.tag[2];
        tags[0] = pet.new tag();
        tags[1] = pet.new tag();

        pet.id = petId; 
        pet.category = category;
        pet.name = petName;
        pet.category.id = categoryId;
        pet.category.name = categoryName;
        // pet.photoUrls não precisa ser incluido pois será vazio
        pet.tags = tags;
        pet.tags[0].id = 9;
        pet.tags[0].name = "vacinado";
        pet.tags[1].id = 8;
        pet.tags[1].name = "vermifugado";
        pet.status = status1; 

        // Criar um Json para o Body a ser enviado a partir da classe Pet e do CSV 

        Gson gson = new Gson(); // Instancia a classe Gson como o objeto gson
        String jsonBody = gson.toJson(pet);

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)

        .when()
            .post(uriPet)

        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(petId))
            .body("name", is(petName))
            .body("category.id", is(categoryId))
            .body("category.name", is(categoryName))
            .body("status", is(status1))
        ;

    }

}
