package org.amelie.spring_security.controller;

import org.amelie.spring_security.dto.LoginDto;
import org.amelie.spring_security.dto.LoginRequestDto;
import org.amelie.spring_security.dto.RegisterRequestDto;
import org.amelie.spring_security.dto.RegisterResponseDto;
import org.amelie.spring_security.entity.UserEntity;
import org.amelie.spring_security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.amelie.spring_security.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(
            UserRepository userRepositoryInjected,
            AuthenticationManager authManagerInjected,
            PasswordEncoder passwordEncoderInjected,
            TokenService tokenServiceInjected) {
        this.userRepository = userRepositoryInjected;
        this.authManager = authManagerInjected;
        this.passwordEncoder = passwordEncoderInjected;
        this.tokenService = tokenServiceInjected;
    }

    @PostMapping("/login")
    public LoginDto login(@RequestBody LoginRequestDto request) {

        Authentication auth = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(), request.password()));
        String token = tokenService.generateToken(auth);


        UserEntity userConnected = (UserEntity) auth.getPrincipal();
        if(userConnected == null) {
            throw new IllegalStateException("Utilisateur non authentifié correctement");
        }
        return new LoginDto(token, userConnected.getUsername());
    }

    @PostMapping("/register")
    public RegisterResponseDto registerUser(@RequestBody RegisterRequestDto request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.username().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email().toLowerCase());
        UserEntity saved = userRepository.save(user);
        return new RegisterResponseDto(saved.getUsername(), saved.getEmail());
    }
}
