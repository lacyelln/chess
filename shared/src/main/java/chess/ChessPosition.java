package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ChessPosition(ChessPosition pos) {
        this.row = pos.row;
        this.col = pos.col;
    }

    public ChessPosition adjust(int addRow, int addCol){
        int newRow = this.row + addRow;
        int newCol = this.col + addCol;
        return new ChessPosition(newRow, newCol);
    }

    public static void possibleMult(Collection<ChessMove> possibles, int[][] directions, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor){
        for (int[] direction : directions) {
            int addRow = direction[0]; //accesses the first number in the tuple(row)
            int addCol = direction[1]; //accesses the second number in the tuple(col)
            ChessPosition newPosition = myPosition;
            while (true) {
                newPosition = newPosition.adjust(addRow, addCol);
                if (inBounds(newPosition)) {
                    break;
                }
                ChessPiece pieceAtPosition = board.getPiece(newPosition);
                if (pieceAtPosition == null) {
                    possibles.add(new ChessMove(myPosition, newPosition, null));
                }
                else {
                    ChessGame.TeamColor theirColor = pieceAtPosition.getTeamColor();
                    if(theirColor != myColor) { //if it's an opponent
                        possibles.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
            }
        }
    }
    public static boolean inBounds(ChessPosition newPosition){
        return newPosition.getRow() < 1 || newPosition.getRow() > 8
                || newPosition.getColumn() < 1 || newPosition.getColumn() > 8;
    }

    public static void possibleSingle(Collection<ChessMove> possibles, int[][] directions, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor){
        for (int[] direction : directions) {
            int addRow = direction[0]; //accesses the first number in the tuple(row)
            int addCol = direction[1]; //accesses the second number in the tuple(col)
            ChessPosition newPosition = myPosition.adjust(addRow, addCol);
            if (inBounds(newPosition)) {
                continue;
            }
            ChessPiece pieceAtPosition = board.getPiece(newPosition);
            if (pieceAtPosition == null) {
                possibles.add(new ChessMove(myPosition, newPosition, null));
            }
            else {
                ChessGame.TeamColor theirColor = pieceAtPosition.getTeamColor();
                if(theirColor != myColor) { //if it's an opponent
                    possibles.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    public static void pawnPos(Collection<ChessMove> possibles, ChessGame.TeamColor myColor, ChessPosition myPosition, ChessBoard board, int[][] directions){
        for (int[] direction : directions){
            int addRow = direction[0]; //accesses the first number in the tuple(row)
            int addCol = direction[1];
            ChessPosition newPosition = myPosition.adjust(addRow, addCol);
            if (ChessPosition.inBounds(newPosition)) {
                continue;
            }
            ChessPiece pieceAtPosition = board.getPiece(newPosition);
            if (newPosition.getColumn() == myPosition.getColumn()) { //if there is no piece in front you can move.
                if (pieceAtPosition == null) {
                    ChessPosition.pawnAdd(possibles, myColor, myPosition, newPosition);
                }
                else {
                    break;
                }
            }
            else {
                if(pieceAtPosition != null){
                    ChessGame.TeamColor theirColor = pieceAtPosition.getTeamColor();
                    if (theirColor != myColor) { //if it's an opponent
                        ChessPosition.pawnAdd(possibles, myColor, myPosition, newPosition);
                    }
                }
            }
        }
    }

    public static void pawnAdd(Collection<ChessMove> possibles, ChessGame.TeamColor myColor, ChessPosition myPosition, ChessPosition newPosition){
        if(myColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7) {
            addingPiece(possibles, myPosition, newPosition);
        } else if (myColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2){
            addingPiece(possibles, myPosition, newPosition);
        }
        else {
            possibles.add(new ChessMove(myPosition, newPosition, null));
        }
    }

    public static void addingPiece(Collection<ChessMove> possibles, ChessPosition myPosition, ChessPosition newPosition){
        possibles.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
        possibles.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
        possibles.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
        possibles.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
