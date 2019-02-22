package br.com.fielo.padiolaJ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
@EnableOAuth2Sso
public class PadiolaJApplication {

    @Autowired
    private Force force;

    @RequestMapping("/accounts")
    public List<Force.Account> accounts(OAuth2Authentication principal) {
        return force.accounts(principal);
    }

    public static void main(String[] args) {
        SpringApplication.run(PadiolaJApplication.class, args);
    }

}
