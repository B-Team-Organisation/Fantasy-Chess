package com.bteam.fantasychess_server.controller;

import com.bteam.common.dto.TokenDTO;
import com.bteam.fantasychess_server.service.PlayerService;
import com.bteam.fantasychess_server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    final TokenService tokenService;
    final PlayerService playerService;

    public AuthController(@Autowired TokenService tokenService, @Autowired PlayerService playerService) {
        this.tokenService = tokenService;
        this.playerService = playerService;
    }

    @GetMapping("/token")
    //@Authenticated
    public ResponseEntity<TokenDTO> getToken(@RequestHeader(name = "X-USER-ID") UUID id) {
        var token = tokenService.generateToken(id);
        return ResponseEntity.ok(new TokenDTO(token.getFirst(), token.getSecond()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam("username") String username) {
        var player = playerService.createPlayer(username);
        return ResponseEntity.ok(player.getPlayerId());
    }
}
