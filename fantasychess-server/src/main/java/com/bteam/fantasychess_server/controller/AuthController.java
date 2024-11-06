package com.bteam.fantasychess_server.controller;

import com.bteam.fantasychess_server.data.dto.TokenDTO;
import com.bteam.fantasychess_server.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/token")
    //@Authenticated
    public ResponseEntity<TokenDTO> getHealth(@RequestHeader(name = "X-USER-ID") UUID id) {
        var token = tokenService.generateToken(id);
        return ResponseEntity.ok(new TokenDTO(token.getFirst(), token.getSecond()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam("username") String username) {
        return ResponseEntity.internalServerError().body("");
    }
}
