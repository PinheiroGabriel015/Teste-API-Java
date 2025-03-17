package ModuloProduto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Pojo.UsuarioPojo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de API rest do módulo de Produto")

public class ProdutoTest {
	private String token;

	@BeforeEach
	public void beforeEah() {
		baseURI = "http://165.227.93.41";
		// port = 8080; ProvavelPorta
		basePath = "/lojinha";
		
		UsuarioPojo usuario = new UsuarioPojo();
		usuario.setUsuarioLogin("admin");
		usuario.setUsuarioSenha("admin");
				
		this.token = 
			given().contentType(ContentType.JSON)
				.body(usuario)
			.when()
				.post("/v2/login")
			.then()
				.extract()
					.path("data.token");

	}

	@Test
	@DisplayName("Validar os limites do valor do Produto")
	public void testValidarLimitesProibidosValorProduto() {

		// Configurando os dados da API Rest da Lojinha

		// Obter Token do usuario Admin

		System.out.println(token);

		/*
		 * Tentar inserir um produto com valor 0.00 e validar que a mensagem de erro foi
		 * apresentada e que Status Code é 422
		 */

		given().header("token", token).contentType(ContentType.JSON)
				.body("{\r\n" + "  \"produtoNome\": \"Teste Api Java\",\r\n" + "  \"produtoValor\": 0.00,\r\n"
						+ "  \"produtoCores\": [\r\n" + "    \"branco\" , \"preto\"\r\n" + "  ],\r\n"
						+ "  \"produtoUrlMock\": \"\",\r\n" + "  \"componentes\": [\r\n" + "    {\r\n"
						+ "      \"componenteNome\": \"controle\",\r\n" + "      \"componenteQuantidade\": 2\r\n"
						+ "    },\r\n" + "    {\r\n" + "      \"componenteNome\": \"Jogo: Forza Horizon\",\r\n"
						+ "      \"componenteQuantidade\": 1\r\n" + "    }\r\n" + "  ]\r\n" + "}")
				.when()
					.post("/v2/produtos")
				.then()
					.assertThat()
						.body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00")).statusCode(422);

	}
}
