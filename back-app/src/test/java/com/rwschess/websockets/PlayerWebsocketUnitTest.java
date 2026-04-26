package com.rwschess.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwschess.services.PlayerService;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerWebsocketUnitTest {

    @Mock
    private Session sessionA;

    @Mock
    private RemoteEndpoint.Async endpointA;

    @Mock
    private Session sessionB;

    @Mock
    private RemoteEndpoint.Async endpointB;

    @Mock
    private Session sessionC;

    @Mock
    private PlayerService playerService;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private PlayerWebsocket playerWebsocket;

    @Test
    void testBroadcastPlayersListOK() throws IOException {

        doReturn(endpointA).when(sessionA).getAsyncRemote();
        playerWebsocket.onOpen(sessionA, "A");
        doReturn(endpointB).when(sessionB).getAsyncRemote();
        playerWebsocket.onOpen(sessionB, "B");
        playerWebsocket.onOpen(sessionB, "C");
        playerWebsocket.onClose(sessionC, "C");
        doReturn(Set.of("one-1", "five-5")).when(playerService).getPlayers();
        playerWebsocket.broadcastPlayerList();
        ArgumentCaptor<String> captorA = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> captorB = ArgumentCaptor.forClass(String.class);
        verify(endpointA).sendText(captorA.capture());
        verify(endpointB).sendText(captorB.capture());
        assertJson(captorA.getValue());
        assertJson(captorB.getValue());
        verify(sessionC, never()).getAsyncRemote();
    }

    @Test
    void testBroadcastPlayerListParsingError() throws JsonProcessingException {
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(any());
        doReturn(Set.of("0")).when(playerService).getPlayers();
        assertThrows( RuntimeException.class, () -> playerWebsocket.broadcastPlayerList());
    }

    private void assertJson(String json) throws IOException {
        final String[] str = objectMapper.readValue(json, String[].class);
        assertEquals(2, str.length);
        final Set<String> actual = Arrays.stream(str).map(String::trim).collect(Collectors.toSet());
        assertTrue( actual.contains("one-1"));
        assertTrue( actual.contains("five-5"));
    }


}
