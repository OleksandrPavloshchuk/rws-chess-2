package com.rwschess.services;

import com.rwschess.websockets.PlayerWebsocket;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class GameServiceImpl implements GameService {
    private final Logger logger = Logger.getLogger(GameServiceImpl.class.getName());

    // TODO use some storage here

    @Override
    public String createGame(List<String> players, String white) {
        final String gameId = UUID.randomUUID().toString();
        logger.info("Creating game " + gameId + ": " + players + ", white: " + white);
        return gameId;
    }

}
