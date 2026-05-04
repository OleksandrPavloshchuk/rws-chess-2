// pages/lobby/state.ts

import {create} from "zustand";

export interface LobbyState {
    freePlayers: string[],
    setFreePlayers: (s: string[]) => void
}

export const useLobbyState = create<LobbyState>((set) => ({
    freePlayers: [],
    setFreePlayers: (a: string[]) => set({freePlayers: a})
}));