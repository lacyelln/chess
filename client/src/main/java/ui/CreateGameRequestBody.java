package ui;

public class CreateGameRequestBody {
    private String gameName;

    public CreateGameRequestBody(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName(){
        return gameName;
    }
}
