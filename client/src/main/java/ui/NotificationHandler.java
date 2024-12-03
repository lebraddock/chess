package ui;

import chess.ChessGame;
import messages.Notification;

public interface NotificationHandler {
    void handleMessage(String message);
}