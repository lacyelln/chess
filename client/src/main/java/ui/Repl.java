package ui;

import ui.WebSocket.NotificationHandler;
import websocket.messages.Notification;

import java.util.Scanner;

import static java.awt.Color.*;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_WHITE + "Let's play chess. Sign in to start.");
        System.out.print(SET_TEXT_COLOR_BLUE+ client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print(SET_TEXT_COLOR_WHITE);
            printPrompt();
            String line = scanner.nextLine();
            if(line.equals("help")){
                System.out.print(SET_TEXT_COLOR_BLUE);
            }
            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Exception e) {
                var msg = e.getMessage();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(Notification notification) {
        System.out.println(RED + notification.message());
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + client.getCurrentState()  + " >>> " + SET_TEXT_COLOR_WHITE);
    }

}

