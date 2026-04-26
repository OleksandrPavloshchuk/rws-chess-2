package com.rwschess.endpoints;

import com.rwschess.services.PlayerService;
import com.rwschess.websockets.PlayerWebsocket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;

@ApplicationScoped
@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayersEndpoint {

    @Inject
    PlayerService playerService;

    @Inject
    PlayerWebsocket playerWebsocket;

    @GET
    public Collection<String> getPlayers() {
        return playerService.getPlayers();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Collection<String> addPlayer(String player) {
        if (playerService.containsPlayer(player)) {
            throw new WebApplicationException("Player already exists", 409);
        } else {
            playerService.add(player);
            playerWebsocket.broadcastPlayerList();
            return playerService.getPlayers();
        }
    }

    @DELETE
    @Path("/{player}")
    public Collection<String> removePlayer(@PathParam("player") String player) {
        if (playerService.containsPlayer(player)) {
            playerService.remove(player);
            playerWebsocket.broadcastPlayerList();
            return playerService.getPlayers();
        } else {
            throw new WebApplicationException("Player does not exist", 409);
        }
    }

}
