package com.rwschess.services;

import jakarta.websocket.Session;

public class Player {
    private final String name;
    private final Session wsSession;
    private String opponentName = null;

    public Player(String name, Session wsSession) {
        this.name = name;
        this.wsSession = wsSession;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public Session getWsSession() {
        return wsSession;
    }
}
