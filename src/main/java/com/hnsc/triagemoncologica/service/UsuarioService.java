package com.hnsc.triagemoncologica.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hnsc.triagemoncologica.dto.LoginUsuarioRequestDTO;
import com.hnsc.triagemoncologica.dto.LoginUsuarioResponseDTO;
import com.hnsc.triagemoncologica.dto.RegistroUsuarioRequestDTO;
import com.hnsc.triagemoncologica.dto.RegistroUsuarioResponseDTO;
import com.hnsc.triagemoncologica.mapper.UsuarioMapper;
import com.hnsc.triagemoncologica.model.Usuario;
import com.hnsc.triagemoncologica.repository.UsuarioRepository;
import com.hnsc.triagemoncologica.utils.JwtTokenHelper;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtTokenHelper jwtTokenHelper;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder,
            @Lazy AuthenticationManager authManager, JwtTokenHelper jwtTokenHelper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário com e-mail: " + email + " não encontrado"));
    }

    @Transactional
    public RegistroUsuarioResponseDTO registroUsuario(RegistroUsuarioRequestDTO dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail: " + dto.email() + " já em uso");
        }

        if (repository.findByNomeCompleto(dto.nomeCompleto()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Nome: " + dto.nomeCompleto() + " já em uso");
        }

        var usuario = new Usuario();
        usuario.setNomeCompleto(dto.nomeCompleto());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        return UsuarioMapper.paraRegistroUsuarioResponseDto(repository.save(usuario));
    }

    public LoginUsuarioResponseDTO loginUsuario(LoginUsuarioRequestDTO dto) {
        var authentication = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        authManager.authenticate(authentication);

        Usuario usuario = repository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos"));

        var jwtToken = jwtTokenHelper.generateToken(UsuarioMapper.paraUsuarioSemSenhaDto(usuario));
        return new LoginUsuarioResponseDTO(usuario.getNomeCompleto(), usuario.getEmail(), jwtToken);
    }
}
