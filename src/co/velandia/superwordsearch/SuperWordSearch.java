
package co.velandia.superwordsearch;

import co.velandia.superwordsearch.LetterGrid.Directions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author cesar
 */


public class SuperWordSearch {

    private void resetStates(List<GridCoordinates> coordinates) {
        for (GridCoordinates coordinatePair : coordinates)
            coordinatePair.setEmpty();
    }

    private void setMode(String line) {
        searchMode = (line.trim().equalsIgnoreCase("WRAP"))? Mode.WRAP: Mode.NO_WRAP;
        gridWorld.setMode(searchMode);
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
        return gridWorld.toString()+"mode: "+ searchMode + ", searchWords: "+ searchWords.toString();
    }
    
    
    public void loadFromFile(String filePath) throws IOException {
        
        List<String> lines=Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"));
        

        int linesRead = 0;
        int pWords = 0;
        
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            for(String line; (line = br.readLine()) != null;) {
                
                if (gridWorld.isEmpty()){
                    String[] dimensions  = line.split(" ");
                    gridWorld.setRowCount(Integer.parseInt(dimensions[0]));
                    gridWorld.setColumnCount(Integer.parseInt(dimensions[1]));
                    
                }else{
                    if (linesRead < gridWorld.getRowCount())
                    {
                        char [] letters = line.toCharArray();
                        gridWorld.setRow(letters, linesRead, gridWorld.getColumnCount());
                        linesRead++;
                    }else{
                        if (searchMode == Mode.NOT_SET)
                            setMode(line); 
                        else{

                            if (pWords == 0){
                                pWords = Integer.parseInt(line);
                            }
                            else{
                                searchWords.add(line);
                            }
                        }
                    }
                }
                
                
                // process the line.
            }
            // line is not visible here.
        }
    }

    
    public void printLetterCoordinates(String letter){
        System.out.println(gridWorld.getLetterCoordinates(letter)); 
    }
    
   
    public void exploreWordSets(){
        
        for (String word : searchWords) {
            System.out.println("\n****searching: "+word);
            List<List<GridCoordinates>> letterSet = new ArrayList<>();
            
            for (char letter : word.toCharArray())
            {
                letterSet.add(gridWorld.getLetterCoordinates(""+letter));
            }

            List<GridCoordinates> streak = new ArrayList<>();
            Directions streakDir=null;
            GridCoordinates curr, prev; 
            
            for (List<GridCoordinates> set: letterSet){
                
                if (streak.isEmpty()){
                    streak.add(set.get(0));
                    set.get(0).setOccupied();
                }
                else{
                    prev = streak.get(streak.size()-1);
                    curr = set.get(0);
                    //System.out.println("state curr?"+curr.state);
//                    if (curr.isOccupied())
//                        break;
//                    else
                    
                    if (!prev.equals(curr)){
                        
                        //System.out.println("comparing= "+prev +" and "+curr );
                        Directions evalDir = gridWorld.compareCoordinates(prev, curr);
                        if (streakDir == null)
                            streakDir = evalDir;
                        
                        if (streakDir != null && streakDir == evalDir && !curr.isOccupied()){
                            //mark as occupied
                            
                            //System.out.println("NOT null= : "+streakDir);
                        }else
                        {
                            //NOTE: means streak failed, rollback positions, stop search
                            //System.out.println("Is null");
                            resetStates(streak);
                            break;
                        }
                        curr.setOccupied();
                        streak.add(curr);
                    }    
                }
            }
            
            if (streak.size() == word.length()){
                System.out.println(streak.get(0)+" "+streak.get(streak.size()-1));
                resetStates(streak);
            }
            else
            System.out.println("NOT FOUND");
            //System.out.println("==> "+streak);
        }

    }
   
    public void print(){
        System.out.println(this);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Please type in your input file...");
        
        SuperWordSearch sws = new SuperWordSearch();
        try {
            sws.loadFromFile("C:\\sws\\3x3_no_wrap.txt");
            sws.print();
            sws.exploreWordSets();
            //sws.print();

            
        } catch (IOException ex) {
            Logger.getLogger(SuperWordSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
   
    
    
}
