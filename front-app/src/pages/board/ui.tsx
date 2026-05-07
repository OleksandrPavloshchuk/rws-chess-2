// pages/board/ui.tsx

import {Button, Flex, Stack} from "@mantine/core";
import {useNavigate} from "react-router-dom";
import {useBoardState} from "./state.ts";

export const BoardPage: React.FC = () => {
    const me = useBoardState((s) => s.me);
    const other = useBoardState((s) => s.other);
    const setOther = useBoardState((s) => s.setOther);
    const whiteMe = useBoardState((s) => s.whiteMe);
    const gameId = useBoardState((s) => s.gameId);
    const setGameId = useBoardState((s) => s.setGameId);

    const navigate = useNavigate();

    const leaveGame = () => {
        setOther(undefined);
        setGameId(undefined);
        // TODO Send message to backend: "game abandoned"
        navigate("/lobby", {replace: true});
    };

    return (<Stack gap="xs">
        <Flex w="100%" gap="sm" align="center" justify="space-between"
            style={{paddingLeft: "10px", paddingRight: "10px"}}
        >
            <h3>RWS Chess 2</h3>
            {gameId}
            <Button onClick={leaveGame}>Leave Game</Button>
        </Flex>
        <div>TODO {me}: {whiteMe ? "WHITE" : "BLACK"}</div>
        <div>TODO {other}: {whiteMe ? "BLACK" : "WHITE"}</div>
    </Stack>);
};