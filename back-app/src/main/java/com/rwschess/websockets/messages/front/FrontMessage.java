package com.rwschess.websockets.messages.front;

public sealed interface FrontMessage<T>
    permits GameStartRequestFrontMessage
{
    FrontMessageType type();
    T payload();
}
