package phylogenetictree;

/**
 *
 * @author Kuba Jalowiec
 */
/**
 * A simple generic class used in data structes. Implements a custom comparator, that is crucial for the data structures (order with respect to the second coordinate).
 */
public class Pair<T1, T2 extends Comparable<T2>> implements Comparable<Pair<T1, T2>>{

    private T1 left;
    private T2 right;

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
         return "(" + left.toString() + " -> " + right.toString() + ")";
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

        return ((Pair<T1, T2>) obj).getRight().equals(this.right) && ((Pair<T1, T2>) obj).getLeft().equals(this.left);
    }

    @Override
    public int hashCode(){

        return left.hashCode() % 10000 * right.hashCode() % 10000;
    }

    public boolean equals(Pair other) {

      return (other != null && other.getLeft() == this.left && other.getRight() == this.right);
    }
    
    /**
     * Elements are compared with respect to the {@link phylogenetictree.Pair#right} value.
     * @param o Pair to compare to.
     * @return returns if this is bigger than o.
     */

    @Override
    public int compareTo(Pair<T1, T2> o) {

        return o.getRight().compareTo(this.right);
    }
}
