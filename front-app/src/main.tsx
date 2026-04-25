import React from 'react'
import {createRoot} from 'react-dom/client'
import {MantineProvider} from "@mantine/core";
import '@mantine/core/styles.css';
import './main.css';
import {LandingPage} from "./pages/landing/ui.tsx";

createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <MantineProvider defaultColorScheme="light">
            <LandingPage/>
        </MantineProvider>
    </React.StrictMode>,
)