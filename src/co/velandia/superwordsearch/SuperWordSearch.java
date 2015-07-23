package co.velandia.superwordsearch;

import co.velandia.superwordsearch.LetterGrid.Directions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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

    private void resetStates(List<GridCoordinates> coordinates) {
        for (GridCoordinates coordinatePair : coordinates) {
            coordinatePair.unset();
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
    
    public String getStack(Stack<WordTreeNode<GridCoordinates>> searchStack) {
        StringBuilder out = new StringBuilder();
        
        for (WordTreeNode el : searchStack) {
            out.append(" ").append(el.value);
        }
        
        return out.toString();
    }
    
    public boolean isMatch(Stack<WordTreeNode<GridCoordinates>> searchStack) {
        
        StringBuilder out = new StringBuilder();
        Directions x = null;
        
        //check for duplicates
        Set set = new HashSet();
   
        // same direction?
        for (int i = 2; i < searchStack.size(); i++) {
            
            
            
            if (!set.add((GridCoordinates)searchStack.get(i).value))
            {
                System.out.println("---------falsey");
                return false;
            }
            
             if (x==null)   
                x = searchStack.get(i).value.state;
             
             if(searchStack.get(i).value.state != x || x == null)
                 return false;
        }
        
        System.out.println("*****set size="+set.size()+" stack"+ searchStack.size());
//                Iterator iterator = set.iterator();
//        while (iterator.hasNext()) {
//            Object object = (Object) iterator.next();
//            System.out.println("i="+object);
//        }
        System.out.println("stack ==="+getStack(searchStack)+" set"+set);
        
        return set.size()+2 == searchStack.size();
        
    }
    
    public void exploreWordSets(String word) {
        
        Stack<WordTreeNode<GridCoordinates>> searchStack = new Stack();
        
        List<List<GridCoordinates>> set = new ArrayList<>();
        
        for (char ch : word.toCharArray()) {
            set.add(gridWorld.getLetterCoordinates("" + ch));
        }
        //System.out.println("set=== "+set);

        //int [] optionIndex = int[4];
        Directions dir = null;


        WordTreeNode<GridCoordinates> root = new WordTreeNode(null);
        
        for (List<GridCoordinates> list : set) {
            root.addToLeaves(list);
        }

        /* uncomment to print search tree */
        System.out.println(root.toString(0));

        //root.dfs(root, null);
        Directions direction = null;
        
        WordTreeSearch.depthFirstSearch(root,
                new GoalFunction< Stack<WordTreeNode<GridCoordinates>>>() {
                    
                    @Override
                    public boolean evaluate(Stack<WordTreeNode<GridCoordinates>> sequence, int length) {
                        
                        if (sequence.size() != length+1)
                            return false;
                        
                        Directions x = null;
                        
//                        System.out.println("stackkk"+getStack(sequence));
                        
                        for (WordTreeNode<GridCoordinates> wordTreeNode : sequence) {
                            
                            // TODO: happens everytime seq is size of word
                            if(wordTreeNode.value != null && wordTreeNode.value.state == null ){
//                                System.out.println("%%%%%found!1"+wordTreeNode.value);
                                return isMatch(sequence);
                            }
                            
                            if (wordTreeNode.value != null && wordTreeNode.value.state != null)
                            {
                                if(x == null){
//                                    System.out.println("found!2");
                                    x = wordTreeNode.value.state;
                                }
                                
                                if (x != wordTreeNode.value.state){
//                                    System.out.println("found!3");
                                    return false;
                                }
                            
                            }
                        }
                        System.out.println("found!");
                        return isMatch(sequence);
                        
                    }
                }, searchStack, direction, word);

        
        for (WordTreeNode el : searchStack) {
            System.out.print(">> " + el.value);
        }
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
