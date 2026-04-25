// pages/lobby/ui.tsx

import {Flex, Stack} from "@mantine/core";
import {useLandingState} from "../landing/state.ts";
import {Link} from "react-router-dom";

export {Stack} from "@mantine/core";

export const LobbyPage: React.FC = () => {
    const user = useLandingState((s) => s.user);

    return (<Stack gap="xs">
        <Flex w="100%" gap="sm" align="center">
            <h3>RWS Chess 2</h3>
            <div>{user}</div>
            <Link to={"/landing"}>Exit</Link>
        </Flex>
    </Stack>);
};