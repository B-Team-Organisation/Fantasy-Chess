package com.bteam.fantasychess_server.service;

import com.bteam.common.models.GameModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Service to manage the state of the currently played Games
 */
@Service
public class GameStateService {
    HashMap<String, GameModel> games = new HashMap<>();


}
