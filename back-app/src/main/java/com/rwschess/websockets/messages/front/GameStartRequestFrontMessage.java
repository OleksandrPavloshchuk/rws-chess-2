package com.rwschess.websockets.messages.front;

public record GameStartRequestFrontMessage(
        FrontMessageType type,
        GameStartRequestPayload payload) implements FrontMessage<GameStartRequestPayload> {

    public GameStartRequestFrontMessage(GameStartRequestPayload payload) {
        this(FrontMessageType.GAME_START_REQUEST, payload);
    }

}
