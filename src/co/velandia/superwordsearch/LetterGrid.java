package co.velandia.superwordsearch;


import co.velandia.superwordsearch.SuperWordSearch.Mode;
import static co.velandia.superwordsearch.SuperWordSearch.Mode;
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

    void setRowCount(int nRows) {
        rowCount = nRows;
    }

    void setColumnCount(int mColumns) {
        columnCount = mColumns;
    }

    boolean isEmpty() {
        return columnCount == 0 && rowCount == 0;
    }

    int getRowCount() {
        return rowCount;
    }

    int getColumnCount() {
        return columnCount;
    }

    public enum Directions {

        N, NE, E, SE, S, SW, W, NW
    }

    private int rowCount;
    private int columnCount;
    private Mode mode = Mode.NO_WRAP;
    
    private Map<String, List<GridCoordinates>> letterGrid = new HashMap<>();
    

    public LetterGrid() {
        this.columnCount = 0;
        this.rowCount = 0;
    }
    
    public void findWord(String word) {
        //searchLetter -> searchInAllDirections
    }

    public List<GridCoordinates> getLetterCoordinates(String letter) {
        return letterGrid.get(letter);
    }

    public void findWordInDirection(String word, int[] position) {
        //call searchInDirection(word, position, direction) for all directions
    }

    public void setRow(char[] letters, int rowIndex, int letterRowCount) {
        int columnIndex = 0;
        GridCoordinates letterCoordinate;

        for (char letter : letters) {

            letterCoordinate = new GridCoordinates(rowIndex, columnIndex);
            List<GridCoordinates> coordinates = this.letterGrid.get("" + letter);

            if (coordinates == null) {
                coordinates = new ArrayList<>();
            }
            coordinates.add(letterCoordinate);
            this.letterGrid.put("" + letter, coordinates);
            columnIndex++;
        }
      
    }

    @Override
    public String toString() {
        Iterator it = letterGrid.entrySet().iterator();
        StringBuilder buffer = new StringBuilder();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            buffer.append(pair.getKey()).append(" -> ").append(pair.getValue()).append("\n");
        }
        //it.remove();
        return buffer.toString();
    }

    public GridCoordinates wrapAround(GridCoordinates current) {
        if (current.row < 0) 
            current.row = rowCount-1;
        
        if (current.row >= rowCount) 
            current.row = 0;
        
        if (current.column < 0) 
            current.column = columnCount-1;
        
        if (current.column >= columnCount) 
            current.column = 0;

        return current;
    }

    public GridCoordinates shift(GridCoordinates current, Directions direction) {
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
            next = wrapAround(next);
        }

        if (isCoordinatePairValid(next)) {
            return next;
        }
        // if invalid, return original coordinates
        return current;
    }

//    public List<GridCoordinates> getNeighbors(GridCoordinates){
//        
//        List<GridCoordinates> neighbors = new ArrayList<>();
//        
//        for (Directions direction : Directions.values()){
//            
//        }
//        
//        return null;
//    }
    
    
    /***
     * Get direction between 2 adjacent coordinates, or null
     * @param x
     * @param y
     * @return 
     */
    public Directions compareCoordinates(GridCoordinates x, GridCoordinates y){
        
        if (shift(x, Directions.N).equals(y)) 
            return Directions.N;

        if (shift(x, Directions.NE).equals(y)) 
            return Directions.NE;

        if (shift(x, Directions.E).equals(y)) 
            return Directions.E;

        if (shift(x, Directions.SE).equals(y)) 
            return Directions.SE;

        if (shift(x, Directions.S).equals(y)) 
            return Directions.S;

        if (shift(x, Directions.SW).equals(y)) 
            return Directions.SW;

        if (shift(x, Directions.W).equals(y)) 
            return Directions.W;                    

        if (shift(x, Directions.NW).equals(y)) 
            return Directions.NW;              

        return null;
    }
    
    
    private boolean isCoordinatePairValid(GridCoordinates coordinates) {
        return  (coordinates.row >= 0 && coordinates.row < rowCount  && coordinates.column >= 0 && coordinates.column < columnCount);
    }

}
