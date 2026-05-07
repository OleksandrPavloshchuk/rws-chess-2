// services/backConnector.ts

import {useBoardState} from "../pages/board/state.ts";
import type {Message} from "../messages/messages.ts";
import {notify} from "../libs/utils.ts";

let ws: WebSocket | null = null;

export const connect = (
    freePlayersConsumer: (arg: string[]) => void,
    playerAlreadyConnectedHandler: () => void
) => {
    if (ws) {
        return;
    }
    const me = useBoardState.getState().me;
    if (me) {
        const url = `ws://localhost:8080/ws/players/${me}`;
        ws = new WebSocket(url);
        ws.onopen = () => {
            console.log("connected to backend");
        }
        ws.onmessage = (event) => messageHandler(event, freePlayersConsumer);
        ws.onclose = (event) => {
            console.log("disconnected from backend");
            if (event.reason === "Player already connected") {
                playerAlreadyConnectedHandler();
            }
            ws = null;
        };
    }
}

export const disconnect = () => {
    ws?.close();
    ws = null;
}

const messageHandler = (
    event: MessageEvent<any>,
    freePlayersConsumer: (arg: string[]) => void
) => {
    const msg: Message = JSON.parse(event.data);
    switch (msg.type) {
        case 'FREE_PLAYERS':
            freePlayersConsumer(msg.payload as string[]);
            break;
        default:
            notify("ERROR", `Unexpected message type ${msg.type}`)
    }
}