package com.rwschess.websockets.messages.back;

public sealed interface BackMessage<T>
    permits FreePlayersBackMessage, GameStartBackMessage
{
    BackMessageType type();
    T payload();
}
