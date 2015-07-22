package co.velandia.superwordsearch;

import co.velandia.superwordsearch.LetterGrid.Directions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cesar
 */
public class SuperWordSearch {

//    BEGIN DFS
//    public class PreOrderDFSIterator implements Iterator<String> {
//    private Set<String> visited = new HashSet<String>();
//    private Deque<Iterator<String>> stack = new LinkedList<Iterator<String>>();
//    private TreeNode graph;
//    private String next;
//
//    public PreOrderDFSIterator(TreeNode g, String startingVertex) {
//        this.stack.push(g.getNeighbors(startingVertex).iterator());
//        this.graph = g;
//        this.next = startingVertex;
//    }
//
//    @Override
//    public void remove() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public boolean hasNext() {
//        return this.next != null;
//    }
//
//    @Override
//    public String next() {
//        if (this.next == null) {
//            throw new NoSuchElementException();
//        }
//        try {
//            this.visited.add(this.next);
//            return this.next;
//        } finally {
//            this.advance();
//        }
//    }
//
//    private void advance() {
//        Iterator<String> neighbors = this.stack.peek();
//        do {
//            while (!neighbors.hasNext()) {  // No more nodes -> back out a level
//                this.stack.pop();
//                if (this.stack.isEmpty()) { // All done!
//                    this.next = null;
//                    return;
//                }
//                neighbors = this.stack.peek();
//            }
//
//            this.next = neighbors.next();
//        } while (this.visited.contains(this.next));
//        this.stack.push(this.graph.getNeighbors(this.next).iterator());
//    }
//}
    // END DFS
    private void resetStates(List<GridCoordinates> coordinates) {
        for (GridCoordinates coordinatePair : coordinates) {
            coordinatePair.setEmpty();
        }
    }

    private void setMode(String line) {
        searchMode = (line.trim().equalsIgnoreCase("WRAP")) ? Mode.WRAP : Mode.NO_WRAP;
        gridWorld.setMode(searchMode);
    }

    private List<String> getSearchWords() {
        return searchWords;
    }

    public enum Mode {

        WRAP, NO_WRAP, NOT_SET
    }

    private static LetterGrid gridWorld;
    private static Mode searchMode;
    private static List<String> searchWords;

    public SuperWordSearch() {
        gridWorld = new LetterGrid();
        searchMode = Mode.NOT_SET;
        searchWords = new ArrayList<>();
    }

    @Override
    public String toString() {
        return gridWorld.toString() + "mode: " + searchMode + ", searchWords: " + searchWords.toString();
    }

    /**
     * Load search parameters and grid letters from a filePath
     *
     * @param filePath
     * @throws IOException
     */
    public void loadFromFile(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"));

        int linesRead = 0;
        int pWords = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            for (String line; (line = br.readLine()) != null;) {

                if (gridWorld.isEmpty()) {
                    String[] dimensions = line.split(" ");
                    gridWorld.setRowCount(Integer.parseInt(dimensions[0]));
                    gridWorld.setColumnCount(Integer.parseInt(dimensions[1]));
                } else {
                    if (linesRead < gridWorld.getRowCount()) {
                        gridWorld.setRow(line.toCharArray(), linesRead, gridWorld.getColumnCount());
                        linesRead++;
                    } else {
                        if (searchMode == Mode.NOT_SET) {
                            setMode(line);
                        } else {
                            if (pWords == 0) {
                                pWords = Integer.parseInt(line);
                            } else {
                                searchWords.add(line);
                            }

                        }
                    }
                }
            }
        }
    }

    public void printLetterCoordinates(String letter) {
        System.out.println(gridWorld.getLetterCoordinates(letter));
    }

    public void exploreWordSets(String word) {

        Stack<WordTreeNode<GridCoordinates>> searchStack = new Stack();

        List<List<GridCoordinates>> set = new ArrayList<>();

        for (char ch : word.toCharArray()) {
            set.add(gridWorld.getLetterCoordinates("" + ch));
        }
        //System.out.println("set=== "+set);

        int letterIndex = 0;
        //int [] optionIndex = int[4];
        int optionIndex[] = new int[word.length()];
        Directions dir = null;

        /**
         * Testing tree implementation
         */
//        TreeNode<GridCoordinates> fake = new TreeNode(null);
//        
//        TreeNode<GridCoordinates> node1 = fake.addChild(new TreeNode<>(new GridCoordinates(1,1)));
//        TreeNode<GridCoordinates> node2 = fake.addChild(new TreeNode<>(new GridCoordinates(1,2)));
//        TreeNode<GridCoordinates> node3 = fake.addChild(new TreeNode<>(new GridCoordinates(1,3)));
//        
//        
//        node1.addChild(new TreeNode<>(new GridCoordinates(2,1)));
//        node2.addChild(new TreeNode<>(new GridCoordinates(2,2)));
//        
//        
//        System.out.println("fakey=\n"+ fake.toString(0));
//        for (TreeNode<GridCoordinates>child: fake.children)
//        {
//            System.out.println("\t"+ child.value);
//            for (TreeNode<GridCoordinates>child2: child.children)
//            {    
//                System.out.println("\t\t"+ child2.value);
//            }
//        }
        /**
         * End of testing tree implementation
         */
        WordTreeNode<GridCoordinates> root = new WordTreeNode(null);
        WordTreeNode<GridCoordinates> copy = root;
        for (List<GridCoordinates> list : set) {
            root.addToLeaves(list);
        }

        /* uncomment to print search tree */
        System.out.println(root.toString(0));

        //root.dfs(root, null);
        Directions direction = null;

        WordTreeSearch.depthFirstSearch(root,
                new GoalFunction<GridCoordinates>() {

                    @Override
                    public boolean evaluate(WordTreeNode<GridCoordinates> o) {

                        //System.out.println("evaluating"+o.getValue())+;
                        return (new GridCoordinates(3, 2).equals(o.getValue()));
                    }
                }, searchStack, direction);

        //System.out.println("Search stack"+searchStack);
        for (WordTreeNode el : searchStack) {
            System.out.print(" " + el.value);
        }

        //        null, searchStack)
//        while(searchStack.size()< word.length()){
//        //for (List<GridCoordinates> letterOptions: set){
//    
//            //if(searchStack.isEmpty())
//            List<GridCoordinates> letterOptions = set.get(letterIndex);
//            
//            if(optionIndex[letterIndex]>= letterOptions.size())
//                break;
//            
//            System.out.println("letter index is "+letterIndex);
//            System.out.println("option index is "+optionIndex[letterIndex]);
//            
//            //System.out.println("for letter:"+ word.charAt(letterIndex)+" "+letterOptions);
//            
//            GridCoordinates newCoo = letterOptions.get(optionIndex[letterIndex]);
//            
//            
//            System.out.println("coo"+newCoo);
//            
//            
//            Directions newDir = null;
//            
//            if (!searchStack.isEmpty())
//            {
//                newDir = gridWorld.compareCoordinates((GridCoordinates) searchStack.peek(), newCoo);
//                System.out.println((GridCoordinates) searchStack.peek()+" vs "+ newCoo +" DIR: "+newDir);
//                
//                //compute new direction 
//                
//                if (newDir==null)
//                {
//                    //stay in the same letter, move to next option
//                    System.out.println("step back************ IS NULL");
//                    letterIndex=0; // start from the
//                    optionIndex[letterIndex]++;
//                    dir = null;
//                    if (!searchStack.empty())
//                        searchStack.pop();
////                    System.out.println("letterIndex"+letterIndex);
////                    System.out.println("optionIndex"+optionIndex[letterIndex]);
//                    
//                }else{
//                    if(newDir == dir){
//                        //add lead and keep exploring
//                        searchStack.push(newCoo);
//                        letterIndex++;
//                    }else{
//                        //directions are diff. get back to prev letter
//                        System.out.println("step back************");
//                        letterIndex=0;
//                        optionIndex[letterIndex]++;
//                    }
//                }
//                
//            }else{
//                searchStack.push(newCoo);
//                letterIndex++;
//            }
//            
//
//        System.out.println("stack options = "+searchStack+"\n");
//        }
        //    System.out.println("stack options = "+searchStack);
//                
//                if (streak.isEmpty()){
//                    streak.push(letterSet.get(0));
//                    letterSet.get(0).setOccupied();
//                }
//                else{
//                    prev = (GridCoordinates) streak.peek();
//                    curr = letterSet.get(0);
//                    //System.out.println("state curr?"+curr.state);
////                    if (curr.isOccupied())
////                        break;
////                    else
//                    
//                    if (!prev.equals(curr)){
//                        
//                        //System.out.println("comparing= "+prev +" and "+curr );
//                        Directions evalDir = gridWorld.compareCoordinates(prev, curr);
//                        if (streakDir == null)
//                            streakDir = evalDir;
//                        
//                        if (streakDir != null && streakDir == evalDir /*&& !curr.isOccupied()*/){
//                            //mark as occupied
//                            //streak.push(curr);
//                            //System.out.println("NOT null= : "+streakDir);
//                        }else
//                        {
//                            //NOTE: means streak failed, rollback positions, stop search
//                            //System.out.println("Is null");
//                            //reletterSetStates(streak);
//                           streak.pop();
//                           streakDir = null;
//                           //break;
//                        }
//                        //curr.letterSetOccupied();
//                        streak.push(curr);
//                    }    
//                }
//                System.out.println("==> "+streak);              
//                
//            }
//            
//            if (streak.size() == word.length()){
//                System.out.println(streak.get(0)+" "+streak.get(streak.size()-1));
////                reletterSetStates(streak);
//            }
//            else
//            System.out.println("NOT FOUND");
////            System.out.println("==> "+streak);
        //       }
    }

    public void print() {
        System.out.println(this);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("Please type in your input file...");

        SuperWordSearch sws = new SuperWordSearch();
        try {
            sws.loadFromFile("C:\\sws\\3x3_wrap.txt");
            sws.print();
            for (String word : sws.getSearchWords()) {
                System.out.println("\nSEARCHING... " + word);
                sws.exploreWordSets(word);
            }

            //sws.print();
        } catch (IOException ex) {
            Logger.getLogger(SuperWordSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
