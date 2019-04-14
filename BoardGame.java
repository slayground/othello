class BoardGame {
    public static void main(String[] args) {
        System.out.println("Boardgame sucks");
        Othello othello = new Othello();
        othello.possibleMoves('B');

        othello.displayPossibleMoves();
    }
}
