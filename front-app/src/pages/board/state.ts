// pages/board/state.ts

import {create} from "zustand";
import {persist} from "zustand/middleware";

export interface BoardState {
    gameId: string|undefined,
    setGameId: (s:string|undefined) => void,
    me: string | undefined,
    setMe: (s: string | undefined) => void,
    other: string | undefined,
    setOther: (s: string | undefined) => void,
    whiteMe: boolean,
    setWhiteMe: (b: boolean) => void
}

export const useBoardState = create<BoardState>()(
    persist(
        (set) => ({
            gameId: undefined,
            setGameId: (s: string|undefined) => set({gameId: s}),
            me: undefined,
            setMe: (s: string | undefined) => set({me: s}),
            other: undefined,
            setOther: (s: string | undefined) => set({other: s}),
            whiteMe: true,
            setWhiteMe: (b: boolean) => set({whiteMe: b})
        }), {
            name: "board-storage"
        }));