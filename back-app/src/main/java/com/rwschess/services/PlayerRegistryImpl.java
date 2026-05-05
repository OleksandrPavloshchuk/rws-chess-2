package com.rwschess.services;

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

    private final Map<String, Player> players = new ConcurrentHashMap<>();

    @Override
    public boolean contains(String playerName) {
        return players.containsKey(playerName);
    }

    @Override
    public void add(String playerName, Session session) {
        logger.info(String.format("Adding player %s", playerName));
        players.put(playerName, new Player(playerName, session));
    }

    @Override
    public void remove(String playerName) {
        logger.info(String.format("Removing player %s", playerName));
        players.remove(playerName);
    }

    @Override
    public Collection<String> allPlayers() {
        return players.keySet();
    }

    @Override
    public Collection<Session> allSessions() {
        return players.values().stream()
                .map(Player::getWsSession)
                .toList();
    }
}
