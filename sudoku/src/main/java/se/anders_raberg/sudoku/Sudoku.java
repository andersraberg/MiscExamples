package se.anders_raberg.sudoku;

public class Sudoku {
    private static final int BLOCK_COLUMNS = 3;
    private static final int BLOCK_ROWS = 3;
    private static final int NUMBERS = 10;
    private static final int ROWS = 9;
    private static final int COLUMNS = 9;
    private final int[][] _grid;
    private int[][] _result;

    public Sudoku(int[][] grid) {
        _grid = copy(grid);
        solve();
    }

    public int[][] getResult() {
        return copy(_result);
    }

    private boolean possible(int y, int x, int n) {
        for (int i = 0; i < COLUMNS; i++) {
            if (_grid[y][i] == n) {
                return false;
            }
        }

        for (int i = 0; i < ROWS; i++) {
            if (_grid[i][x] == n) {
                return false;
            }
        }

        int x0 = (x / BLOCK_COLUMNS) * BLOCK_COLUMNS;
        int y0 = (y / BLOCK_ROWS) * BLOCK_ROWS;

        for (int i = 0; i < BLOCK_ROWS; i++) {
            for (int j = 0; j < BLOCK_COLUMNS; j++) {
                if (_grid[y0 + i][x0 + j] == n) {
                    return false;
                }
            }
        }

        return true;
    }

    private void solve() {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                if (_grid[y][x] == 0) {
                    for (int n = 1; n < NUMBERS; n++) {
                        if (possible(y, x, n)) {
                            _grid[y][x] = n;
                            solve();
                            _grid[y][x] = 0;
                        }
                    }
                    return;
                }
            }
        }
        _result = copy(_grid);
    }

    public static String toString(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                int posVal = grid[y][x];
                sb.append(posVal == 0 ? "-" : String.valueOf(posVal));
                sb.append((x + 1) % BLOCK_COLUMNS == 0 ? "  " : "");
            }
            sb.append("\n");
            sb.append((y + 1) % BLOCK_ROWS == 0 ? "\n" : "");
        }
        return sb.toString();

    }

    private static int[][] copy(int[][] from) {
        int[][] result = new int[COLUMNS][ROWS];
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                result[x][y] = from[x][y];
            }
        }
        return result;

    }

}
