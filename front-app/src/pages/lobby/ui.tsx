// pages/lobby/ui.tsx

import {Button, Flex, Stack} from "@mantine/core";
import {useNavigate} from "react-router-dom";
import {useBoardState} from "../board/state.ts";
import {useLobbyState} from "./state.ts";
import {useEffect, useRef} from "react";

export const LobbyPage: React.FC = () => {
    const me = useBoardState((s) => s.me);
    const setMe = useBoardState((s) => s.setMe);
    const setOther = useBoardState((s) => s.setOther);

    const freePlayers = useLobbyState((s) => s.freePlayers);
    const setFreePlayers = useLobbyState((s) => s.setFreePlayers);

    const navigate = useNavigate();

    const wsRef = useRef<WebSocket | null>(null);

    useEffect(() => {
        if (!me) {
            return;
        }
        const ws = new WebSocket(`ws://localhost:8080/ws/players/${me}`);
        wsRef.current = ws;

        ws.onopen = () => {
            console.log("WS connected");
        };

        ws.onmessage = (event) => {
            console.log("WS message:", event.data);
            const players: string[] = JSON.parse(event.data);
            setFreePlayers(players.filter(p => p !== me));
        };

        ws.onclose = () => {
            console.log("WS closed");
        };

        return () => {
            ws.close();
        };
    }, [me]);


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
        <div>{
            freePlayers.map( (name) => <div
                key={name}
            >{name}</div>)
        }</div>
    </Stack>);
};