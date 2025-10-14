package com.hnsc.triagemoncologica;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hnsc.triagemoncologica.enums.Role;
import com.hnsc.triagemoncologica.model.Usuario;
import com.hnsc.triagemoncologica.repository.UsuarioRepository;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TriagemoncologicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriagemoncologicaApplication.class, args);
	}

	@Bean
	CommandLineRunner createDefaultAdmin(UsuarioRepository repository, PasswordEncoder pe,
			ApplicationProperties appProperties) {
		return _ -> {
			if (repository.findByNomeCompleto(appProperties.admin().nomeCompleto()).isEmpty()) {
				var admin = new Usuario();
				admin.setNomeCompleto(appProperties.admin().nomeCompleto());
				admin.setEmail(appProperties.admin().email());
				admin.setSenha(pe.encode(appProperties.admin().password()));
				admin.setRole(Role.ADMIN);
				repository.save(admin);
			}
		};
	}
}
