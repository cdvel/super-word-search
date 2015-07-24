package co.velandia.superwordsearch.search;
import co.velandia.superwordsearch.structures.GridCoordinates;
import co.velandia.superwordsearch.structures.GridDirection;
import co.velandia.superwordsearch.structures.LetterGrid;
import java.util.Stack;

/**
 *
 * @author cesar
 */
public class WordTreeSearch {

    /**
     * Search for a path that contains <tt>word</tt> in tree <tt>start</tt>
     * 
     * @param start search tree for this word
     * @param isGoal function to evaluate search end
     * @param result stack with partial results
     * @param direction direction of current search
     * @param word word represented in this tree
     * @return <tt>true</tt> if the word was found, <tt>false</tt> otherwise
     */
    public static <T> boolean depthFirstSearch(WordTreeNode<GridCoordinates> start, GoalFunction<T> isGoal, Stack<WordTreeNode<GridCoordinates>> result, GridDirection direction, String word) {

        GridCoordinates current = start.getValue();

        if (current != null) {
            current.setDirection(direction);
        }
        result.push(start);
        
        if (isGoal.evaluate(result)) 
            return true;
            

        for (WordTreeNode<GridCoordinates> node : start.getChildren()) {

            if (node.getValue().getDirection() != null)
                return false;
            
            GridDirection dir = LetterGrid.compareCoordinates(current, node.getValue());

            if (dir != null || direction == dir) {
                if (depthFirstSearch(node, isGoal, result, dir, word)) 
                    return true;
            }

        }
        // No path was found, backstep 
        if(result.size()>1)
            result.pop();
        
        return false;
    }
}
