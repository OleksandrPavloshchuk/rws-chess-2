package com.rwschess.services;

import com.rwschess.websockets.PlayerWebsocket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@ApplicationScoped
public class PlayerRegistryImpl implements PlayerRegistry {
    private final Logger logger = Logger.getLogger(PlayerRegistryImpl.class.getName());

    private final Map<String, Session> namesToSessions = new ConcurrentHashMap<>();

    @Override
    public boolean contains(String playerName) {
        return namesToSessions.containsKey(playerName);
    }

    @Override
    public void add(String playerName, Session session) {
        logger.info(String.format("Adding player %s", playerName));
        namesToSessions.put(playerName, session);
    }

    @Override
    public void remove(String playerName) {
        logger.info(String.format("Removing player %s", playerName));
        namesToSessions.remove(playerName);
    }

    @Override
    public Collection<String> allPlayers() {
        return namesToSessions.keySet();
    }

    @Override
    public Collection<Session> allSessions() {
        return namesToSessions.values();
    }
}
