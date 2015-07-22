/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.velandia.superwordsearch;

import co.velandia.superwordsearch.LetterGrid.Directions;
import java.util.Stack;

/**
 *
 * @author cesar
 */
public class WordTreeSearch {

//    private boolean[] marked;    // marked[v] = is there an s-v path?
//    private int[] edgeTo;        // edgeTo[v] = last edge on s-v path
//    private final int s;         // source vertex
    /**
     * Computes a path between <tt>s</tt> and every other vertex in graph
     * <tt>G</tt>.
     *
     * @param G the graph
     * @param s the source vertex
     */
    public WordTreeSearch(WordTreeNode G, int s) {
//        this.s = s;
//        edgeTo = new int[G.V()];
//        marked = new boolean[G.V()];
//        dfs(G, s);
    }
//
//    // depth first search from v
//    private void dfs(WordTreeNode G, int v) {
//        marked[v] = true;
//        for (int w : G.adj(v)) {
//            if (!marked[w]) {
//                edgeTo[w] = v;
//                dfs(G, w);
//            }
//        }
//    }

    public static <T> boolean depthFirstSearch(WordTreeNode<GridCoordinates> start, GoalFunction<T> isGoal, Stack<WordTreeNode<GridCoordinates>> result, Directions direction) {

        System.out.println("start=" + start.getValue());

        GridCoordinates current = start.getValue();

        for (WordTreeNode el : result) {
            System.out.println("\t=>" + el.value);
        }

            //ensure we're not stuck in a cycle
        //TODO: remove, not needed in dags
        // if (result.contains(start))
        // {
        //  return false;
        // }
        /*check conditions here to do a push*/
            //1. same direction
            //2. start is not being used
//        if (current != null && current.isOccupied()) {
//            direction = null;
//        }

        //TODO: mark as in use
        if (current != null) {
            current.setOccupied();
        }

        result.push(start);
        
        System.out.println("last dir "+current+ " "+ direction);

//            check if we've found the goal
//        if (isGoal.evaluate(start)) {
//            System.out.println("reached goal");
//            return true;
//        }
          if (isGoal.evaluate(start)) {
            System.out.println("reached goal");
            return true;
            }

//            expand each child node in order, returning if we find the goal
        for (WordTreeNode<GridCoordinates> w : start.getChildren()) {

            Directions d = LetterGrid.compareCoordinates(current, w.getValue());
//                
//                if (direction==null)
//                    d = direction;

            System.out.println("Comparing " + current + " vs " + w.getValue() + " = " + d);

            if (d != null || direction == d) {
                if (depthFirstSearch(w, isGoal, result, d)) {
                    return true;
                }
            } else {
                System.out.println("changing direction");
                result.pop();
                return false;
            }

        }

            //TODO: mark as not in use
        // No path was found
        result.pop();
        return false;
    }

    /**
     * Is there a path between the source vertex <tt>s</tt> and vertex
     * <tt>v</tt>?
     *
     * @param v the vertex
     * @return <tt>true</tt> if there is a path, <tt>false</tt> otherwise
     */
//    public boolean hasPathTo(int v) {
//        return marked[v];
//    }
    /**
     * Returns a path between the source vertex <tt>s</tt> and vertex
     * <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     *
     * @param v the vertex
     * @return the sequence of vertices on a path between the source vertex
     * <tt>s</tt> and vertex <tt>v</tt>, as an Iterable
     */
//    public Iterable<Integer> pathTo(int v) {
//        if (!hasPathTo(v)) return null;
//        Stack<Integer> path = new Stack<Integer>();
//        for (int x = v; x != s; x = edgeTo[x])
//            path.push(x);
//        path.push(s);
//        return path;
//    }
}
