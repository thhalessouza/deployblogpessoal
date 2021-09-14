package br.org.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.org.generation.blogpessoal.model.Usuario;
import br.org.generation.blogpessoal.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private Usuario usuario;
	private Usuario usuarioUpdate;
	private Usuario  usuarioAdmin;
	
	@BeforeAll
	public void start() throws ParseException {
		
		LocalDate dataAdmin = LocalDate.parse("1997-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));	
		usuarioAdmin = new Usuario (0L, "Administrador", "admin@gmail.com", "admin123", dataAdmin);
		
		if(!usuarioRepository.findByUsuario(usuarioAdmin.getUsuario()).isPresent())	{
			
			HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuarioAdmin);
			testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);
			
		}
		
		LocalDate dataPost = LocalDate.parse("1997-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));	
		usuario  = new Usuario (0L, "João Antunes", "joao@gmail.com", "12345678", dataPost);
		
		LocalDate dataPut = LocalDate.parse("1997-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));	
		usuarioUpdate = new Usuario (2L, "João Antunes de Souza", "joao_souza@gmail.com", "souza123", dataPut);
		
		}
	
	@Test
	@DisplayName(" 😃 Cadastrar Usuário!")
	@Order (1)
	public void deveRealizarpostUsuario() {
		
		HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuario);
		
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
	
	@Test
	@DisplayName(" 👨🏿‍🤝‍👨🏿 Listar todos os Usuários!")
	@Order (2)
	public void deveRealizarGetAllUsuario() {
		
	ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("admin@gmail.com", "admin123").exchange("/usuarios/all", HttpMethod.GET, null, String.class);
	assertEquals(HttpStatus.OK, resposta.getStatusCode());
	
	
}
	
	@Test
	@DisplayName("😳 Alterar Usuário!")
	@Order(3)
	public void deveRealizarPutUsuario() {

		HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate
			.withBasicAuth("admin@gmail.com", "admin123")
			.exchange("/usuarios/atualizar", HttpMethod.PUT, request, Usuario.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
}
}

