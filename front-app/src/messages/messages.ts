export type MessageType = "FREE_PLAYERS";

export type Message = {
    type: MessageType;
    payload: any
}