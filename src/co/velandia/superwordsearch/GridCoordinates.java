package co.velandia.superwordsearch;

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
    CoordinatesState state;

    public GridCoordinates(int row, int column) {
//        this.index = row+column;
        this.row = row;
        this.column = column;
        state = CoordinatesState.EMPTY;

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

    void setEmpty() {
        state = CoordinatesState.EMPTY;
    }

    void setOccupied() {
        state = CoordinatesState.OCCUPPIED;
    }

    boolean isOccupied() {
        return state == CoordinatesState.OCCUPPIED;
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
        String mark = (state == CoordinatesState.OCCUPPIED) ? "*" : "_";
        return "(" + row + ", " + column + ")" + mark;
    }

}
