package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;



public class Board {

    //board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    static ChessBoard board = new ChessBoard();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        printBoard(out);

    }

    public static void printBoard(PrintStream out){
        out.print(ERASE_SCREEN);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println("Black perspective: (black is blue): ");
        drawBlackChessBoard(out);
        out.println();
        out.println();

        out.println("White perspective (white is red):");
        drawWhiteChessBoard(out);





        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_BOLD_FAINT);



    }

    private static void drawHeaders(PrintStream out, String[] headers){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_TEXT_BOLD);
        out.print(" ");
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol){
            out.print(headers[boardCol]);
        }
        out.println();

    }

    private static void drawRowOfSquares(PrintStream out, boolean isEvenRow, int row) {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(row);
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if ((isEvenRow && boardCol % 2 == 0) || (!isEvenRow && boardCol % 2 != 0)) {
                setWhite(out);
            } else {
                setBlack(out);
            }
            addPieceToBoard(out, boardCol, row);
        }
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(row);
        out.println();
    }


    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void addPieceToBoard(PrintStream out, int boardCol, int row){
        out.print(SET_TEXT_BOLD);
        ChessPiece piece = ChessBoard.getPiece(new ChessPosition(row, boardCol+1));
        if (piece != null) {
            out.print(SET_TEXT_COLOR_BLUE);
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_RED);
            }
            out.print(piece.getPieceType().getSymbol());
        }
        else{
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        }
    }




    private static void drawBlackChessBoard(PrintStream out) {
        String[] headers = {" h ", " f ", " g ", " e ", " d ", " c ", " b ", " a "};
        drawHeaders(out, headers);

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            int answer = (boardRow % 2 );
            boolean isEven = answer != 1;
            board.resetBoard();
            board.addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
            board.addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
            board.addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
            board.addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

            drawRowOfSquares(out, isEven, boardRow+1);
            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
        }
        drawHeaders(out, headers);


    }


    private static void drawWhiteChessBoard(PrintStream out){
        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        drawHeaders(out, headers);
        int currentRow = 8;
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            int answer = (boardRow % 2 );
            boolean isEven = answer != 1;
            board.resetBoard();
            drawRowOfSquares(out, isEven, currentRow);
            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
            currentRow --;
        }
        drawHeaders(out, headers);
    }



}
