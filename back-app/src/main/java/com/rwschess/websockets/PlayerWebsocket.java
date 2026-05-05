package com.rwschess.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwschess.services.PlayerRegistry;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.Collection;
import java.util.logging.Logger;

@ServerEndpoint("/ws/players/{player}")
public class PlayerWebsocket {
    private final Logger logger = Logger.getLogger(PlayerWebsocket.class.getName());

    @Inject
    PlayerRegistry playerRegistry;

    @Inject
    ObjectMapper objectMapper;

    @OnOpen
    public void onOpen(Session session, @PathParam("player") String player) {
        logger.info("Player " + player + " connected. SessionId: " + session.getId());
        playerRegistry.add(player, session);
        broadcastPlayerList();
    }

    @OnClose
    public void onClose(Session session, @PathParam("player") String player) {
        logger.info("Player " + player + " disconnected. SessionId: " + session.getId());
        playerRegistry.remove(player);
        broadcastPlayerList();
    }

    public void broadcastPlayerList() {
        logger.info("Broadcast player list");
        final Collection<String> players = playerRegistry.allPlayers();
        try {
            final String playersStr = objectMapper.writeValueAsString(players);
            for (final Session session : playerRegistry.allSessions()) {
                session.getAsyncRemote().sendText(playersStr);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}