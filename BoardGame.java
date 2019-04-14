class BoardGame {
    public static void main(String[] args) {
        System.out.println("Boardgame sucks");
        Othello othello = new Othello();
        
        othello.possibleMoves('B');
        othello.possibleMoves('W');

        othello.displayPossibleMoves();

        othello.newMove('B', 4, 5);
        othello.newMove('W', 5, 3);
        othello.newMove('B', 3, 2);
        othello.newMove('B', 6, 3);
        //othello.newMove('W', 7, 3);
    }
}
