package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoard {

    //board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    static chess.ChessBoard gameBoard = new chess.ChessBoard();
    static String observer = "WHITE";


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        printBoard(out, observer);

    }

    public static void printBoard(PrintStream out, String observer){
        out.print(ERASE_SCREEN);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);

        if (Objects.equals(observer, "WHITE")){
            out.println("white perspective");
            drawWhiteChessBoard(out);
            out.println();
            out.println();
            out.println("black perspective");
            drawBlackChessBoard(out);
        }
        else{
            out.println("black perspective");
            drawBlackChessBoard(out);
            out.println();
            out.println();
            out.println("white perspective");
            drawWhiteChessBoard(out);
        }






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

    private static void drawRowOfSquares(PrintStream out, boolean isEvenRow, int row, String board) {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(row);
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if ((isEvenRow && boardCol % 2 == 0) || (!isEvenRow && boardCol % 2 != 0)) {
                setWhite(out);
            } else {
                setBlack(out);
            }
            if (row < 3 || row > 6){
                addPieceToBoard(out, boardCol, row);
            }
            else {
                out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
            }
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
        ChessPiece piece = gameBoard.getPiece(new ChessPosition(row, boardCol+1));
        out.print(SET_TEXT_COLOR_RED);
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_BLUE);
        }
        out.print(piece.getPieceType().getSymbol());


    }


    private static void drawBlackChessBoard(PrintStream out) {
        String[] headers = {" h ", " f ", " g ", " e ", " d ", " c ", " b ", " a "};
        drawHeaders(out, headers);
        gameBoard.resetBoard();
        gameBoard.changeBoard();
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            int answer = (boardRow % 2 );
            boolean isEven = answer != 1;
            drawRowOfSquares(out, isEven, boardRow+1, "black");
            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
        }
        drawHeaders(out, headers);


    }


    private static void drawWhiteChessBoard(PrintStream out){
        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        drawHeaders(out, headers);
        gameBoard.resetBoard();
        int currentRow = 8;
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            int answer = (boardRow % 2 );
            boolean isEven = answer != 1;
            drawRowOfSquares(out, isEven, currentRow, "white");
            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
            currentRow --;
        }
        drawHeaders(out, headers);
    }



}
