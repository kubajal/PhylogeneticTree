package phylogenetictree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that handles all input, i. e. reads the given sequences from the fasta file and weights from the matrix file.
 * @author Kuba Jalowiec
 */
public class Interface {
    
    /**
     * The alphabet of the weights matrix.
     */
    
    public String alphabet = null;
    
    /**
     * The collection contatining sequences to cluster.
     * Integer: index of the sequence
     * String: the sequence
     */
    HashMap<Integer, String> sequences;
    
    /**
     * A HashMap representing the weights matrix.
     * To get the generic value weights[a][b] (where a and b are characters that we compare) we do a lookup in the outer HashMap (aka weights) with the key of a. In return we get the HashMap which represents the weight verctor for the character a in which we do a second lookup to find weight.
     */
    public HashMap<Character, HashMap<Character, Double>> weights;
    
    /**
     * Parses the given .fast file. Initializes first and second String objects that represent DNA sequences to align.
     * @param fileName the name of the file in FASTA format containing two sequences to align
     * @throws IOException if error occured while reading the file
     */
    
    public void getSequences(String fileName) throws IOException{
        BufferedReader br = null;
        InputStream in = null;
        InputStreamReader s = null;
        String text = null;
        
        in = new FileInputStream(fileName);
        s = new InputStreamReader(in);
        br = new BufferedReader(s);
        StringBuilder sb;
        String line = null;
        sequences = new HashMap<>();
        
        /* ------ parse the .fasta file ------ */
        
        int counter = 0;
        
        line = br.readLine();
        
        while(line != null){
            
            sb = new StringBuilder();
            
            while(line.charAt(0) == '>'){
                
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            
            System.out.printf("FASTA description of the sequence no. %d: \n%s", ++counter, sb.toString());
            
            sb = new StringBuilder().append(line);

            while((line = br.readLine()) != null && line.charAt(0) != '>'){

                sb = sb.append(line);
            }
            
            System.out.printf("sequence no. %d: %s\n\n", counter, sb.toString());
            
            sequences.put(counter, sb.toString());
        }
        
        /* ------++++++++++++++++++++++++++++++++------ */
    }
    
    /**
     * Reads the weights matrix {@link phylogenetictree.Interface#weights} with the values given in the matrix.txt file.
     * @param fileName the name of the file containing weights matrix. Its format should be as follows:
 1) optional: a few first lines starting with # are comments,
 2) first non-comment line consists the alphabet (letters separated by blank spaces)
 3) further non-comment lines start with a letter followed by |sigma| integers, describing the line in the weights matrix corresponding to the given letter.
     * @throws IOException if error occured while reading the file
     */
    
    public void getMatrix(String fileName) throws IOException{
        
        BufferedReader br = null;
        InputStream in = null;
        InputStreamReader s = null;
        String text = null;
        
        try {
            in = new FileInputStream(fileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new InputStreamReader(in);
        br = new BufferedReader(s);
        String line = null;
        
        while((line = br.readLine()) != null && line.charAt(0) == '#'); // comment lines
        
        alphabet = line.replaceAll("\\s", "");  // ereases all whitespaces
        
        int c;
        weights = new HashMap<>();
        for(int i = 0; i < alphabet.length(); i++){
            weights.put(alphabet.charAt(i), new HashMap<>());
        }
        
        for(int i = 0; i < alphabet.length(); i++){
            
            c = br.read();
            if(c != alphabet.charAt(i))
                throw new IOException("Wrong matrix.txt format.\n");    // simple validation of matrix.txt: first letter of this row doesnt correspond to the expected letter
            
            line = br.readLine();
            line = line.substring(1);
            
            List<String> weights = new ArrayList(Arrays.asList(line.split("\\s")));
            weights.removeAll(Arrays.asList("", null));
            int j = 0;
            
            for(String it : weights){
                
                this.weights.get(alphabet.charAt(j)).put((char) c, Double.parseDouble(it));
                j++;
            }
        }
        return;
        
    }
    
    public String getAlphabet(){
        
        return alphabet;
    }
}
