package com.rwschess.websockets.messages.back;

import java.util.List;

public record GameStartPayload(String gameId, List<String> players, String white) {
}
