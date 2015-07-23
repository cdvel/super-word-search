package co.velandia.superwordsearch;

import co.velandia.superwordsearch.LetterGrid.Directions;

/**
 *
 * @author cesar
 */
public class GridCoordinates {

    // TODO: revise use of visited
    public enum CoordinatesState {

        EMPTY, OCCUPPIED, VISITED
    };

    int row;
    int column;
    boolean wrap = false;
    Directions state;

    public GridCoordinates(int row, int column) {
//        this.index = row+column;
        this.row = row;
        this.column = column;
        state = null;

    }

    /**
     * Copy constructor
     *
     * @param copy
     */
    public GridCoordinates(GridCoordinates copy) {
        this.row = copy.row;
        this.column = copy.column;
        this.state = copy.state;
    }

    void unset() {
        state = null;
    }

    void setState(Directions state) {
        this.state = state;
    }

    boolean isSet() {
        return state != null;
    }

    public boolean equals(GridCoordinates obj) {
        if (obj == null) {
            return false;
        }
        return (this.row == obj.row && this.column == obj.column);
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
        String mark = "";
        if (state != null)       
            mark = (state == null) ? "_" : state.name();
        return "(" + row + ", " + column + ")" + mark;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GridCoordinates){
            GridCoordinates co = (GridCoordinates) obj;
            return row == co.row && column == co.column;
        }
        return false;
    }
    
    

}
