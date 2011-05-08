public interface Traversable {
    /**
     * Return a list of legal nodes 
     */
    public java.util.ArrayList<Pair> getNeighbors( Pair whoever );
    /**
     * Process the edge between start and end, based on value
     */
    public void process( Pair start, Pair end, int value );
}