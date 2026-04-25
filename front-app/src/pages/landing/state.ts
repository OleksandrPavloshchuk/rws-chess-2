// pages/landing/state.tsx

import {create} from "zustand";

export interface LandingState {
    user: string|undefined,
    setUser: (s: string|undefined) => void
}

export const useLandingState = create<LandingState>((set) => ({
    user: undefined,
    setUser: (s: string|undefined) => set({user:s})
}));