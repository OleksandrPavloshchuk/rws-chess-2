import React from 'react'
import {createRoot} from 'react-dom/client'
import {MantineProvider} from "@mantine/core";
import '@mantine/core/styles.css';

createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <MantineProvider defaultColorScheme="light">
            <div>TODO RWS Chess 2</div>
        </MantineProvider>
    </React.StrictMode>,
)