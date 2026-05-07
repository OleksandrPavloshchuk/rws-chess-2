package com.rwschess.websockets.messages.back;

public record GameStartBackMessage(
        BackMessageType type,
        GameStartPayload payload) implements BackMessage<GameStartPayload> {

    public GameStartBackMessage(GameStartPayload payload) {
        this(BackMessageType.GAME_START, payload);
    }

}
