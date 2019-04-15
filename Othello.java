import java.util.ArrayList;
import java.util.Scanner;

class Othello {
    public static void main(String[] args) {
        System.out.println("Boardgame sucks");
        Board game = new Board();

        game.possibleMoves('B');
        game.possibleMoves('W');

        while (game.gameStatus() == 3) {

            String input = getInput(game);
            int posX = Character.getNumericValue(input.charAt(0));
            int posY= Character.getNumericValue(input.charAt(1));

            game.newMove('B', posX, posY);

            if (game.gameStatus() == 3) {
                game.botMove();
            } else {
                break;
            }

        }

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

                    if (game.validateInput('B', posX, posY)) {
                        result = input;
                        System.out.println("Valid input. You made a move at " + input);
                    } else {
                        System.out.println("Valid input but can't make moves based on current table");
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
