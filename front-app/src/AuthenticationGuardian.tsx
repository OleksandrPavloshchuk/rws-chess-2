// AuthenticationGuardian.tsx

import {Navigate, Outlet} from "react-router-dom";
import {useBoardState} from "./pages/board/state.ts";

export const AuthenticationGuardian = () => {
    const isHydrated = useBoardState?.persist.hasHydrated?.();
    if (!isHydrated) {
        return null;
    }
    const me = useBoardState((s) => s.me);
    if (me) {
        return <Outlet/>;
    } else {
        return <Navigate to="/landing" replace/>;
    }
};