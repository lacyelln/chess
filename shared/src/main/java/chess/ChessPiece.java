package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
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
            for (int[] direction : directions){
                int addRow = direction[0]; //accesses the first number in the tuple(row)
                int addCol = direction[1];
                ChessPosition newPosition = myPosition.adjust(addRow, addCol);
                if (newPosition.getRow() < 1 || newPosition.getRow() > 8
                        || newPosition.getColumn() < 1 || newPosition.getColumn() > 8) {
                    continue;
                }
                ChessPiece pieceAtPosition = board.getPiece(newPosition);
                if (newPosition.getColumn() == myPosition.getColumn()) { //if there is no piece in front you can move.
                    if (pieceAtPosition == null) {
                        if(myColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7) {
                            possibles.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                            possibles.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                            possibles.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                            possibles.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                        } else if (myColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2){
                            possibles.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                            possibles.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                            possibles.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                            possibles.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                        }
                        else {possibles.add(new ChessMove(myPosition, newPosition, null ));}
                    }
                    else {
                        break;
                    }
                }
                else {
                    if(pieceAtPosition != null){
                        ChessGame.TeamColor theirColor = pieceAtPosition.getTeamColor();
                        if (theirColor != myColor) { //if it's an opponent
                            if(myColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7) {
                                possibles.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                                possibles.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                                possibles.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                                possibles.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                            } else if (myColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2){
                                possibles.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                                possibles.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                                possibles.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                                possibles.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                            }
                            else {
                                possibles.add(new ChessMove(myPosition, newPosition, null));
                            }
                        }
                    }
                }
            }
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