// pages/lobby/state.tsx

import {create} from "zustand";

export interface LobbyState {
    isAuthenticated: boolean,
    setAuthenticated: () => void
    players: string[],
    setPlayers: (s: string[]) => void
}

export const useLobbyState = create<LobbyState>((set) => ({
    isAuthenticated: false,
    setAuthenticated: () => set({isAuthenticated: true}),
    players: [],
    setPlayers: (s: string[]) => set({players: s})
}));