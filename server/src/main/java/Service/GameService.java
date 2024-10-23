package Service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class GameService {

    private final AuthDAO aData;
    private final GameDAO gData;
    private final UserDAO uData;

    public GameService(AuthDAO aData, GameDAO gData, UserDAO uData){
        this.aData = aData;
        this.gData = gData;
        this.uData = uData;
    }

    public GameData createGame(String authToken, GameData game) throws DataAccessException{
        if(aData.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if(gData.createGame(game) == null){
            throw new DataAccessException("Error: bad request");
        }
        return gData.createGame(game);
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException{
        if(aData.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        return gData.listGames();
    }

    public void joinGame(String authToken, GameData game) throws DataAccessException{
        if(aData.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if(gData.getGame(game) == 0){
            throw new DataAccessException("Error: bad request");
        }
        gData.updateGame(game);
    }

    public void delete() throws DataAccessException{
        gData.deleteAllGames();


    }
}

