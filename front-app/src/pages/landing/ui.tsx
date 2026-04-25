// pages/landing/ui.tsx

import {useLandingState} from "./state.ts";
import {Button, Container, Stack, TextInput} from "@mantine/core";
import {useNavigate} from "react-router-dom";
import {useLobbyState} from "../lobby/state.ts";

export const LandingPage:React.FC = () => {

    const user = useLandingState((s) => s.user);
    const setUser = useLandingState((s) => s.setUser);

    const setAuthenticated = useLobbyState((s) => s.setAuthenticated);

    const navigate = useNavigate();

    const doEnter = () => {
        // TODO check presence:

        setAuthenticated();
        navigate("/lobby", {replace: true});
    };

    return (<Container size={"xs"}>
        <Stack gap="xs">
            <h3>RWS Chess 2</h3>
            <div>User:</div>
            <TextInput
                value={user}
                onChange={(e) => setUser(e.currentTarget.value)}
            />
            <Button onClick={doEnter}>Enter</Button>
        </Stack>
    </Container>);
};