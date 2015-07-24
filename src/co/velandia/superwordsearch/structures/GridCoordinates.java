package co.velandia.superwordsearch.structures;

/**
 *
 * @author cesar
 */
public class GridCoordinates {

    int row;
    int column;
    boolean wrap = false;
    GridDirection direction;

    public GridCoordinates(int row, int column) {
//        this.index = row+column;
        this.row = row;
        this.column = column;
        direction = null;

    }

    public GridCoordinates(GridCoordinates copy) {
        row = copy.row;
        column = copy.column;
        direction = copy.direction;
    }

    public void unset() {
        direction = null;
    }

    public void setDirection(GridDirection dir) {
        direction = dir;
    }
    
    public GridDirection getDirection(){
        return direction;
    }

    public boolean isSet() {
        return direction != null;
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
        if (direction != null)       
            mark = (direction == null) ? "_" : direction.name();
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
