package phylogenetictree;

import java.util.HashMap;

/**
 * Represents abstract solver for the global alignment problem using Smith-Waterman algorithm.
 * @author Kuba Jalowiec
 */
public class SmithWaterman {
    
    /**
     * first string to align
     */
    private String first;
    /**
     * second string to align
     */
    private String second;
    /**
     * table of weights for each replacement
     */
    private HashMap<Character, HashMap<Character, Double>> weightMatrix;
    /**
     * table of output scores of dynamic programming
     */
    private double[][] dynProg;
    /**
     * i coordinate of the cell which gave the local optimal weightMatrix
     */
    private int iPredecessor[][];
    /**
     * j coordinate of the cell which gave the local optimal weightMatrix
     */
    private int jPredecessor[][];
    /**
     * StringBuilder that generates output alignment of the first string
     */
    private StringBuilder firstAligned = new StringBuilder();
    /**
     * StringBuilder that generates output alignment of the second string
     */
    private StringBuilder secondAligned = new StringBuilder();
    /**
     * StringBuilder that generates symbols at the corresponding indexes of both strings of miss ('.'), insertion or deletion (' ') and match ('|')
     */
    private StringBuilder alignment  = new StringBuilder();
    
    private int iBestAlignment;
    private int jBestAlignment;
    
    /**
     * A lookup to the HashMap weightMatrix {@link globalalignment.SmithWaterman#weightMatrix}
     * @param a letter to compare with b
     * @param b letter to compare with a
     * @return weight of matching a and b
     */
    
    private Double score(char a, char b){
        
        return weightMatrix.get(a).get(b);
    }
    
    /**
     * Constructs a new instance of local alignment solver that uses Smith-Waterman algorithm.
     * @param _first first string to align
     * @param _second second string to align
     * @param _weightMatrix matrix of costs (aka scores, weights) of mismatches and matches
     */
    
    SmithWaterman(String _first, String _second, HashMap<Character, HashMap<Character, Double>> _weightMatrix){
        
        dynProg = new double[_first.length() + 1][_second.length() + 1];
        iPredecessor = new int[_first.length() + 1][_second.length() + 1];
        jPredecessor = new int[_first.length() + 1][_second.length() + 1];
        first = _first;
        second = _second;
        weightMatrix = new HashMap<Character, HashMap<Character, Double>>(_weightMatrix);
    }
    
    /**
     * Runs the Smith-Waterman algorithm. Finds the best global alignment.
     */
    
    public void align(){
        
        
        for(int i = 1; i < first.length() + 1; i++){
            
            dynProg[i][0] = dynProg[i - 1][0] + score(first.charAt(i-1), '*'); 
        }
        
        for(int j = 1; j < second.length() + 1; j++){

            dynProg[0][j] = dynProg[0][j - 1] + score(second.charAt(j-1), '*'); 
        }  
        for(int i = 1; i < first.length() + 1; i++){
            
            for(int j = 1; j < second.length() + 1; j++){
                
                dynProg[i][j] = dynProg[i-1][j-1] + score(first.charAt(i-1), second.charAt(j-1)); // let us suppose, that it is optimal to match letters from both strings as it is
                iPredecessor[i][j] = i - 1;
                jPredecessor[i][j] = j - 1;
                
                if(dynProg[i][j] < dynProg[i][j-1] + score(second.charAt(j-1), '*')){ // check if it is optimal to shift the first string (put a blank into it)

                    dynProg[i][j] = dynProg[i][j-1] + score(second.charAt(j-1), '*');
                    iPredecessor[i][j] = i;
                    jPredecessor[i][j] = j - 1;
                }
                if(dynProg[i][j] < dynProg[i-1][j] + score(first.charAt(i-1), '*')){ // check if it is optimal to shift the second string (put a blank into it)
                    
                    dynProg[i][j] = dynProg[i-1][j] + score(first.charAt(i-1), '*');
                    iPredecessor[i][j] = i - 1;
                    jPredecessor[i][j] = j;
                }
            }
        }
    }
    
    /**
     * Returns the minimal cost of global alignment. Call only after align() (otherwise returns 0).
     * @return optimal global alignemnt with respect to the given weight matrix
     */
    
    public Double getScore(){
        
        return dynProg[first.length()][second.length()];
    }
    
    /**
     * Returns String containing computed alignment of the first string.
     * @return alignment of the first string
     */
    
    public String getFirstAligned(){
        
        return firstAligned.reverse().toString();
    }
    
    /**
     * Returns String containing computed alignment of the second string.
     * @return alignment of the second string
     */
    public String getSecondAligned(){
        
        return secondAligned.reverse().toString();
    }
    
    /**
     * Returns String representing matches ('|')., mismatches ('.'). and insertions/deletions (' ').
     * @return String representing relation between two aligned strings
     */
    
    public String getAlignment(){
        
        return alignment.reverse().toString();
    }
}
