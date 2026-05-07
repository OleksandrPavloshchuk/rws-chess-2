package com.rwschess.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwschess.services.PlayerRegistry;
import com.rwschess.websockets.messages.FreePlayersMessage;
import jakarta.inject.Inject;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/ws/players/{player}")
public class PlayerWebsocket {
    private final Logger logger = Logger.getLogger(PlayerWebsocket.class.getName());

    @Inject
    PlayerRegistry playerRegistry;

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

    public void broadcastPlayerList() {
        final FreePlayersMessage freePlayersMessage = new FreePlayersMessage(
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

    private static String decodePlayer(String src) {
        return URLDecoder.decode(src, StandardCharsets.UTF_8);
    }

}