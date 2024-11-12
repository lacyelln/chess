package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        AuthData auth = this.makeRequest("POST", path, user, AuthData.class);
        return auth;
    }

    public void login(UserData user) throws ResponseException {
        var path = "/session";
        this.makeRequest("POST", path, user, AuthData.class);
    }


    public void deleteAll() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public ChessGame[] listPets() throws ResponseException {
        var path = "/pet";
        record listPetResponse(ChessGame[] pet) {
        }
        var response = this.makeRequest("GET", path, null, listPetResponse.class);
        return new ChessGame[0];
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
            //if(status == )
            throw new ResponseException("server error");
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}