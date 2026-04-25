// main.tsx

import React from 'react'
import {createRoot} from 'react-dom/client'
import {MantineProvider} from "@mantine/core";
import '@mantine/core/styles.css';
import './main.css';
import {RouterProvider} from "react-router-dom";
import {router} from "./router.tsx";

createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <MantineProvider defaultColorScheme="light">
            <RouterProvider router={router}/>
        </MantineProvider>
    </React.StrictMode>
)