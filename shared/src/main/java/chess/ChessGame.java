package chess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor team;
    private ChessBoard chessBoard;

    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;

    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        if (chessBoard.getPiece(startPosition) == null){
            return null;
        }
        else{
            ChessPiece current_piece = chessBoard.getPiece(startPosition);
            TeamColor myColor = current_piece.getTeamColor();
            Collection<ChessMove> possibleList = current_piece.pieceMoves(chessBoard, startPosition);
            for(ChessMove possibles : possibleList){
                ChessPiece endPiece = null;
                if(chessBoard.getPiece(possibles.getEndPosition())!= null){
                    endPiece = chessBoard.getPiece(possibles.getEndPosition());
                }
                chessBoard.addPiece(startPosition, null);
                chessBoard.addPiece(possibles.getEndPosition(), current_piece);
                if(!isInCheck(myColor)){
                    validMoves.add(possibles);
                }
                chessBoard.addPiece(startPosition, current_piece);
                chessBoard.addPiece(possibles.getEndPosition(), endPiece);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null; //checking where my kings position
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition pos = new ChessPosition(i, j);
                if(chessBoard.getPiece(pos) != null) {
                    if (chessBoard.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING && chessBoard.getPiece(pos).getTeamColor() == teamColor) {
                        kingPosition = pos;
                    }
                }
            }
        }

        for(int i = 1; i <= 8; i++){ //checking the opponents valid moves
            for(int j = 1; j <= 8; j++){
                ChessPosition otherPos = new ChessPosition(i, j);
                ChessPiece otherPiece = chessBoard.getPiece(otherPos);
                if(otherPiece != null){
                    if(otherPiece.getTeamColor() != teamColor){
                        Collection<ChessMove> opponentList = otherPiece.pieceMoves(chessBoard, otherPos);
                        for(ChessMove moves: opponentList){
                            if(moves.getEndPosition().equals(kingPosition)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.chessBoard;
    }
}
