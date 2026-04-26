package com.rwschess.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwschess.services.PlayerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@ServerEndpoint("/ws/players/{player}")
public class PlayerWebsocket {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Session> namesToWebsockets = new ConcurrentHashMap<>();
    private final Map<Session, String> websocketsToNames = new ConcurrentHashMap<>();

    @Inject
    PlayerService playerService;

    @OnOpen
    public void onOpen(Session session, @PathParam("player") String player) {
        namesToWebsockets.put(player, session);
        websocketsToNames.put(session, player);
    }

    @OnClose
    public void onClose(Session session, @PathParam("player") String player) {
        namesToWebsockets.remove(player);
        websocketsToNames.remove(session);
    }

    public void broadcastPlayerList() {
        final Collection<String> players = playerService.getPlayers();
        try {
            final String playersStr = objectMapper.writeValueAsString(players);
            for (final Session session : namesToWebsockets.values()) {
                session.getAsyncRemote().sendText(playersStr);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}