// pages/board/state.tsx

import {create} from "zustand";
import {persist} from "zustand/middleware";

export interface BoardState {
    me: string | undefined,
    setMe: (s: string | undefined) => void,
    other: string | undefined,
    setOther: (s: string | undefined) => void
}

export const useBoardState = create<BoardState>()(
    persist(
        (set) => ({
            me: undefined,
            setMe: (s: string | undefined) => set({me: s}),
            other: undefined,
            setOther: (s: string | undefined) => set({other: s})
        }), {
            name: "board-storage"
        }));