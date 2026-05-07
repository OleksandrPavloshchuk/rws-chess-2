package com.rwschess.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        players.put(playerName, new Player(session));
    }

    @Override
    public void remove(String playerName) {
        logger.info(String.format("Removing player %s", playerName));
        players.remove(playerName);
    }

    @Override
    public Collection<String> freePlayers() {
        return players.keySet().stream()
                .filter(key -> players.get(key).isFree())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Collection<Session> allSessions() {
        return players.values().stream()
                .map(Player::getWsSession)
                .toList();
    }

    @Override
    public Player getPlayer(String playerName) {
        return players.get(playerName);
    }
}
