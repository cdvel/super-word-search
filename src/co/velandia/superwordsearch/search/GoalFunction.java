package co.velandia.superwordsearch.search;

import co.velandia.superwordsearch.structures.GridCoordinates;
import java.util.Stack;

/**
 *
 * @author cesar
 */

public interface GoalFunction<T> {

    boolean evaluate(Stack<WordTreeNode<GridCoordinates>> sequence);
}
