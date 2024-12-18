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
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        team = TeamColor.WHITE;
        setBoard(board);
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
            ChessPiece currentPiece = chessBoard.getPiece(startPosition);
            TeamColor myColor = currentPiece.getTeamColor();
            Collection<ChessMove> possibleList = currentPiece.pieceMoves(chessBoard, startPosition);
            for(ChessMove possibles : possibleList){
                ChessPiece endPiece = null;
                if(chessBoard.getPiece(possibles.getEndPosition())!= null){
                    endPiece = chessBoard.getPiece(possibles.getEndPosition());
                }
                chessBoard.removePiece(startPosition);
                chessBoard.addPiece(possibles.getEndPosition(), currentPiece);
                if(!isInCheck(myColor)){
                    validMoves.add(possibles);
                }
                chessBoard.addPiece(startPosition, currentPiece);
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
        ChessPosition myPiecePos = new ChessPosition(move.getStartPosition());
        if(chessBoard.getPiece(myPiecePos) == null)
        {
            throw new InvalidMoveException();
        }
        ChessPiece myPiece = chessBoard.getPiece(myPiecePos);
        TeamColor myColor = myPiece.getTeamColor();
        if(getTeamTurn() == myColor && this.validMoves(move.getStartPosition()).contains(move)){
            if(move.getPromotionPiece() != null){
                ChessPiece promotionPiece = new ChessPiece(myColor, move.getPromotionPiece());
                chessBoard.addPiece(move.getEndPosition(), promotionPiece);
                chessBoard.addPiece(move.getStartPosition(), null);
            }
            else{
                chessBoard.addPiece(move.getEndPosition(), chessBoard.getPiece(myPiecePos));
                chessBoard.addPiece(move.getStartPosition(), null);
            }
            if(myColor == TeamColor.WHITE){
                setTeamTurn(TeamColor.BLACK);
            }
            else{
                setTeamTurn(TeamColor.WHITE);
            }
        }
        else{
            throw new InvalidMoveException();
        }
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
                ChessPosition p = new ChessPosition(i, j);
                if(chessBoard.getPiece(p) != null) {
                    if (chessBoard.getPiece(p).getPieceType() == ChessPiece.PieceType.KING && chessBoard.getPiece(p).getTeamColor() == teamColor) {
                        kingPosition = p;
                    }
                }
            }
        }
        for(int i = 1; i <= 8; i++){ //checking the opponents valid moves
            for(int j = 1; j <= 8; j++){
                ChessPosition otherPos = new ChessPosition(i, j);
                ChessPiece otherPiece = chessBoard.getPiece(otherPos);
                if(otherPiece == null || otherPiece.getTeamColor() == teamColor){
                    continue;
                }
                Collection<ChessMove> opponentList = otherPiece.pieceMoves(chessBoard, otherPos);
                for(ChessMove moves: opponentList){
                    if(moves.getEndPosition().equals(kingPosition)){
                        return true;
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
        Collection<ChessMove> allValidMoves= new ArrayList<ChessMove>();
        getMoves(allValidMoves, teamColor);
        return allValidMoves.isEmpty() && isInCheck(teamColor);
    }

    public void getMoves(Collection<ChessMove> allValidMoves, TeamColor teamColor){
        for(int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currentPos = new ChessPosition(i, j);
                Collection<ChessMove> validMoves = this.validMoves(currentPos);
                if (validMoves == null) {
                    continue;
                }
                if (!validMoves.isEmpty()) {
                    if(chessBoard.getPiece(currentPos).getTeamColor() == teamColor){
                        allValidMoves.addAll(validMoves);
                    }
                }
            }
        }

    }
    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> allValidMoves= new ArrayList<ChessMove>();
        getMoves(allValidMoves, teamColor);
        return allValidMoves.isEmpty() && !isInCheck(teamColor);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessGame chessGame = (ChessGame) o;
        return team == chessGame.team && Objects.equals(chessBoard, chessGame.chessBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, chessBoard);
    }
}
