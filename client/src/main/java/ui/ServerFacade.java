package ui;

import chess.GenericError;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, null, user, AuthData.class);
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        try {
            return this.makeRequest("POST", path, null, user, AuthData.class);
        } catch (ResponseException e) {
            throw new ResponseException("Please login with a registered username and password.");
        }
    }

    public int createGame(AuthData auth, String gameName) throws ResponseException {
        CreateGameRequestBody body = new CreateGameRequestBody(gameName);
        GameData game = this.makeRequest("POST", "/game", auth, body, GameData.class);
        return game.gameID();
    }

    public void deleteAll() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public GameData[] listGames(AuthData auth) throws ResponseException {
        var path = "/game";
        record ListChessResponse(GameData[] games) {
        }
        var response = this.makeRequest("GET", path, auth, null, ListChessResponse.class);
        return response.games;
    }

    public void joinGame(AuthData auth, int gameID, String playerColor) throws ResponseException {
        var path = "/game";
        Map<String, Object> params = new HashMap<>();
        params.put("gameID", gameID);
        params.put("playerColor", playerColor.toUpperCase());
        this.makeRequest("PUT", path, auth, params, null);
    }

    public void logout(AuthData auth) throws ResponseException{
        var path = "/session";
        this.makeRequest("DELETE", path, auth, null, null);
    }



    private <T> T makeRequest(String method, String path, AuthData auth, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if(auth != null){
                http.addRequestProperty("authorization", auth.authToken());
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            InputStreamReader reader = new InputStreamReader(http.getErrorStream());
            StringBuilder sb = new StringBuilder();
            while (true){
                int i = reader.read();
                if(i == -1){
                    break;
                }
                sb.append((char) i);
            }
            String content = sb.toString();
            GenericError error = new Gson().fromJson(content, GenericError.class);
            throw new ResponseException(error.getMessage());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                StringBuilder sb = new StringBuilder();
                while (true){
                    int i = reader.read();
                    if(i == -1){
                        break;
                    }
                    sb.append((char) i);
                }
                String content = sb.toString();

                if (responseClass != null) {
                    response = new Gson().fromJson(content, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}