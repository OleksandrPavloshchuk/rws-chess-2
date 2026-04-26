package com.rwschess.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplUnitTest {

    @Test
    void testAddPlayer() {
        final PlayerServiceImpl playerService = new PlayerServiceImpl();
        playerService.add( "one");
        assertTrue(playerService.containsPlayer("one"));
    }

    @Test
    void testRemovePlayer() {
        final PlayerServiceImpl playerService = new PlayerServiceImpl();
        for( int i = 0; i < 3; i++ ) {
            playerService.add( "(" + i + ")");
        }
        playerService.remove("(1)");
        assertEquals( Set.of("(0)", "(2)"), playerService.getPlayers());
    }
}
