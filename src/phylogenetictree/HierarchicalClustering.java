package phylogenetictree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Class that handles all computation. Initializes all structuers and then computes the clustering and the phylogenetic tree.
 * @author Kuba Jalowiec
 */
public class HierarchicalClustering {
    
    HierarchicalClustering(HashMap<Integer, String> _sequences, HashMap<Character, HashMap<Character, Double>> _weights){
        
        weights = new HashMap<>(_weights);
        sequences = new HashMap<>(_sequences);
    }
    
    /**
     * Structure contatining the given cost matrix.s
     */
    HashMap<Character, HashMap<Character, Double>> weights;
    
    /**
     * Structure containing all the given sequences.
     */
    HashMap<Integer, String> sequences;

    /**
     * The indicies of sequences (possibly virtual) that are currently being processed.
     */
    private ArrayList<Integer> activeNodes; // nodes that are currently being processed
    
    /**
     * The generated phylogenetic tree.
     */
    private HashMap<Integer, Pair<Integer, Integer>> tree;  // binary tree: (parent -> (left child, right child)), children of leaves are -1
    
    /**
     * HashTable containing all possible scores.
     */
    private HashMap<Pair<Integer, Integer>, Double> scores; // (left, right) -> weights(left,right)
    
    /**
     *  Structure to process activeNodes.
     */
    private PriorityQueue<Pair <Pair<Integer, Integer>, Double> > activeNodeScores; // at the top is the pair of the closest sequences
    
    /**
     * Initializes all structures needed for computation.
     */
    public void initalizeClustering(){
        
        activeNodeScores = new PriorityQueue<>();   // at the top of this structure is the best global alignment
        scores = new HashMap<>();   // constant time lookup for the already computed scores
        tree = new HashMap<>(); // the phylogenetic tree
        activeNodes = new ArrayList<>();    // nodes the are being aligned in activeNodeScores
        
        for(Integer j : sequences.keySet()){
            
            tree.put(j, new Pair(-1, -1));  // initializing trivial clusters containing only the plain sequences (they dont habe any children)
        }
        
        int nr = 0;
        
        for(Integer it1 : sequences.keySet()){
            
            activeNodes.add(it1);   // adding all sequences from the fasta file to be processed
            
            for(Integer it2 : sequences.keySet()){
                
                if(!it1.equals(it2)){
                    
                    String a = sequences.get(it1);
                    String b = sequences.get(it2);
                    
                    SmithWaterman alignment = new SmithWaterman(a, b, weights); // global alignemnt of the given sequences
                    alignment.align();
                    activeNodeScores.add(new Pair(new Pair(it1, it2), alignment.getScore()));   // inserting the computed weights to be processed
                    scores.put(new Pair(it1, it2), alignment.getScore());   // inserting the computed weights to the lookup table
                }
            }
        }
        
    }
    
    /**
     * left and right are merged to a virtual sequence.
     * @param left index to the first node being clusterd with the second node. They both make a virtual node.
     * @param right index to the second node being clustered with the first node. They both make a virtual node.
     */
    
    private void cluster(Integer left, Integer right){
        
            for(Integer i : activeNodes){
                
                if(!i.equals(left) && !i.equals(right)){    
                
                    double newScore = 0.5*(scores.get(new Pair(left, i)) + scores.get(new Pair(right, i)));
                    scores.put(new Pair(i, tree.size() + 1), newScore);  // insert virutal node with the new weights between it and i
                    scores.put(new Pair(tree.size() + 1, i), newScore);
                    
                    activeNodeScores.add(new Pair(new Pair(i, tree.size() + 1), newScore));
                    activeNodeScores.add(new Pair(new Pair(tree.size() + 1, i), newScore));

                    activeNodeScores.remove(new Pair(new Pair(i, left), scores.get(new Pair(i, left))));    // removes old (i, left) node
                    activeNodeScores.remove(new Pair(new Pair(left, i), scores.get(new Pair(left, i))));    // removes old (left, i) node
                    activeNodeScores.remove(new Pair(new Pair(i, right), scores.get(new Pair(i, right))));    // removes old (i, right) node
                    activeNodeScores.remove(new Pair(new Pair(right, i), scores.get(new Pair(right, i))));    // removes old (right, i) node
                }
            }
            activeNodes.add(tree.size() + 1);
            tree.put(tree.size() + 1, new Pair(left, right));
            activeNodes.remove(left);
            activeNodes.remove(right);
    }
    
    /**
     * Computes the phylogenetic tree.
     */
    public void runClustering(){
       
        while(activeNodes.size() > 2){
            
            Pair< Pair<Integer,Integer>, Double> e = activeNodeScores.remove(); // removes (a, b) -> weights of a and b
            activeNodeScores.remove(); // removes (b, a) -> weights of a and b
            Integer left = e.getLeft().getLeft(), right = e.getLeft().getRight();
            
            cluster(left, right);
        }
    }
    
    public HashMap<Integer, Pair<Integer, Integer>> getTree(){
        
        return tree;
    }
    
    String printTree(Integer index) {
        
        if(tree.get(index).getLeft() == -1 && tree.get(index).getRight() == -1){
            return index.toString();
        }
        String left = printTree(tree.get(index).getLeft());
        String right = printTree(tree.get(index).getRight());
        
        System.out.printf("(%s,%s)", left, right);
        
        return left+right;
    }
}