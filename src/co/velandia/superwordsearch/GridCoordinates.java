
package co.velandia.superwordsearch;

/**
 *
 * @author cesar
 */
public class GridCoordinates {

    boolean isOccupied() {
        return state == CoordinatesState.OCCUPPIED;
    }
    
    public enum CoordinatesState {
        EMPTY, OCCUPPIED
    };
    
    void setEmpty() {
        state = CoordinatesState.EMPTY;
    }
    
    void setOccupied() {
        state = CoordinatesState.OCCUPPIED;
    }

    
    int row;
    int column;
    boolean wrap = false;
    CoordinatesState state;
    

    public GridCoordinates (int row, int column) {
//        this.index = row+column;
        this.row = row;
        this.column = column;
        state = CoordinatesState.EMPTY;
                
    }

    public boolean equals(GridCoordinates obj) {
        return (this.row == obj.row && this.column == obj.column);
    }
    
    
    
    /**
     * Copy constructor
     * @param copy 
     */
    public GridCoordinates(GridCoordinates copy){
        this.row = copy.row;
        this.column = copy.column;
        this.state = copy.state;
    }
    
    GridCoordinates shiftUp() {
        row--;
        return this;
    }

    GridCoordinates shiftRight() {
        column++;
        return this;
    }

    GridCoordinates shiftDown() {
        row++;
        return this;
    }

    GridCoordinates shiftLeft() {
        column--;
        return this;
    }

    @Override
    public String toString() {
        String mark = (state==CoordinatesState.OCCUPPIED)?"*":"_";
        return "("+row+", "+column+")"+mark;
    }
    
}
