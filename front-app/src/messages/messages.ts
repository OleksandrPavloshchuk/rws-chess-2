// messages.ts

export type BackMessageType =
    "FREE_PLAYERS"
    | "GAME_START";

export type FrontMessageType =
    "GAME_START_REQUEST";

export interface BackMessage<T> {
    type: BackMessageType;
    payload: T
}

export interface FrontMessage<T> {
    type: FrontMessageType;
    payload: T
}

export interface GameStartRequest {
    players: string[];
    white: string
}

export interface GameStart extends GameStartRequest {
    gameId: string
}

export const createGameStartRequest =
    (me: string, other: string, whiteMe: boolean): FrontMessage<GameStartRequest> => {
    return {
        type: 'GAME_START_REQUEST',
        payload: {
            players: [me, other],
            white: whiteMe ? me : other
        }
    }
}