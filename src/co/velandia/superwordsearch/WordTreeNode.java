/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.velandia.superwordsearch;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author cesar
 */
public class WordTreeNode<T>
    {
        private LinkedList<WordTreeNode<T>> children = new LinkedList<>();
        private Set<WordTreeNode<T>> visited = new HashSet<>();
        public T value;
        public int level=1;

        public T getValue(){
            return value;
        }
        
        public void setValue(T val){
            value = val;
        }

        public WordTreeNode(T value)
        {
            this.value = value;
        }
        public LinkedList<WordTreeNode<T>> getChildren()
        {
            return children;
        }
        
        public WordTreeNode<T> addChild(WordTreeNode<T> child)
        {
            children.add(child);
            return child;
        }
        
        public void addChildren(LinkedList<WordTreeNode<T>> ch)
        {
            for (WordTreeNode<T> t: ch)
                children.add(t);
        }
        
        // TODO: make generic
        public void addChildren(List<GridCoordinates> ch)
        {
            for (GridCoordinates t: ch)
                children.add(new WordTreeNode(t));
        }


        public String toString(int level) {
            
            StringBuilder out = new StringBuilder("\n");
            
            for (int i = 0; i < level; i++) { 
                out.append("\t");
            }
            
            Object current = (this.value == null)?"root":this.value;
            out.append(current);
            
            for(WordTreeNode<T> child: children)
            {
                out.append(child.toString(level+1));
            }
            
            return out.toString();
        }
        
        public String /*List<GridCoordinates>*/ dfs(WordTreeNode<T> node, LetterGrid.Directions direction) {
            
            System.out.println("\nENTERING"+ node.getValue()+" - "+ direction);
            
            if(node.children.isEmpty())
                return null;
            
            StringBuilder something = new StringBuilder("\n");
                    
                
            for(WordTreeNode<T> child: children)
            {
                //System.out.println("in for");
                
                LetterGrid.Directions newDirection = null;
                if (node.value != null){
                    newDirection = LetterGrid.compareCoordinates((GridCoordinates) node.value, (GridCoordinates) child.value);
                    System.out.println((GridCoordinates)node.value+", "+(GridCoordinates)child.value);
                    //System.out.println(direction +" -> "+ newDirection);
                }
                if (newDirection != null && newDirection == direction){
                    //System.out.println("EQUAL "+newDirection);
                    //something.append("EQUAL "+newDirection);
                }
                else{
                    
                    //System.out.println("NOT EQUAL "+newDirection+", "+direction);
                    //something.append("NOT EQUAL "+newDirection+", "+direction);
                    
                    // if direction has changed, stop
                    if(direction!= null)
                        return null;
                }
                something.append(child.dfs(child, newDirection));
            }                    
            
            return something.toString();
//            StringBuilder out = new StringBuilder("\n");
//            
//            for (int i = 0; i < level; i++) { 
//                out.append("\t");
//            }
//            
//            Object current = (this.value == null)?"root":this.value;
//            out.append(current);
//            
//            for(TreeNode<T> child: children)
//            {
//                out.append(child.toString(level+1));
//            }
            
            //return out.toString();
        }
        
        public void addToLeaves(List<GridCoordinates> leaves)
        {
            if (children.isEmpty())
                addChildren(leaves);
            else{
                for (WordTreeNode<T> child:children){
                    child.addToLeaves(leaves);
                }
            }
        }
                
    }
