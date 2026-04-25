import {useLandingState} from "./state.ts";
import {Button, Container, Stack, TextInput} from "@mantine/core";

export const LandingPage:React.FC = () => {
    const user = useLandingState((s) => s.user);
    const setUser = useLandingState((s) => s.setUser);

    return (<Container size={"xs"}>
        <Stack gap="xs">
            <h3>RWS Chess 2</h3>
            <div>User:</div>
            <TextInput
                value={user}
                onChange={(e) => setUser(e.currentTarget.value)}
            />
            <Button onClick={() => console.log({user})}>Enter</Button>
        </Stack>
    </Container>);
};