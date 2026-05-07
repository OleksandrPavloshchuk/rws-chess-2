package com.rwschess.websockets.messages.back;

import java.util.Collection;

public record FreePlayersBackMessage(
        BackMessageType type,
        Collection<String> payload) implements BackMessage<Collection<String>> {

    public FreePlayersBackMessage(Collection<String> payload) {
        this(BackMessageType.FREE_PLAYERS, payload);
    }

}
