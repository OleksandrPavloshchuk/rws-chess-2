package com.rwschess.websockets;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwschess.services.GameService;
import com.rwschess.services.Player;
import com.rwschess.services.PlayerRegistry;
import com.rwschess.websockets.messages.back.FreePlayersBackMessage;
import com.rwschess.websockets.messages.back.GameStartBackMessage;
import com.rwschess.websockets.messages.back.GameStartPayload;
import com.rwschess.websockets.messages.front.FrontMessageType;
import com.rwschess.websockets.messages.front.GameStartRequestFrontMessage;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/ws/players/{player}")
public class PlayerWebsocket {
    private final Logger logger = Logger.getLogger(PlayerWebsocket.class.getName());

    @Inject
    PlayerRegistry playerRegistry;

    @Inject
    GameService gameService;

    @Inject
    ObjectMapper objectMapper;

    @OnOpen
    public void onOpen(Session session, @PathParam("player") String playerRaw) {
        final String player = decodePlayer(playerRaw);
        if (playerRegistry.contains(player)) {
            closeSocket(session, "Player already connected");
            return;
        }
        logger.info("Player " + player + " connected. SessionId: " + session.getId());
        playerRegistry.add(player, session);
        broadcastPlayerList();
    }

    @OnClose
    public void onClose(Session session, @PathParam("player") String playerRaw) {
        final String player = decodePlayer(playerRaw);
        logger.info("Player " + player + " disconnected. SessionId: " + session.getId());
        playerRegistry.remove(player);
        broadcastPlayerList();
    }

    @OnMessage
    public void onMessage(Session session, String messageRaw) throws IOException {

        logger.info("Message received: " + messageRaw);

        try (final JsonParser jsonParser = objectMapper.createParser(messageRaw)) {
            final JsonNode rootNode = jsonParser.readValueAsTree();

            final FrontMessageType type = FrontMessageType.valueOf(rootNode.get("type").asText());
            switch (type) {
                case FrontMessageType.GAME_START_REQUEST -> handleStartGameRequest(messageRaw);
                default -> {
                    logger.warning("Unexpected message type: " + type);
                }
            }
        }
    }

    public void broadcastPlayerList() {
        final FreePlayersBackMessage freePlayersMessage = new FreePlayersBackMessage(
                playerRegistry.freePlayers()
        );
        try {
            final String str = objectMapper.writeValueAsString(freePlayersMessage);
            playerRegistry.allSessions().forEach(
                    session -> session.getAsyncRemote().sendText(str)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeSocket(Session session, String reason) {
        try {
            session.close(
                    new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, reason)
            );
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    private void handleStartGameRequest(String messageRaw) {

        try {
            final GameStartRequestFrontMessage inputMessage = objectMapper.readValue(messageRaw,
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

            broadcastPlayerList();
        } catch( Exception ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    private static String decodePlayer(String src) {
        return URLDecoder.decode(src, StandardCharsets.UTF_8);
    }

}