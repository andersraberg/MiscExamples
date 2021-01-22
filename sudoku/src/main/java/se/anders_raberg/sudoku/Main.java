package se.anders_raberg.sudoku;

public class Main {

    private static final int[][] GRID = new int[][] { //
        { 6, 3, 0, 0, 2, 0, 0, 0, 9 }, //
        { 0, 4, 0, 5, 3, 1, 0, 0, 2 }, //
        { 0, 7, 5, 0, 4, 9, 0, 3, 1 }, //
        { 8, 0, 0, 4, 0, 6, 1, 0, 0 }, //
        { 0, 0, 0, 2, 1, 0, 3, 9, 6 }, //
        { 0, 0, 0, 7, 0, 3, 2, 0, 4 }, //
        { 3, 8, 7, 0, 0, 0, 4, 0, 0 }, //
        { 4, 0, 2, 1, 0, 0, 0, 6, 3 }, //
        { 0, 0, 0, 0, 7, 0, 0, 0, 0 } };

public static void main(String[] args) {
    System.out.println(Sudoku.toString(GRID));
    Sudoku solver = new Sudoku(GRID);

    System.out.println(Sudoku.toString(solver.getResult()));
}
}
