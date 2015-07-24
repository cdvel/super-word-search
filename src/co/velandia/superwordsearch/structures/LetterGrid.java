package co.velandia.superwordsearch.structures;

import co.velandia.superwordsearch.SuperWordSearch.Mode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cesar
 */
public class LetterGrid {

    private static int rowCount;
    private static int columnCount;
    private static Mode mode = Mode.NO_WRAP;

    private Map<String, List<GridCoordinates>> letterGrid = new HashMap<>();

    public LetterGrid() {
        columnCount = 0;
        rowCount = 0;
    }
    
    public void setRowCount(int nRows) {
        rowCount = nRows;
    }

    public void setColumnCount(int mColumns) {
        columnCount = mColumns;
    }

    public boolean isEmpty() {
        return columnCount == 0 && rowCount == 0;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setMode(Mode searchMode) {
        mode = searchMode;
    }
    
    public List<GridCoordinates> getLetterCoordinates(String letter) {
        return new ArrayList<>(letterGrid.get(letter));
    }

    /**
     * Fills a new letter row in the grid 
     * @param letters
     * @param rowIndex
     * @param letterRowCount 
     */
    public void setRow(char[] letters, int rowIndex, int letterRowCount) {
        int columnIndex = 0;
        GridCoordinates letterCoordinate;

        for (char letter : letters) {

            letterCoordinate = new GridCoordinates(rowIndex, columnIndex);
            List<GridCoordinates> coordinates = letterGrid.get("" + letter);

            if (coordinates == null) {
                coordinates = new ArrayList<>();
            }
            coordinates.add(letterCoordinate);
            letterGrid.put("" + letter, coordinates);
            columnIndex++;
        }

    }

    /**
     * Displaces <tt>current </tt> to <tt>direction</tt> 
     * @param current
     * @param direction
     * @return new coordinates, otherwise <tt>current</tt>
     */
    public static GridCoordinates shift(GridCoordinates current, GridDirection direction) {
        GridCoordinates next = new GridCoordinates(current);

        switch (direction) {
            case N:
                next.shiftUp();
                break;
            case NE:
                next.shiftUp().shiftRight();
                break;
            case E:
                next.shiftRight();
                break;
            case SE:
                next.shiftDown().shiftRight();
                break;
            case S:
                next.shiftDown();
                break;
            case SW:
                next.shiftDown().shiftLeft();
                break;
            case W:
                next.shiftLeft();
                break;
            case NW:
                next.shiftUp().shiftLeft();
                break;
        }

        if (mode == Mode.WRAP) {
            wrapAround(next);
        }

        if (isCoordinatePairValid(next))
            return next;
        
        return current;
    }

    /**
     * Wraps coordinates around the grid
     * @param current
     * @return new coordinate, otherwise <tt>current</tt>
     */
    public static GridCoordinates wrapAround(GridCoordinates current) {
        if (current.row < 0) {
            current.row = rowCount - 1;
        }

        if (current.row >= rowCount) {
            current.row = 0;
        }

        if (current.column < 0) {
            current.column = columnCount - 1;
        }

        if (current.column >= columnCount) {
            current.column = 0;
        }

        return current;
    }

    /**
     * *
     * Get direction between adjacent coordinates<tt>x</tt> and <tt>y</tt>
     *
     * @param x
     * @param y
     * @return Direction between coordinates, otherwise null
     */
    public static GridDirection compareCoordinates(GridCoordinates x, GridCoordinates y) {

        if (x == null || y == null) {
            return null;
        }

        if (shift(x, GridDirection.N).equals(y)) {
            return GridDirection.N;
        }

        if (shift(x, GridDirection.NE).equals(y)) {
            return GridDirection.NE;
        }

        if (shift(x, GridDirection.E).equals(y)) {
            return GridDirection.E;
        }

        if (shift(x, GridDirection.SE).equals(y)) {
            return GridDirection.SE;
        }

        if (shift(x, GridDirection.S).equals(y)) {
            return GridDirection.S;
        }

        if (shift(x, GridDirection.SW).equals(y)) {
            return GridDirection.SW;
        }

        if (shift(x, GridDirection.W).equals(y)) {
            return GridDirection.W;
        }

        if (shift(x, GridDirection.NW).equals(y)) {
            return GridDirection.NW;
        }

        return null;
    }

    /**
     * Sanity check for shift function
     * @param coordinates
     * @return true if <tt>coordinates</tt> are valid, false otherwise
     */
    private static boolean isCoordinatePairValid(GridCoordinates coordinates) {
        return (coordinates.row >= 0 && coordinates.row < rowCount && coordinates.column >= 0 && coordinates.column < columnCount);
    }

    @Override
    public String toString() {
        Iterator it = letterGrid.entrySet().iterator();
        StringBuilder buffer = new StringBuilder();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            buffer.append(pair.getKey()).append(" -> ").append(pair.getValue()).append("\n");
        }
        return buffer.toString();
    }

}
