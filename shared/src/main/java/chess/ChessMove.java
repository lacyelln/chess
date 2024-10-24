package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition sP;
    private ChessPosition endP;
    private ChessPiece.PieceType promP;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.sP = startPosition;
        this.endP = endPosition;
        this.promP = promotionPiece;

    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return sP;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endP;
    }



    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(sP, chessMove.sP) && Objects.equals(endP, chessMove.endP) && promP == chessMove.promP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sP, endP, promP);
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "startPosition=" + sP +
                ", endPosition=" + endP +
                ", promotionPiece=" + promP +
                '}';
    }
}
