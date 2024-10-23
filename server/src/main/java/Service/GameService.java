package Service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class GameService {

    private final AuthDAO aData;
    private final GameDAO gData;

    public GameService(AuthDAO aData, GameDAO gData){
        this.aData = aData;
        this.gData = gData;
    }

    public GameData createGame(String authToken, GameData game) throws DataAccessException, UnauthorizedException, BadRequestException{
        if(aData.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        if(gData.createGame(game) == null){
            throw new BadRequestException();
        }
        return gData.createGame(game);
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
        if(gData.getGame(game) == 0){
            throw new BadRequestException();
        }
        gData.updateGame(game);
    }

    public void delete() throws DataAccessException{
        gData.deleteAllGames();


    }
}

