package phylogenetictree;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 *
 * @author Kuba
 */
public class HierarchicalClustering {

    /**
     * A simple generic class.
     */
    private class Pair<T1, T2 extends Comparable<T2>> implements Comparable<Pair<T1, T2>>{
        
        private final T1 left;
        private final T2 right;
        
        Pair(T1 a, T2 b){
            
            left = a;
            right = b;
        }
        
        Pair(Pair x){
            
            left = (T1) x.getLeft();
            right = (T2) x.getRight();
        }
        
        public T1 getLeft(){
            
            return left;
        }
        public T2 getRight(){
            
            return right;
        }
        
        @Override
        public String toString()
        {
             return "(" + left.toString() + ", " + right.toString() + ")";
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            if(right != ((Pair<T1, T2>) obj).getRight() || left != ((Pair<T1, T2>) obj).getLeft()){
                return false;
            }
            
            return true;
        }
        
        @Override
        public int hashCode(){
            
            return left.hashCode() % 10000 * right.hashCode() % 10000;
        }
        
        public boolean equals(Pair other) {
            
          return (other != null && other.getLeft() == this.left && other.getRight() == this.right);
        }

        @Override
        public int compareTo(Pair<T1, T2> o) {
            
            return o.getRight().compareTo(this.right);
        }
    }
    
    /**
     * The sequences to cluster.
     */
    HashMap<Integer, String> sequences;
    
    /**
     * The generated tree.
     */
    HashMap<Integer, Pair<Integer, Integer>> tree;
    
    /**
     * HashTable containing scores of all clusters.
     */
    public HashMap<Pair<Integer, Integer>, Double> scores;
    
    /**
     *  Structure to process clustering.
     */
    public PriorityQueue<Pair <Pair<Integer, Integer>, Double> > clustering;
    
    public void initalizeClustering(Interface i){
        
        clustering = new PriorityQueue<>();
        scores = new HashMap<>();
        tree = new HashMap<>();
        sequences = new HashMap<>(i.sequences);
        
        for(Integer j : sequences.keySet()){
            
            tree.put(j, new Pair(-1, -1));
        }
        
        for(Integer it1 : i.sequences.keySet()){
            
            for(Integer it2 : i.sequences.keySet()){
                
                if(!it1.equals(it2)){
                    
                    String a = i.sequences.get(it1);
                    String b = i.sequences.get(it2);
                    
                    SmithWaterman alignment = new SmithWaterman(a, b, i.score);
                    alignment.align();
                    clustering.add(new Pair(new Pair(it1, it2), alignment.getScore()));
                    scores.put(new Pair(it1, it2), alignment.getScore());
                    scores.put(new Pair(it2, it1), alignment.getScore());
                }
            }
        }
        
        /*while(scores.size() != 0){
            
            System.out.printf("(%d, %d) -> %f\n", clustering.element().left.left, clustering.element().left.right, clustering.element().right);
            clustering.remove();
        }*/
    }
    
    public void runClustering(){
        
        Pair<Integer, Integer> p1 = new Pair(1, 2);
        Pair<Integer, Integer> p2 = new Pair(2, 1);
        
        
        System.out.printf("%b\n", p1.equals(p1));
        System.out.printf("%d\n", p1.hashCode());
        System.out.printf("%d\n", p2.hashCode());
        
        /*System.out.printf("\nclustering:\n");
        while(clustering.size() > 0){
            
            Pair< Pair<Integer,Integer>, Double> e = clustering.remove();
            System.out.printf("(%d, %d) -> %f\n", e.left.left, e.left.right, e.right);
        }
        System.out.println();*/
        
        while(clustering.size() > 4){
            
            Pair< Pair<Integer,Integer>, Double> e = clustering.remove();
            clustering.remove();
            
            
            for(Integer i : sequences.keySet()){
                
                if(!i.equals(e.left.left) && !i.equals(e.left.right)){
                
                    System.out.printf("\nclustering:\n");
                    System.out.print(clustering);
                    System.out.printf("\nmax: (%d, %d) -> %f\n", e.left.left, e.left.right, e.right);
                    
                    System.out.printf("licze srednia z: (%d, %d) i (%d, %d)", e.left.left, i, e.left.right, i);
                    double tmp = 0.5*(scores.get(new Pair(e.left.left, i)) + scores.get(new Pair(e.left.right, i)));
                    clustering.add(new Pair(new Pair(i, tree.size()), tmp));
                    clustering.add(new Pair(new Pair(tree.size(), i), tmp));
                    scores.put(new Pair(i, tree.size()), tmp);
                    scores.put(new Pair(tree.size(), i), tmp);
                    clustering.remove(new Pair(i, e.left.left));
                    clustering.remove(new Pair(i, e.left.right));
                    clustering.remove(new Pair(e.left.left, i));
                    clustering.remove(new Pair( e.left.right, i));
                }
            }
            tree.put(tree.size(), e.left);
        }
    }
}
    
