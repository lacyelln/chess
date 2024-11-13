package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoard {

    //board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        printBoard(out);

    }

    public static void printBoard(PrintStream out){
        out.print(ERASE_SCREEN);

        drawBlackChessBoard(out);

        out.println();
        out.println();

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
                addPieceToBoard(out, board, boardCol, row);
            }
            if (row == 3 || row ==4 || row ==5 || row == 6){
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

    private static void addPieceToBoard(PrintStream out, String board, int boardCol, int row){
        out.print(SET_TEXT_BOLD);
        String[] whiteBoard = {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN,
                WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};
        String[] blackBoard = {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_KING,
                WHITE_QUEEN, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};
        if (Objects.equals(board, "white")) {
            if (row == 1) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(whiteBoard[boardCol]);
            } else if (row == 2) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(WHITE_PAWN);
            }
            else if (row == 7) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(WHITE_PAWN);
            } else if (row == 8){
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(whiteBoard[boardCol]);
            }
        }
        else{
            if (row == 8){
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(blackBoard[boardCol]);
            } else if (row == 7) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(WHITE_PAWN);
            } else if (row == 2){
                out.print(SET_TEXT_COLOR_RED);
                out.print(WHITE_PAWN);
            } else if (row == 1){
                out.print(SET_TEXT_COLOR_RED);
                out.print(blackBoard[boardCol]);
            }

        }
    }


    private static void drawBlackChessBoard(PrintStream out) {
        String[] headers = {" h ", " f ", " g ", " e ", " d ", " c ", " b ", " a "};
        drawHeaders(out, headers);

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
