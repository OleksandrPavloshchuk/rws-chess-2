package com.rwschess.services;

import java.util.List;

public interface GameService {
    String createGame(List<String> players, String white);
}
