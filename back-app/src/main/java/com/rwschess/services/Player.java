package com.rwschess.services;

import jakarta.websocket.Session;

public class Player {
    private final Session wsSession;
    private boolean free = true;

    public Player(Session wsSession) {
        this.wsSession = wsSession;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public Session getWsSession() {
        return wsSession;
    }
}
