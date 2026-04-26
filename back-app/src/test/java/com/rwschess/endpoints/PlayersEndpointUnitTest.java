package com.rwschess.endpoints;

import com.rwschess.services.PlayerService;
import com.rwschess.websockets.PlayerWebsocket;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayersEndpointUnitTest {

    @Mock
    private PlayerWebsocket playerWebsocket;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayersEndpoint playersEndpoint;

    @Test
    void testGetAllPlayers() {
        doReturn(Set.of("one", "two")).when(playerService).getPlayers();
        final Collection<String> actual = playersEndpoint.getPlayers();
        assertEquals(Set.of("one", "two"), actual);
    }

    @Test
    void testAddPlayerOk() {
        doReturn(Set.of("001", "002", "003")).when(playerService).getPlayers();
        final Collection<String> actual = playersEndpoint.addPlayer("next");
        assertEquals(Set.of("001", "002", "003"), actual);
        verify(playerService).add("next");
        verify(playerWebsocket).broadcastPlayerList();
    }

    @Test
    void testAddPlayerAlreadyFound() {
        doReturn(true).when(playerService).containsPlayer("one");
        assertThrows(WebApplicationException.class, () -> playersEndpoint.addPlayer("one"));
        verify(playerService, never()).add("one");
        verify(playerWebsocket, never()).broadcastPlayerList();
    }

    @Test
    void testRemovePlayerOk() {
        doReturn(true).when(playerService).containsPlayer("0");
        doReturn(Set.of("1")).when(playerService).getPlayers();
        final Collection<String> actual = playersEndpoint.removePlayer("0");
        assertEquals(Set.of("1"), actual);
        verify(playerService).remove("0");
        verify(playerWebsocket).broadcastPlayerList();
    }

    @Test
    void testRemovePlayerNotFound() {
        doReturn(false).when(playerService).containsPlayer("-");
        assertThrows(WebApplicationException.class, () -> playersEndpoint.removePlayer("-"));
        verify(playerService, never()).remove("-");
        verify(playerWebsocket, never()).broadcastPlayerList();
    }
}
