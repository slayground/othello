import java.util.ArrayList;
import java.util.Scanner;

class Othello {
    public static void main(String[] args) {
        System.out.println("Boardgame sucks");
        Board game = new Board();

        game.possibleMoves('B');
        game.possibleMoves('W');

        System.out.println("Current game status is " + game.gameStatus());
        Scanner scanner = new Scanner(System.in);

        // while (game.gameStatus() == 3) {
        //     System.out.print("Your next move: ");
        //     try {
        //         String input = scanner.nextLine();
        //         if (verifyInput(input)) {
        //             System.out.println("Valid input of " + input);
        //         } else {
        //             System.out.println("'Invalid input. Must be XX with X from 0-7");
        //         }
        //     }
        //     catch (java.util.InputMismatchException e) {
        //         System.out.println("Invalid input, please try again");
        //     }

        //     game.displayTable();
        // }

        game.displayPossibleMoves();

        game.newMove('B', 4, 5);
        game.newMove('W', 5, 3);
        game.newMove('B', 3, 2);
        game.newMove('W', 2, 3);
        game.newMove('B', 4, 2);
        game.newMove('W', 3, 1);
        game.newMove('B', 3, 0);

        // white has no move
        game.newMove('B', 6, 3);
        // now white has a move
        game.newMove('W', 7, 3);
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
}
