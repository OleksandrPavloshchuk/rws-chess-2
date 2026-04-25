// pages/lobby/ui.tsx

import {Button, Flex, Stack} from "@mantine/core";
import {useNavigate} from "react-router-dom";
import {useBoardState} from "../board/state.ts";

export const LobbyPage: React.FC = () => {
    const me = useBoardState((s) => s.me);
    const setMe = useBoardState((s) => s.setMe);
    const setOther = useBoardState((s) => s.setOther);

    const navigate = useNavigate();

    const logout = () => {
        setMe(undefined);
        setOther(undefined);
        navigate("/landing", {replace: true});
    };

    return (<Stack gap="xs">
        <Flex w="100%" gap="sm" align="center">
            <h3>RWS Chess 2</h3>
            <div>{me}</div>
            <Button onClick={logout}>Logout</Button>
        </Flex>
    </Stack>);
};