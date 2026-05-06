package com.rwschess.services;

import jakarta.websocket.Session;

import java.util.Collection;

public interface PlayerRegistry {
    boolean contains(String playerName);
    void add(String playerName, Session session);
    void remove(String playerName);
    Collection<String> freePlayers();
    Collection<Session> allSessions();
}
