package chess;

public class GenericError{

    private String message;

    public GenericError(String message) {
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
