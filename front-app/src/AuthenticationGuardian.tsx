// AuthenticationGuardian.tsx

import {Navigate, Outlet} from "react-router-dom";
import {useLobbyState} from "./pages/lobby/state.ts";

export const AuthenticationGuardian = () => {
    const isAuthenticated = useLobbyState((s) => s.isAuthenticated);
    if (isAuthenticated) {
        return <Outlet/>;
    } else {
        return <Navigate to="/landing" replace />;
    }
};