package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class Chess {

    //board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);


        drawBlackChessBoard(out);


        out.println();
        out.println();

        drawWhiteChessBoard(out);


        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLACK);



    }

    private static void drawHeaders(PrintStream out, String[] headers){
        out.print(SET_BG_COLOR_DARK_GREY);

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol){
            out.print(headers[boardCol]);


        }
        out.println();

    }

    private static void drawRowOfSquares(PrintStream out, boolean isEvenRow, int row) {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_DARK_GREY);

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if(squareRow == 0 && boardCol == 0){
                    out.print(row);
                }
                if(squareRow == 1 && boardCol == 0){
                    out.print(SET_TEXT_COLOR_DARK_GREY);
                    out.print(boardCol+1);
                }
                if ((isEvenRow && boardCol % 2 == 0) || (!isEvenRow && boardCol % 2 != 0)) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }
                out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
            }
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(SET_BG_COLOR_DARK_GREY);
            if(squareRow == 0){
                out.print(row);
            }
            out.println();
        }
    }


    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

//    private static void printPlayer(PrintStream out, String player) {
//        out.print(SET_BG_COLOR_WHITE);
//        out.print(SET_TEXT_COLOR_BLACK);
//
//        out.print(player);
//
//        setWhite(out);
//    }

    private static void drawBlackChessBoard(PrintStream out) {
        String[] headers = {"    a     ", "b     ", "c     ", "d     ", "e     ", "f     ", "g     ", "h     "};
        drawHeaders(out, headers);
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            int answer = (boardRow % 2 );
            boolean isEven = answer != 1;
            drawRowOfSquares(out, isEven, boardRow+1);
            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
        }
        drawHeaders(out, headers);

    }

    private static void drawWhiteChessBoard(PrintStream out){
        String[] headers = {"    h     ", "g     ", "f     ", "e     ", "d     ", "c     ", "b     ", "a     "};
        drawHeaders(out, headers);
        int currentRow = 8;
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            int answer = (boardRow % 2 );
            boolean isEven = answer != 1;
            drawRowOfSquares(out, isEven, currentRow);
            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
            currentRow --;
        }
        drawHeaders(out, headers);
    }



}
