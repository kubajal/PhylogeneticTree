package phylogenetictree;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class. Handles input and calculates hierarchcial clustering.
 * 
 * Calling convention: PhylogeneticTree sequences.fasta matrix.txt
 *  where:
 *   sequences.fasta - sequences to cluster,
 *   matrix.txt - cost matrix of indels/mismatches to the alphabet that sequences use.
 * 
 * Computes clusters and phylogenetic tree.
 * 
 * First, initial (given in the .fasta file) sequences are aligned pairwise and their scores are inserted into the pool.
 * Second, the pair with the best score is chosen and merged to a virtual sequence. All pairs scores are recomputed with the two merged together.
 * Third, the algorithm is repeated until there are only 2 (virtual) sequences left in the pool, which are then merged to the last cluster.
 * 
 * @author Kuba Jalowiec
 */
public class PhylogeneticTree {
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        if(args.length != 2){
            System.out.printf("Wrong call format.\n");
            return;
        }
        
        Interface in = new Interface();
        try {
            in.getSequences(args[0]);
            in.getMatrix(args[1]);
        } catch (IOException ex) {
            Logger.getLogger(PhylogeneticTree.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        HierarchicalClustering hc = new HierarchicalClustering(in.sequences, in.weights);
        hc.initalizeClustering();
        hc.runClustering();
        hc.printTree(hc.getTree().size());
    }
}
