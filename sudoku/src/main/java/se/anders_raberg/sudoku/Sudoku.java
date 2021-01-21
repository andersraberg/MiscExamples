package se.anders_raberg.sudoku;

public class Sudoku {

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

        System.out.println(toString(GRID));
        solve();
        System.out.println(toString(GRID));
    }

    private static boolean possible(int y, int x, int n) {
        for (int i = 0; i < 9; i++) {
            if (GRID[y][i] == n) {
                return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (GRID[i][x] == n) {
                return false;
            }
        }

        int x0 = (x / 3) * 3;
        int y0 = (y / 3) * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (GRID[y0 + i][x0 + j] == n) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void solve() {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (GRID[y][x] == 0) {
                    for (int n = 1; n < 10; n++) {
                        if (possible(y, x, n)) {
                            GRID[y][x] = n;
                            solve();
                            GRID[y][x] = 0;
                        }
                    }
                    return;
                }
            }
        }
        // At this point, the solution is correct.
        System.out.println(toString(GRID));
    }

    private static String toString(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                int posVal = grid[y][x];
                sb.append(posVal == 0 ? "-" : String.valueOf(posVal));
                sb.append((x + 1) % 3 == 0 ? "  " : "");
            }
            sb.append("\n");
            sb.append((y + 1) % 3 == 0 ? "\n" : "");
        }
        return sb.toString();

    }

}
