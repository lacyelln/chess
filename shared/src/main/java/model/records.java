package model;
import chess.ChessGame;

record UserData(String username, String password, String email);
record GameData(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);
record AuthData(String authToken, String username);