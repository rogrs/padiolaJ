package br.com.fielo.padiolaJ;

import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping("/")
	public ResponseEntity<String> greeting() {
		JavarestApi api = new JavarestApi();
		try {
			api.execute();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Erro " + e.getMessage());
		}
		return ResponseEntity.ok().body("teste");
	}

}
