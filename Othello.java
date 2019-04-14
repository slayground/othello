// TODO:
// newMove() -> check valid -> update table
// game flow
// diagonal
// AI

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class Othello {

    // BLACK 'B'
    // WHITE 'W'

    static int NUM_OF_PLAYERS = 2;
    static int NUM_OF_ROW_COLUMN = 8;

    static char[][] TABLE = new char[NUM_OF_ROW_COLUMN][NUM_OF_ROW_COLUMN];
    static final char EMPTY = '_';
    static final char BLACK = 'B';
    static final char WHITE = 'W';
    static final char POSSIBLE = '*';

    static ArrayList<String> CURRENT_BLACKS = new ArrayList<String>();
    static ArrayList<String> CURRENT_WHITES = new ArrayList<String>();

    static ArrayList<String> POSSIBLE_BLACKS = new ArrayList<String>();
    static ArrayList<String> POSSIBLE_WHITES = new ArrayList<String>();

    static ArrayList<String> POSSIBLE_MOVES = new ArrayList<String>();
    static Map<String, ArrayList<String>> POSSIBLE_SCENARIOS = new HashMap<String, ArrayList<String>>();

    public Othello() {
        for (int row = 0; row < NUM_OF_ROW_COLUMN; row++) {
            for (int col = 0; col < NUM_OF_ROW_COLUMN; col++) {
                TABLE[row][col] = EMPTY;
            }
        }

        TABLE[3][4] = BLACK;
        TABLE[4][3] = BLACK;
        TABLE[3][3] = WHITE;
        TABLE[4][4] = WHITE;

        String b34 = "34";
        String b43 = "43";
        String w33 = "33";
        String w44 = "44";
        CURRENT_BLACKS.add(b34);
        CURRENT_BLACKS.add(b43);
        CURRENT_WHITES.add(w33);
        CURRENT_WHITES.add(w44);

        // TEST BOUNDS
        // TABLE[4][2] = BLACK;
        // TABLE[4][1] = BLACK;
        // TABLE[4][0] = BLACK;
        // String b42 = "42";
        // String b41 = "41";
        // String b40 = "40";
        // CURRENT_BLACKS.add(b42);
        // CURRENT_BLACKS.add(b41);
        // CURRENT_BLACKS.add(b40);

        displayTable();
    }

    public int[] convertInput(int input) {
        int x = input / 10 - 1;
        int y = input % 10 - 1;
        int[] result = {x, y};
        return result;
    }

    public void possibleMoves(char player) {
        if (player == BLACK) {
            for (int i = 0; i < CURRENT_BLACKS.size(); i++) {
                String pos = CURRENT_BLACKS.get(i);
                int posX = Character.getNumericValue(pos.charAt(0));
                int posY = Character.getNumericValue(pos.charAt(1));

                checkHorizontal(player, posX, posY);
                checkVertical(player, posX, posY);
                //checkDiagonal(player, posX, posY);
            }
        } else if (player == WHITE) {
            for (int i = 0; i < CURRENT_WHITES.size(); i++) {
                String pos = CURRENT_WHITES.get(i);
                int posX = Character.getNumericValue(pos.charAt(0));
                int posY = Character.getNumericValue(pos.charAt(1));

                checkHorizontal(player, posX, posY);
                checkVertical(player, posX, posY);
                //checkDiagonal(player, posX, posY);
            }
        }
    }

    public void checkHorizontal(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer;
        if (player == BLACK) {
            oppositePlayer = WHITE;
        } else {
            oppositePlayer = BLACK;
        }

        System.out.println("Starts checking " + posX + posY);
        // check horizontal left
        if (TABLE[posX][posY-1] == oppositePlayer) {
            //System.out.println("Starts checking horizontal left");
            posY--;
            while (TABLE[posX][posY] == oppositePlayer && posY > 0) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                //System.out.println("Adding " + currentElement + " with current size is " + betweenElements.size());
                posY--;
                //System.out.println("Currently at " + posX + posY);
            }
            if (posY >= 0) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;
                    POSSIBLE_MOVES.add(possible);
                    POSSIBLE_SCENARIOS.put(possible, betweenElements);
                }
            }
        // check horizontal right    
        } else if (TABLE[posX][posY+1] == oppositePlayer) {
            //System.out.println("Starts checking horizontal right");
            posY++;
            while (TABLE[posX][posY] == oppositePlayer && posY < NUM_OF_ROW_COLUMN) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                //System.out.println("Adding " + currentElement + " with current size is " + betweenElements.size());
                posY++;
                //System.out.println("Currently at " + posX + posY);
            }
            if (posY <= NUM_OF_ROW_COLUMN) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;
                    POSSIBLE_MOVES.add(possible);
                    POSSIBLE_SCENARIOS.put(possible, betweenElements);
                }
            }
        }
    }

    public void checkVertical(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer;
        if (player == BLACK) {
            oppositePlayer = WHITE;
        } else {
            oppositePlayer = BLACK;
        }

        // check vertical up
        if (TABLE[posX-1][posY] == oppositePlayer) {
            posX--;
            while (TABLE[posX][posY] == oppositePlayer && posX > 0) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posX--;
                //System.out.println("Currently at " + posX + posY);
            }
            if (posX >= 0) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;
                    POSSIBLE_MOVES.add(possible);
                    POSSIBLE_SCENARIOS.put(possible, betweenElements);
                }
            }
        // check vertical down
        } else if (TABLE[posX+1][posY] == oppositePlayer) {
            posX++;
            while (TABLE[posX][posY] == oppositePlayer && posX < NUM_OF_ROW_COLUMN) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posX++;
                //System.out.println("Currently at " + posX + posY);
            }
            if (posX <= NUM_OF_ROW_COLUMN) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;
                    POSSIBLE_MOVES.add(possible);
                    POSSIBLE_SCENARIOS.put(possible, betweenElements);
                }
            }
        }
    }

    public void newMove(char player, int posX, int posY) {
        
    }

    public void displayTable() {
        for (int row = 0; row < NUM_OF_ROW_COLUMN; row++) {
            for (int col = 0; col < NUM_OF_ROW_COLUMN; col++) {
                System.out.print(TABLE[row][col]);
            }
            System.out.println();
        }
    }

    public void displayPossibleMoves() {
        System.out.print("Possible moves: ");
        for (int i = 0; i < POSSIBLE_MOVES.size(); i++) {
            String element = POSSIBLE_MOVES.get(i);
            System.out.print(element + " ");
        }

        System.out.println("Possible Scenarios: ");
        for (Map.Entry<String, ArrayList<String>> entry : POSSIBLE_SCENARIOS.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}