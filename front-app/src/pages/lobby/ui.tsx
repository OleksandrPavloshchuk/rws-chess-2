// pages/lobby/ui.tsx

import {Button, Flex, ScrollArea, Stack} from "@mantine/core";
import {useNavigate} from "react-router-dom";
import {useBoardState} from "../board/state.ts";
import {useLobbyState} from "./state.ts";
import {useEffect} from "react";
import {notify} from "../../libs/utils.ts";
import {connect, disconnect} from "../../services/backConnector.ts";

export const LobbyPage: React.FC = () => {
    const me = useBoardState((s) => s.me);
    const setMe = useBoardState((s) => s.setMe);
    const setOther = useBoardState((s) => s.setOther);

    const freePlayers = useLobbyState((s) => s.freePlayers);
    const setFreePlayers = useLobbyState((s) => s.setFreePlayers);

    const navigate = useNavigate();
    const playerAlreadyConnectedHandler = () => {
        navigate("/landing", {replace: true});
        notify("Authentication error", "This name is already in use");
    };

    useEffect(() => {
        if (!me) {
            return;
        }
        connect(
            (a: string[]) => setFreePlayers(
                a.filter( (v) => v!==me)
            ),
            playerAlreadyConnectedHandler
        );

        return disconnect;
    }, [me]);

    const logout = () => {
        disconnect();
        setMe(undefined);
        setOther(undefined);
        navigate("/landing", {replace: true});
    };

    const renderFreePlayer = (name: string) =>
        (<div className={"free-player"}>
            <Flex w="100%" gap="sm" >
                {name}
                <Button
                    onClick={() => notify("TODO", "start white")}
                    className={"start-white"}
                >Start White</Button>
                <Button
                    onClick={() => notify("TODO", "start black")}
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