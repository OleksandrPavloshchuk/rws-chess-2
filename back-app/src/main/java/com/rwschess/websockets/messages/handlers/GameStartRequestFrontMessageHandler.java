package com.rwschess.websockets.messages.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwschess.services.GameService;
import com.rwschess.services.Player;
import com.rwschess.services.PlayerRegistry;
import com.rwschess.websockets.PlayerWebsocket;
import com.rwschess.websockets.messages.back.GameStartBackMessage;
import com.rwschess.websockets.messages.back.GameStartPayload;
import com.rwschess.websockets.messages.front.GameStartRequestFrontMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class GameStartRequestFrontMessageHandler {
    private final Logger logger = Logger.getLogger(PlayerWebsocket.class.getName());

    @Inject
    PlayerRegistry playerRegistry;

    @Inject
    GameService gameService;

    @Inject
    ObjectMapper objectMapper;

    public void handle(String src) {
        try {
            final GameStartRequestFrontMessage inputMessage = objectMapper.readValue(src,
                    GameStartRequestFrontMessage.class);

            // TODO chack players availability

            final List<String> players = inputMessage.payload().players();
            final String white = inputMessage.payload().white();

            final String gameId = gameService.createGame(players, white);

            final GameStartBackMessage outputMessage = new GameStartBackMessage(
                    new GameStartPayload(gameId, players, white));

            final String messageStr = objectMapper.writeValueAsString(outputMessage);

            logger.info("Output message JSON: " + messageStr);

            players.forEach(playerName -> {
                        final Player player = playerRegistry.getPlayer(playerName);
                        player.setFree(false);
                        player.getWsSession().getAsyncRemote().sendText(messageStr);
                    }
            );

        } catch( Exception ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

    }
}
