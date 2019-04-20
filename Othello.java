// Othello: Flow of the game

import java.util.ArrayList;
import java.util.Scanner;

class Othello {

    private static final char BLACK = 'B';
    private static final char WHITE = 'W';
    public static void main(String[] args) {

        System.out.println("--> Welcome to Othello.");
        System.out.println();
        System.out.println("--> The goal of the game is to outscore the opponent on the board.");
        System.out.println("----> If you can place a disk so that on both ends are your discs");
        System.out.println("----> and everything in the middle belongs to your opponent:");
        System.out.println("--------> Flip all elements in between to yours.");
        System.out.println("--------> Don't understand? http://bit.ly/tryothello");
        System.out.println();
        System.out.println("--> How to win? Play until both are out of moves.");
        System.out.println("----> You are BLACK.");
        System.out.println("----> Bot is WHITE.");
        System.out.println("--------> You go first because I like you! Let's go.");
        System.out.println();
        
        Board game = new Board();

        // generate possible moves for the default table
        game.possibleMoves(BLACK);
        game.possibleMoves(WHITE);

        int round = 1;
        
        // When game status = 3 (no winners yet) -> keep playing
        // Player is BLACK. Bot is WHITE
        // Each player take turn or will lose round if there's no move to make
        // When game status != 3 (both ran out moves or one has no discs left) -> decide winner
        while (game.gameStatus() == 3) {
            System.out.println("Round " + round);

            game.displayTable();

            game.displayPossibleMovesBlack();

            // skip this round of PLAYER if there's no move to be made
            if (game.getNumChoices(BLACK) == 0) {
                System.out.println("No move this round for BLACK. Player lose round.");
            } else {
                String input = getInput(game);
                int posX = Character.getNumericValue(input.charAt(0));
                int posY= Character.getNumericValue(input.charAt(1));
                game.newMove(BLACK, posX, posY);
                game.displayTable();
            }

            game.displayPossibleMovesWhite();
            if (game.gameStatus() == 3) {
                game.botMove();
            } else {
                break;
            }
            round++;
            System.out.println("___________________________");
        }

        // After while loop ends -> decide winner

        System.out.println("FINAL TABLE");
        System.out.println();
        game.displayTable();

        int[] result = game.getWinner();
        int winnerCode = result[0];

        if (winnerCode == 1) {
            System.out.println("Winner is BLACK - PLAYER.");
        } else if (winnerCode == 2) {
            System.out.println("Winner is WHITE - BOT");
        } else {
            System.out.println("Tie game.");
        }

        System.out.println("BLACK has " + result[1]);
        System.out.println("WHITE has " + result[2]);
    }

    // Verify input from scanner so that it must be XX with X from 0-7
    public static boolean verifyInput(String input) {
        String allowedStr = new String("01234567");
        ArrayList<Character> allowed = new ArrayList<Character>();
 
        for(int i = 0; i< allowedStr.length(); i++){
            allowed.add(allowedStr.charAt(i));
        }

        if (input.length() != 2) {
            return false;
        }

        if (!allowed.contains(input.charAt(0))) {
            return false;
        }

        if (!allowed.contains(input.charAt(1))) {
            return false;
        }

        return true;
    }

    // Get input from scanner, verify, and return
    public static String getInput(Board game) {
        String result = "";
        Scanner scanner = new Scanner(System.in);

        while (result == "") {
            System.out.print("Your move: ");
            try {
                String input = scanner.nextLine();
                if (verifyInput(input)) {
                    // System.out.println("Valid input of " + input);
                    // result = input;

                    int posX = Character.getNumericValue(input.charAt(0));
                    int posY= Character.getNumericValue(input.charAt(1));

                    if (game.validateInput(BLACK, posX, posY)) {
                        result = input;
                        System.out.println("Valid input. You made a move at " + input);
                    } else {
                        System.out.println("Valid input already been made");
                    }

                } else {
                    System.out.println("'Invalid input. Must be XX with X from 0-7");
                }
            }
            catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input, please try again");
            }
        }

        return result;
    }
}
