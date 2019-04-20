// Board: Othello Game Structure

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

class Board {

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

    // Current discs currently on the table of B and W
    static ArrayList<String> CURRENT_BLACKS = new ArrayList<String>();
    static ArrayList<String> CURRENT_WHITES = new ArrayList<String>();

    static ArrayList<String> POSSIBLE_MOVES = new ArrayList<String>();

    // Get possible moves and an aray of possible discs to be flipped
    static ArrayList<String> POSSIBLE_MOVES_BLACK = new ArrayList<String>();
    static Map<String, ArrayList<ArrayList<String>>> POSSIBLE_SCENARIOS_BLACK = new HashMap<String, ArrayList<ArrayList<String>>>();

    static ArrayList<String> POSSIBLE_MOVES_WHITE = new ArrayList<String>();
    static Map<String, ArrayList<ArrayList<String>>> POSSIBLE_SCENARIOS_WHITE = new HashMap<String, ArrayList<ArrayList<String>>>();

    // generate a default 8x8 board with 2 blacks and 2 whites at the middle
    public Board() {

        for (int row = 0; row < NUM_OF_ROW_COLUMN; row++) {
            for (int col = 0; col < NUM_OF_ROW_COLUMN; col++) {
                TABLE[row][col] = EMPTY;
            }
        }

        initiateTable();
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
    }

    // Since the input from scanner is String -> we extract x and y coordinate to an array
    public int[] convertInput(String input) {
        int x = Character.getNumericValue(input.charAt(0));
        int y = Character.getNumericValue(input.charAt(1));
        int[] result = { x, y };
        return result;
    }

    // Get current status of the game
    // 1: Full table -> decides winner based on number of discs on the table
    // 2: One of the players have no disc on table left
    // 3: Not 1 and 2 -> keep playing
    public int gameStatus() {
        if (CURRENT_BLACKS.size() + CURRENT_WHITES.size() == NUM_OF_ROW_COLUMN * NUM_OF_ROW_COLUMN) {
            return 1;
        } else if (POSSIBLE_MOVES_BLACK.size() == 0 && POSSIBLE_MOVES_WHITE.size() == 0) {
            return 2;
        } else {
            return 3;
        }
    }

    // Return the winner when game status != 3
    public int[] getWinner() {
        int blackSize = CURRENT_BLACKS.size();
        int whiteSize = CURRENT_WHITES.size();
        int winner;

        if (blackSize > whiteSize) {
            winner = 1;
        } else if (blackSize < whiteSize) {
            winner = 2;
        } else {
            winner = 3; // tie game
        }

        int[] result = { winner, blackSize, whiteSize };

        return result;
    }

    // WHITE - BOT makes a move randomly based on possible options
    public void botMove() {
        int size = POSSIBLE_MOVES_WHITE.size();

        if (size == 0) {
            System.out.println("No move for WHITE. Bot lose round.");
            return;
        } else {
            Random ran = new Random();
            String move = POSSIBLE_MOVES_WHITE.get(ran.nextInt(size));

            int[] xy = convertInput(move);
            System.out.println("Bot made a move at " + move);
            newMove(WHITE, xy[0], xy[1]);
        }
    }

    // Return the number of possible moves can be made by a player
    public int getNumChoices(char player) {
        if (player == BLACK) {
            return POSSIBLE_MOVES_BLACK.size();
        } else {
            return POSSIBLE_MOVES_WHITE.size();
        }
    }

    // BLACK - PLAYER make a new move
    public void newMove(char player, int posX, int posY) {

        String pos = "" + posX + posY;

        if (player == BLACK) {
            TABLE[posX][posY] = BLACK;

            // Add the position of new disc to the BLACK discs
            CURRENT_BLACKS.add(pos);

            // Flip all the discs in between 
            flipSign(WHITE, BLACK, POSSIBLE_SCENARIOS_BLACK.get(pos));
        } else if (player == WHITE) {
            TABLE[posX][posY] = WHITE;

            // Add the position of new disc to the WHITE discs
            CURRENT_WHITES.add(pos);

            // Flip all the discs in between 
            flipSign(BLACK, WHITE, POSSIBLE_SCENARIOS_WHITE.get(pos));
        }

        // Since table changes -> clear possible moves and resets
        POSSIBLE_MOVES_BLACK.clear();
        POSSIBLE_MOVES_WHITE.clear();

        POSSIBLE_SCENARIOS_BLACK.clear();
        POSSIBLE_SCENARIOS_WHITE.clear();

        possibleMoves(BLACK);
        possibleMoves(WHITE);
    }

    // Flip the sign of all elements between
    public void flipSign(char oldValue, char newValue, ArrayList<ArrayList<String>> arrays) {

        for (int list = 0; list < arrays.size(); list++) {
            for (int index = 0; index < arrays.get(list).size(); index++) {
                int[] pos = convertInput(arrays.get(list).get(index));
                int posX = pos[0];
                int posY = pos[1];

                TABLE[posX][posY] = newValue;

                String flippedItem = "" + posX + posY;

                // Add and remove flipped elements from WHITES and BLACKS
                if (oldValue == BLACK) {
                    CURRENT_BLACKS.remove(flippedItem);
                    CURRENT_WHITES.add(flippedItem);
                } else if (oldValue == WHITE) {
                    CURRENT_WHITES.remove(flippedItem);
                    CURRENT_BLACKS.add(flippedItem);
                }
            }
        }
    }

    // Make sure that the coordinate the move to be made is empty (not B or W)
    public boolean validateInput(char player, int posX, int posY) {

        String pos = "" + posX + posY;

        if (player == BLACK) {

            if (POSSIBLE_SCENARIOS_BLACK.containsKey(pos)) {
                return true;
            } else {
                return false;
            }
        } else {

            if (POSSIBLE_SCENARIOS_WHITE.containsKey(pos)) {
                return true;
            } else {
                return false;
            }
        }

    }

    // Generate all possible moves of a player based on current table
    public void possibleMoves(char player) {
        if (player == BLACK) {
            for (int i = 0; i < CURRENT_BLACKS.size(); i++) {
                String pos = CURRENT_BLACKS.get(i);
                int posX = Character.getNumericValue(pos.charAt(0));
                int posY = Character.getNumericValue(pos.charAt(1));

                // Get all possible moves from all 8 directions
                checkHorizontalLeft(player, posX, posY);
                checkHorizontalRight(player, posX, posY);
                checkVerticalUp(player, posX, posY);
                checkVerticalDown(player, posX, posY);
                checkDiagonalUpLeft(player, posX, posY);
                checkDiagonalUpRight(player, posX, posY);
                checkDiagonalDownRight(player, posX, posY);
                checkDiagonalDownLeft(player, posX, posY);
            }
        } else if (player == WHITE) {
            for (int i = 0; i < CURRENT_WHITES.size(); i++) {
                String pos = CURRENT_WHITES.get(i);
                int posX = Character.getNumericValue(pos.charAt(0));
                int posY = Character.getNumericValue(pos.charAt(1));

                // Get all possible moves from all 8 directions
                checkHorizontalLeft(player, posX, posY);
                checkHorizontalRight(player, posX, posY);
                checkVerticalUp(player, posX, posY);
                checkVerticalDown(player, posX, posY);
                checkDiagonalUpLeft(player, posX, posY);
                checkDiagonalUpRight(player, posX, posY);
                checkDiagonalDownRight(player, posX, posY);
                checkDiagonalDownLeft(player, posX, posY);
            }
        }
    }

    // BASIC IDEA OF CHECKING POSSIBLE MOVE
    //  return if at edge
    //  else if (adjacent is opposite)
    //      increment/decrement index while adjacent is still opposite of not at edge
    //      add disc to an array betweenElements
    //
    //      after while loop complete (either out of edge or empty position)
    //          if at empty possition
    //              add betweenElements to possible moves of that player

    public void checkHorizontalLeft(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer = getOppositePlayer(player);

        // return if at edge
        if (posY == 0) {
            return;
        } else if (TABLE[posX][posY - 1] == oppositePlayer) {
            // check horizontal left
            posY--;
            while (TABLE[posX][posY] == oppositePlayer && posY > 0) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posY--;
            }
            if (posY >= 0) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;

                    if (player == BLACK) {

                        if (POSSIBLE_SCENARIOS_BLACK.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_BLACK.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_BLACK.put(possible, list);
                            POSSIBLE_MOVES_BLACK.add(possible);
                        }
                    } else {

                        if (POSSIBLE_SCENARIOS_WHITE.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_WHITE.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_WHITE.put(possible, list);
                            POSSIBLE_MOVES_WHITE.add(possible);
                        }
                    }
                }
            }
        }
    }

    public void checkHorizontalRight(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer = getOppositePlayer(player);

        // return if at edge
        if (posY == (NUM_OF_ROW_COLUMN - 1)) {
            return;
        } else if (TABLE[posX][posY + 1] == oppositePlayer) {
            // check horizontal right
            posY++;
            while (TABLE[posX][posY] == oppositePlayer && posY < NUM_OF_ROW_COLUMN - 1) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posY++;
            }
            if (posY <= NUM_OF_ROW_COLUMN) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;

                    if (player == BLACK) {

                        if (POSSIBLE_SCENARIOS_BLACK.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_BLACK.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_BLACK.put(possible, list);
                            POSSIBLE_MOVES_BLACK.add(possible);
                        }
                    } else {

                        if (POSSIBLE_SCENARIOS_WHITE.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_WHITE.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_WHITE.put(possible, list);
                            POSSIBLE_MOVES_WHITE.add(possible);
                        }
                    }
                }
            }
        }
    }

    public void checkVerticalUp(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer = getOppositePlayer(player);

        // return if at edge
        if (posX == 0) {
            return;
        } else if (TABLE[posX - 1][posY] == oppositePlayer) {
            // check vertical up
            posX--;
            while (TABLE[posX][posY] == oppositePlayer && posX > 0) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posX--;
            }
            if (posX >= 0) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;

                    if (player == BLACK) {

                        if (POSSIBLE_SCENARIOS_BLACK.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_BLACK.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_BLACK.put(possible, list);
                            POSSIBLE_MOVES_BLACK.add(possible);
                        }
                    } else {

                        if (POSSIBLE_SCENARIOS_WHITE.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_WHITE.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_WHITE.put(possible, list);
                            POSSIBLE_MOVES_WHITE.add(possible);
                        }
                    }
                }
            }
        }
    }

    public void checkVerticalDown(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer = getOppositePlayer(player);

        // return if at edge
        if (posX == (NUM_OF_ROW_COLUMN - 1)) {
            return;
        } else if (TABLE[posX + 1][posY] == oppositePlayer) {
            // check vertical down
            posX++;
            while (TABLE[posX][posY] == oppositePlayer && posX < NUM_OF_ROW_COLUMN - 1) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posX++;
            }
            if (posX <= NUM_OF_ROW_COLUMN) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;

                    if (player == BLACK) {

                        if (POSSIBLE_SCENARIOS_BLACK.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_BLACK.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_BLACK.put(possible, list);
                            POSSIBLE_MOVES_BLACK.add(possible);
                        }
                    } else {

                        if (POSSIBLE_SCENARIOS_WHITE.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_WHITE.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_WHITE.put(possible, list);
                            POSSIBLE_MOVES_WHITE.add(possible);
                        }
                    }
                }
            }
        }
    }

    public void checkDiagonalUpLeft(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer = getOppositePlayer(player);

        // return if at edge
        if (posX == 0 || posY == 0) {
            return;
        } else if (TABLE[posX - 1][posY - 1] == oppositePlayer) {
            // check vertical up and horizontal left
            posX--;
            posY--;
            while (TABLE[posX][posY] == oppositePlayer && posX > 0 && posY > 0) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posX--;
                posY--;
            }
            if (posX >= 0 || posY >= 0) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;

                    if (player == BLACK) {

                        if (POSSIBLE_SCENARIOS_BLACK.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_BLACK.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_BLACK.put(possible, list);
                            POSSIBLE_MOVES_BLACK.add(possible);
                        }
                    } else {

                        if (POSSIBLE_SCENARIOS_WHITE.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_WHITE.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_WHITE.put(possible, list);
                            POSSIBLE_MOVES_WHITE.add(possible);
                        }
                    }
                }
            }
        }
    }

    public void checkDiagonalUpRight(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer = getOppositePlayer(player);

        // return if at edge
        if (posX == 0 || posY == NUM_OF_ROW_COLUMN - 1) {
            return;
        } else if (TABLE[posX - 1][posY + 1] == oppositePlayer) {
            // check vertical up and horizontal right
            posX--;
            posY++;
            while (TABLE[posX][posY] == oppositePlayer && posX > 0 && posY < NUM_OF_ROW_COLUMN - 1) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posX--;
                posY++;
            }
            if (posX >= 0 || posY <= NUM_OF_ROW_COLUMN - 1) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;

                    if (player == BLACK) {

                        if (POSSIBLE_SCENARIOS_BLACK.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_BLACK.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_BLACK.put(possible, list);
                            POSSIBLE_MOVES_BLACK.add(possible);
                        }
                    } else {

                        if (POSSIBLE_SCENARIOS_WHITE.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_WHITE.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_WHITE.put(possible, list);
                            POSSIBLE_MOVES_WHITE.add(possible);
                        }
                    }
                }
            }
        }
    }

    public void checkDiagonalDownRight(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer = getOppositePlayer(player);

        // return if at edge
        if (posX == NUM_OF_ROW_COLUMN - 1 || posY == NUM_OF_ROW_COLUMN - 1) {
            return;
        } else if (TABLE[posX + 1][posY + 1] == oppositePlayer) {
            // check vertical down and horizontal right
            posX++;
            posY++;
            while (TABLE[posX][posY] == oppositePlayer && posX < NUM_OF_ROW_COLUMN - 1
                    && posY < NUM_OF_ROW_COLUMN - 1) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posX++;
                posY++;
            }
            if (posX >= NUM_OF_ROW_COLUMN - 1 || posY <= NUM_OF_ROW_COLUMN - 1) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;

                    if (player == BLACK) {

                        if (POSSIBLE_SCENARIOS_BLACK.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_BLACK.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_BLACK.put(possible, list);
                            POSSIBLE_MOVES_BLACK.add(possible);
                        }
                    } else {

                        if (POSSIBLE_SCENARIOS_WHITE.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_WHITE.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_WHITE.put(possible, list);
                            POSSIBLE_MOVES_WHITE.add(possible);
                        }
                    }
                }
            }
        }
    }

    public void checkDiagonalDownLeft(char player, int posX, int posY) {
        ArrayList<String> betweenElements = new ArrayList<String>();

        char oppositePlayer = getOppositePlayer(player);

        // return if at edge
        if (posX == NUM_OF_ROW_COLUMN - 1|| posY == 0) {
            return;
        } else if (TABLE[posX + 1][posY - 1] == oppositePlayer) {
            // check vertical up and horizontal left
            posX++;
            posY--;
            while (TABLE[posX][posY] == oppositePlayer && posX < NUM_OF_ROW_COLUMN - 1 && posY > 0) {
                String currentElement = "" + posX + posY;
                betweenElements.add(currentElement);
                posX++;
                posY--;
            }
            if (posX <= NUM_OF_ROW_COLUMN - 1 || posY >= 0) {
                if (TABLE[posX][posY] == EMPTY) {
                    String possible = "" + posX + posY;

                    if (player == BLACK) {

                        if (POSSIBLE_SCENARIOS_BLACK.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_BLACK.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_BLACK.put(possible, list);
                            POSSIBLE_MOVES_BLACK.add(possible);
                        }
                    } else {

                        if (POSSIBLE_SCENARIOS_WHITE.containsKey(possible)) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it
                            ArrayList<ArrayList<String>> list = POSSIBLE_SCENARIOS_WHITE.get(possible);
                            list.add(betweenElements);
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                            list.add(betweenElements);
                            POSSIBLE_SCENARIOS_WHITE.put(possible, list);
                            POSSIBLE_MOVES_WHITE.add(possible);
                        }
                    }
                }
            }
        }
    }

    // Return the opposite player
    public char getOppositePlayer(char player) {
        if (player == BLACK) {
            return WHITE;
        } else {
            return BLACK;
        }
    }

    // Display the table with current discs
    public void displayTable() {
        System.out.println("     0   1   2   3   4   5   6   7");
        System.out.println("   _________________________________");
        for (int row = 0; row < NUM_OF_ROW_COLUMN; row++) {
            // System.out.println(" | | | | | | | | |");
            System.out.print(" " + row + " ");
            for (int col = 0; col < NUM_OF_ROW_COLUMN; col++) {
                System.out.print("| " + TABLE[row][col] + " ");
            }
            System.out.print("|");
            System.out.println();
            System.out.println("   |___|___|___|___|___|___|___|___|");
        }
        System.out.println();
    }

    // Display all the possible moves that BLACK - PLAYER can make
    public void displayPossibleMovesBlack() {
        System.out.println("Possible Moves for BLACK: " + POSSIBLE_MOVES_BLACK.size());
        for (Map.Entry<String, ArrayList<ArrayList<String>>> entry : POSSIBLE_SCENARIOS_BLACK.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    // Display all the possible moves that WHITE - BOT can make
    public void displayPossibleMovesWhite() {
        System.out.println("Possible Moves for WHITE: " + POSSIBLE_MOVES_WHITE.size());
        for (Map.Entry<String, ArrayList<ArrayList<String>>> entry : POSSIBLE_SCENARIOS_WHITE.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}