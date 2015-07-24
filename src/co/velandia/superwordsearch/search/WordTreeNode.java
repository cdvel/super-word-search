package co.velandia.superwordsearch.search;

import co.velandia.superwordsearch.structures.GridCoordinates;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cesar
 */
public class WordTreeNode<T>
    {
        private LinkedList<WordTreeNode<T>> children = new LinkedList<>();
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
        
        public void addToLeaves(List<GridCoordinates> leaves)
        {
            if (children.isEmpty())
                addChildren(leaves);
            else{
                for (WordTreeNode<T> child:children)
                    child.addToLeaves(leaves);
                
            }
        }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  WordTreeSearch)
        {
            WordTreeNode node = (WordTreeNode)obj;
            return node.value == value;
        }
        return false;
    }

    @Override
    public String toString() {
        return ""+value;
    }
}
