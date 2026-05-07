package com.rwschess.websockets.messages;

import java.util.Collection;

public record FreePlayersMessage(
        MessageType type,
        Collection<String> payload) implements Message<Collection<String>> {

    public FreePlayersMessage(Collection<String> payload) {
        this(MessageType.FREE_PLAYERS, payload);
    }

}
