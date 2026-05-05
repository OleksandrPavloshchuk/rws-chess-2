package com.rwschess.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwschess.services.PlayerRegistry;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerWebsocketUnitTest {

    @Mock
    private Session sessionA;

    @Mock
    private RemoteEndpoint.Async endpointA;

    @Mock
    private PlayerRegistry playerRegistry;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private PlayerWebsocket playerWebsocket;

    @Test
    void testAddPlayerTwice() throws IOException {
        doReturn(true).when(playerRegistry).contains("A");
        playerWebsocket.onOpen(sessionA, "A");
        verify(sessionA).close(any());
    }

    @Test
    void testBroadcastPlayersListOK() throws IOException {
        doReturn(endpointA).when(sessionA).getAsyncRemote();
        doReturn(List.of("A")).when(playerRegistry).allPlayers();
        doReturn(List.of(sessionA)).when(playerRegistry).allSessions();
        playerWebsocket.broadcastPlayerList();
        verify(objectMapper).writeValueAsString(List.of("A"));
    }


}
