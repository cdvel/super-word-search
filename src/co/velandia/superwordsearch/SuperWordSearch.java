package co.velandia.superwordsearch;

import co.velandia.superwordsearch.search.*;
import co.velandia.superwordsearch.structures.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author cesar
 */
public class SuperWordSearch {
    
    public enum Mode {
        
        WRAP, NO_WRAP, NOT_SET
    }

    private static LetterGrid gridWorld;
    private static Mode searchMode;
    private static List<String> searchWords;
    
    public SuperWordSearch(String filename) {
        try {
                gridWorld = new LetterGrid();
                searchMode = Mode.NOT_SET;
                searchWords = new ArrayList<>();
                loadFromFile(filename);
        } catch (IOException ex) {
            System.err.println("Error reading input file. Restart and try again");
        }
    }
    
    private void setMode(String line) {
        searchMode = (line.trim().equalsIgnoreCase("WRAP")) ? Mode.WRAP : Mode.NO_WRAP;
        gridWorld.setMode(searchMode);
    }
    
    private List<String> getSearchWords() {
        return searchWords;
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
    
    /**
     * Check stack for duplicated coordinates
     * @param searchStack
     * @return true if <tt>searchStack</tt> has duplicates, false otherwise
     */
    private boolean hasDuplicates(Stack<WordTreeNode<GridCoordinates>> searchStack) {
         
        Set set = new HashSet();
        for (WordTreeNode<GridCoordinates> node : searchStack) 
            set.add(node.value);
        
        return searchStack.size() != set.size();
    }
    
    /**
     * Check whether <tt>searchStack</tt> matches <tt>wordLength</tt> and has same direction
     * @param searchStack
     * @param wordLength
     * @return true if matches length and direction, false otherwise
     */
    private boolean isMatch(Stack<WordTreeNode<GridCoordinates>> searchStack, int wordLength) {
        
        StringBuilder out = new StringBuilder();
        GridDirection dir = null;
        
        //stack has root and initial node
        for (int i = 2; i < searchStack.size(); i++) {
            if (dir==null)
                dir = searchStack.get(i).getValue().getDirection();
             
             if(searchStack.get(i).getValue().getDirection() != dir || dir == null)
                 return false;
        }
        
        // letters can be included only once 
        return !hasDuplicates(searchStack) && wordLength+1==searchStack.size();
    }
    
    /**
     * Generates a tree structure for <tt>word</tt>
     * @param word
     * @return Tree representing nodes for each letter
     */
    private WordTreeNode generateWordTree(String word)
    {
        WordTreeNode<GridCoordinates> root = new WordTreeNode(null);
        List<List<GridCoordinates>> set = new ArrayList<>();
        
        for (char ch : word.toCharArray())
            set.add(gridWorld.getLetterCoordinates("" + ch));
        
        for (List<GridCoordinates> list : set)
            root.addToLeaves(list);
        
        return root;
    }
    
    
    public String search(String word) {

        Stack<WordTreeNode<GridCoordinates>> searchStack = new Stack();
        GridDirection direction = null;
        
        boolean result = WordTreeSearch.depthFirstSearch(generateWordTree(word),
                new GoalFunction< Stack<WordTreeNode<GridCoordinates>>>() {
                    
                    @Override
                    public boolean evaluate(Stack<WordTreeNode<GridCoordinates>> sequence) {
                        
                        //filtering out early
                        if (sequence.size() < word.length())
                            return false;
                        
                        GridDirection dir = null;
                        for (WordTreeNode<GridCoordinates> node : sequence) {
                            
                            if (node.getValue() != null && node.getValue().getDirection() != null)
                            {
                                if(dir == null)
                                    dir = node.getValue().getDirection();
                                
                                if (dir != node.getValue().getDirection())
                                    return false;
                            
                                return isMatch(sequence, word.length());
                            }
                        }
                        return isMatch(sequence, word.length());
                    }
                }, searchStack, direction, word);
        
        
        
        resetDirections(searchStack);
        
        if(result)
            return ""+searchStack.get(1)+searchStack.get(searchStack.size()-1);
        else
            return "NOT FOUND";
    }
    
    private void resetDirections(Stack<WordTreeNode<GridCoordinates>> nodes) {
        for (WordTreeNode node : nodes) 
            if (node.value!=null)
                ((GridCoordinates)node.getValue()).setDirection(null);
    }
    
    @Override
    public String toString() {
        return gridWorld.toString() + "mode: " + searchMode + ", searchWords: " + searchWords.toString()+"\n";
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        
            SuperWordSearch sws = new SuperWordSearch("src\\files\\3x3_no_wrap.txt");
            
            for (String word : sws.getSearchWords()) {
                System.out.println(sws.search(word));
            }
            
            System.out.println("");
                    
            sws = new SuperWordSearch("src\\files\\3x3_wrap.txt");
            for (String word : sws.getSearchWords()) {
                System.out.println(sws.search(word));
            }
            
            Scanner in = new Scanner(System.in);
            
            System.out.print("\nPlease type absolute path to your input file: ");
            String filePath = in.nextLine();
      
            sws = new SuperWordSearch(filePath);
            for (String word : sws.getSearchWords()) {
                System.out.println(sws.search(word));
            }

    }
}
