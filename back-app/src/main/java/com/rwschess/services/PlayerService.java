package com.rwschess.services;

import java.util.Collection;

public interface PlayerService {
    void add(String name);
    void remove(String name);
    Collection<String> getPlayers();
    boolean containsPlayer(String name);
}
