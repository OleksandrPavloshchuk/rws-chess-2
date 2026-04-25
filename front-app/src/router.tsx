// router.tsx

import {createBrowserRouter, Navigate} from "react-router-dom";
import {LandingPage} from "./pages/landing/ui.tsx";
import {LobbyPage} from "./pages/lobby/ui.tsx";
import {AuthenticationGuardian} from "./AuthenticationGuardian.tsx";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <Navigate to="/landing" replace />
    },
    {
        path: "/landing",
        element: <LandingPage/>
    },
    {
        element: <AuthenticationGuardian />,
        children: [
            {
                path: "/lobby",
                element: <LobbyPage/>
            }
        ]
    }
]);
