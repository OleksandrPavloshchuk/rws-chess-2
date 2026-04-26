package com.rwschess.services;

// TODO temporary implementation. Use some cache like Redis here
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

@ApplicationScoped
public class PlayerServiceImpl implements PlayerService {

    private final Set<String> players = new TreeSet<>();

    @Override
    public void add(String name) {
        players.add(name);
    }

    @Override
    public void remove(String name) {
        players.remove(name);
    }

    @Override
    public Collection<String> getPlayers() {
        return players;
    }

    @Override
    public boolean containsPlayer(String name) {
        return players.contains(name);
    }
}
