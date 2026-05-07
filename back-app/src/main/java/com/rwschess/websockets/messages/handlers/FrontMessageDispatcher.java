package com.rwschess.websockets.messages.handlers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwschess.websockets.PlayerWebsocket;
import com.rwschess.websockets.messages.front.FrontMessageType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class FrontMessageDispatcher {
    private final Logger logger = Logger.getLogger(PlayerWebsocket.class.getName());

    @Inject
    ObjectMapper objectMapper;

    @Inject
    GameStartRequestFrontMessageHandler gameStartRequestFrontMessageHandler;

    public void dispatch(String messageRaw) {
        switch (getFrontMessageType(messageRaw)) {
            case FrontMessageType.GAME_START_REQUEST ->
                gameStartRequestFrontMessageHandler.handle(messageRaw);
            default -> throw new IllegalArgumentException("Invalid type of message: " + messageRaw);
        }
    }

    private FrontMessageType getFrontMessageType(String messageRaw) {
        try (final JsonParser jsonParser = objectMapper.createParser(messageRaw)) {
            final JsonNode rootNode = jsonParser.readValueAsTree();
            return FrontMessageType.valueOf(rootNode.get("type").asText());
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

}
