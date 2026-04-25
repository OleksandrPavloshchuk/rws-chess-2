// pages/landing/ui.tsx

import {Button, Container, Stack, TextInput} from "@mantine/core";
import {useNavigate} from "react-router-dom";
import {useBoardState} from "../board/state.ts";

export const LandingPage:React.FC = () => {

    const me = useBoardState((s) => s.me);
    const setMe = useBoardState((s) => s.setMe);

    const navigate = useNavigate();

    const doEnter = () => {
        if (!me || me.trim().length==0) {
            return;
        }

        // TODO check presence:

        navigate("/lobby", {replace: true});
    };

    return (<Container size={"xs"}>
        <Stack gap="xs">
            <h3>RWS Chess 2</h3>
            <div>User:</div>
            <TextInput
                value={me}
                onChange={(e) => setMe(e.currentTarget.value)}
            />
            <Button onClick={doEnter}>Enter</Button>
        </Stack>
    </Container>);
};