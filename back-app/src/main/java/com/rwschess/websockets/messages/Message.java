package com.rwschess.websockets.messages;

public sealed interface Message<T>
    permits FreePlayersMessage
{
    MessageType type();
    T payload();
}
