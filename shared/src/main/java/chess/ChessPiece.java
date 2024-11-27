package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static chess.ChessPosition.pawnPos;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    private boolean hasMoved;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {

        KING(" K "),
        QUEEN(" Q "),
        BISHOP(" B "),
        KNIGHT(" N "),
        ROOK(" R "),
        PAWN(" P ");

        private final String symbol;

        // Constructor
        PieceType(String symbol) {
            this.symbol = symbol;
        }

        // Getter for the associated symbol
        public String getSymbol() {
            return symbol;
        }
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece myPiece = board.getPiece(myPosition);
        PieceType myType = myPiece.getPieceType();
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        Collection<ChessMove> possibles = new ArrayList<>();

        if (myType == PieceType.BISHOP) {
            int[][] directions = {{-1, 1}, {1, -1}, {-1, -1}, {1, 1}};
            ChessPosition.possibleMult(possibles, directions, board, myPosition, myColor);
        }
        if (myType == PieceType.ROOK){
            int[][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}};
            ChessPosition.possibleMult(possibles, directions, board, myPosition, myColor);
        }
        if (myType == PieceType.QUEEN) {
            int[][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}, {1,-1}, {1,1}, {-1,1}, {-1,-1}};
            ChessPosition.possibleMult(possibles, directions, board, myPosition, myColor);
        }
        if (myType == PieceType.KING) {
            int[][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}, {1,-1}, {1,1}, {-1,1}, {-1,-1}};
            ChessPosition.possibleSingle(possibles, directions, board, myPosition, myColor);
        }
        if (myType == PieceType.KNIGHT) {
            int[][] directions = {{1,2}, {1,-2}, {2,1}, {2,-1}, {-1,2}, {-1,-2}, {-2, -1}, {-2, 1}};
            ChessPosition.possibleSingle(possibles, directions, board, myPosition, myColor);
        }
        if (myType == PieceType.PAWN) {
            int[][]directions;
            if(myColor == ChessGame.TeamColor.WHITE) {
                if(myPosition.getRow() == 2){
                    directions = new int [][]{{1,1}, {1,-1},{1,0}, {2,0}};
                }
                else{ directions = new int [][]{{1,1}, {1,-1}, {1,0}};}
            }
            else {
                if(myPosition.getRow() == 7){
                    directions = new int [][]{{-1,-1}, {-1,1},{-1,0},{-2,0}};
                }
                else{ directions = new int[][]{{-1, -1}, {-1, 1},{-1, 0}};}
            }
            pawnPos(possibles, myColor, myPosition, board, directions);
        }
        return possibles;





    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }




}