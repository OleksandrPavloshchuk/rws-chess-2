// pages/lobby/ui.tsx

import {Button, Flex, ScrollArea, Stack} from "@mantine/core";
import {useNavigate} from "react-router-dom";
import {useBoardState} from "../board/state.ts";
import {useLobbyState} from "./state.ts";
import {useEffect} from "react";
import {notify} from "../../libs/utils.ts";
import {connectToBack, disconnectFromBack, sendToBack} from "../../services/backConnector.ts";
import {createGameStartRequest, type GameStart} from "../../messages/messages.ts";

export const LobbyPage: React.FC = () => {
    const me = useBoardState((s) => s.me);
    if (!me) {
        return;
    }

    const setMe = useBoardState((s) => s.setMe);
    const setOther = useBoardState((s) => s.setOther);
    const setWhiteMe = useBoardState((s) => s.setWhiteMe);
    const setGameId = useBoardState((s) => s.setGameId);

    const freePlayers = useLobbyState((s) => s.freePlayers);
    const setFreePlayers = useLobbyState((s) => s.setFreePlayers);

    const navigate = useNavigate();

    const notMe = (s: string) => s !== me;

    const freePlayersConsumer = (list: string[]) => setFreePlayers(
        list.filter(notMe)
    );

    const gameStarter = (game: GameStart) => {
        setOther(game.players.find(notMe));
        setWhiteMe(game.white === me);
        setGameId(game.gameId);
        navigate("/board", {replace: true});
    };

    const authenticationErrorHandler = () => {
        navigate("/landing", {replace: true});
        notify("Authentication error", "This name is already in use");
    };

    useEffect(() => {
        if (!me) {
            return;
        }
        connectToBack(
            freePlayersConsumer,
            gameStarter,
            authenticationErrorHandler
        );

        return disconnectFromBack;
    }, [me]);

    const logout = () => {
        disconnectFromBack();
        setMe(undefined);
        setOther(undefined);
        navigate("/landing", {replace: true});
    };

    const startGameRequest = (other: string, whiteMe: boolean) => {
        sendToBack(createGameStartRequest(me, other, whiteMe));
    };

    const renderFreePlayer = (name: string) =>
        (<div className={"free-player"} key={name}>
            <Flex w="100%" gap="sm">
                {name}
                <Button
                    onClick={() => startGameRequest(name, true)}
                    className={"start-white"}
                >Start White</Button>
                <Button
                    onClick={() => startGameRequest(name, false)}
                    className={"start-black"}
                >Start Black</Button>
            </Flex>
        </div>);

    return (<Stack gap="xs">
        <Flex w="100%" gap="sm" align="center" justify="space-between"
              style={{paddingLeft: "10px", paddingRight: "10px"}}
        >
            <h3>RWS Chess 2</h3>
            <div>{me}</div>
            <Button onClick={logout}>Logout</Button>
        </Flex>
        <ScrollArea h={720}>
            <div className={"lobby"}>{
                freePlayers.map((name) => renderFreePlayer(name))
            }</div>
        </ScrollArea>
    </Stack>);
};