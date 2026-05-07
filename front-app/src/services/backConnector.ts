// services/backConnector.ts

import {useBoardState} from "../pages/board/state.ts";
import type {GameStart, BackMessage, FrontMessage} from "../messages/messages.ts";
import {notify} from "../libs/utils.ts";

let ws: WebSocket | null = null;

export const connectToBack = (
    freePlayersConsumer: (arg: string[]) => void,
    gameStarter: (game: GameStart) => void,
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
        ws.onmessage = (event) => backMessageHandler(
            event,
            freePlayersConsumer,
            gameStarter
        );
        ws.onclose = (event) => {
            console.log("disconnected from backend");
            if (event.reason === "Player already connected") {
                playerAlreadyConnectedHandler();
            }
            ws = null;
        };
    }
}

export const sendToBack = <T>(msg: FrontMessage<T>) => {
    const json = JSON.stringify(msg);
    console.log(`send message: ${json}`)
    ws?.send(json);
}

export const disconnectFromBack = () => {
    ws?.close();
    ws = null;
}

const backMessageHandler = (
    event: MessageEvent<string>,
    freePlayersConsumer: (arg: string[]) => void,
    gameStarter: (game: GameStart) => void
) => {
    console.log(`received message: ${event.data}`)

    const msg: BackMessage<any> = JSON.parse(event.data);
    switch (msg.type) {
        case 'FREE_PLAYERS':
            freePlayersConsumer(msg.payload as string[]);
            break;
        case "GAME_START":
            gameStarter(msg.payload as GameStart)
            break;
        default:
            notify("ERROR", `Unexpected backend message type ${msg.type}`)
    }
}