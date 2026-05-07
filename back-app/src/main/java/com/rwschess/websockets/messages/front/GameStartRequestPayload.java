package com.rwschess.websockets.messages.front;

import java.util.List;

public record GameStartRequestPayload(List<String> players, String white) {
}
