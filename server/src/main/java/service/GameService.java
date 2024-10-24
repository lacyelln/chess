package service;

import dataaccess.*;
import model.GameData;

import java.util.ArrayList;

public class GameService {

    private final AuthDAO aData;
    private final GameDAO gData;

    public GameService(AuthDAO aData, GameDAO gData){
        this.aData = aData;
        this.gData = gData;
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException, UnauthorizedException, BadRequestException{
        if(aData.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        GameData gameId = gData.createGame(gameName);
        if(gameId == null | gameName == null){
            throw new BadRequestException();
        }
        return gameId;
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException, UnauthorizedException{
        if(aData.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        return gData.listGames();
    }

    public void joinGame(String authToken, GameData game) throws DataAccessException, BadRequestException, UnauthorizedException{
        if(aData.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        if(game.gameName() == null){
            throw new BadRequestException();
        }
        if(gData.getGame(game.gameName()) == null){
            throw new AlreadyTakenException();
        }

        gData.updateGame(game);
    }

    public void delete() throws DataAccessException{
        gData.deleteAllGames();


    }
}

