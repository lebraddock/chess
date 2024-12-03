package ui;

import chess.ChessGame;
import messages.Notification;

public interface NotificationHandler {
    void notify(String message);
    void loadGame(String message);
}