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

    static final int NUM_OF_PLAYERS = 2;
    static final int NUM_OF_ROW_COLUMN = 8;
    static int NUM_OF_CHECKERS = 4;

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

    static ArrayList<String> POSSIBLE_MOVES_BLACK = new ArrayList<String>();
    static Map<String, ArrayList<String>> POSSIBLE_SCENARIOS_BLACK = new HashMap<String, ArrayList<String>>();

    static ArrayList<String> POSSIBLE_MOVES_WHITE = new ArrayList<String>();
    static Map<String, ArrayList<String>> POSSIBLE_SCENARIOS_WHITE = new HashMap<String, ArrayList<String>>();

    public Othello() {
        for (int row = 0; row < NUM_OF_ROW_COLUMN; row++) {
            for (int col = 0; col < NUM_OF_ROW_COLUMN; col++) {
                TABLE[row][col] = EMPTY;
            }
        }

        initiateTable();

        displayTable();
    }

    public void initiateTable() {

        TABLE[3][4] = BLACK;
        TABLE[4][3] = BLACK;
        TABLE[3][3] = WHITE;
        TABLE[4][4] = WHITE;

        CURRENT_BLACKS.add("34");
        CURRENT_BLACKS.add("43");
        CURRENT_WHITES.add("33");
        CURRENT_WHITES.add("44");

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
    }

    public int[] convertInput(int input) {
        int x = input / 10 - 1;
        int y = input % 10 - 1;
        int[] result = {x, y};
        return result;
    }

    public void newMove(char player, int posX, int posY) {

        POSSIBLE_MOVES.clear();
        POSSIBLE_SCENARIOS.clear();

        String pos = "" + posX + posY;

        if (player == BLACK) {
            CURRENT_BLACKS.add(pos);
            possibleMoves(BLACK);

        }

        validateInput(player, posX, posY);

    }

    public boolean validateInput(char player, int posX, int posY) {

        String pos = "" + posX + posY;

        if (player == BLACK) {
            possibleMoves(BLACK);
            if (POSSIBLE_SCENARIOS.containsKey(pos)) {
                return true;
            } else {
                return false;
            }
        } else {
            possibleMoves(WHITE);
            if  (POSSIBLE_SCENARIOS.containsKey(pos)) {
                return true;
            } else {
                return false;
            }
        }

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
            posY--;
            while (TABLE[posX][posY] == oppositePlayer && posY > 0) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posY--;
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
            posY++;
            while (TABLE[posX][posY] == oppositePlayer && posY < NUM_OF_ROW_COLUMN) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posY++;
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