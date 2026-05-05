// main.tsx

import {createRoot} from 'react-dom/client'
import {MantineProvider} from "@mantine/core";
import '@mantine/core/styles.css';
import './main.css';
import {RouterProvider} from "react-router-dom";
import {router} from "./router.tsx";
import {Notifications} from "@mantine/notifications";

createRoot(document.getElementById('root')!).render(
    <MantineProvider defaultColorScheme="light">
        <RouterProvider router={router}/>
        <Notifications position="top-left"/>
    </MantineProvider>
)