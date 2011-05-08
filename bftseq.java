/**
 * It's a doppelganger of dftseq! Only it uses a queue
 */
import java.util.*;
import edu.rit.util.Range;
public class bftseq {
	private Traversable graph;
	public bftseq( Traversable t ) {
		this.graph = t;
	}
	
	public void traverse( Pair source, int color ) {
		traverse(source, color, new Range(0,t.getSize()), new Range(0,t.getSize()));
	/*
		LinkedList< Pair > queue = new LinkedList<Pair>();
		queue.addLast( source );
		while( !queue.isEmpty() ) {
			Pair w = queue.removeFirst();
			for( Pair neighbor : graph.getNeighbors(w)) {
               graph.process( w, neighbor, color );
			   queue.addLast(neighbor);
			}
		}
	*/
	}
	
	public void traverse( Pair source, int color, Range rowrange, Range colrange ) {
		LinkedList< Pair > queue = new LinkedList<Pair>();
		queue.addLast( source );
		while( !queue.isEmpty() ){
			Pair w = queue.removeFirst();
			for( Pair neighbor : graph.getNeighbors(w, rowrange, colrange)) {
               graph.process( w, neighbor, color );
			   queue.addLast(neighbor);
			}
		}
	}
	
	public void rainbowTraverse( Pair source, int color ) {
		LinkedList< Pair > queue = new LinkedList<Pair>();
		queue.addLast( source );
		while( !queue.isEmpty() ){
			Pair w = queue.removeFirst();
			for( Pair neighbor : graph.getNeighbors(w)){
				graph.process( w, neighbor, color );
				queue.addLast(neighbor);
				++color;
			}
		}
	}
	
	public static void main( String[] args ) throws Exception{
		int size = 101;
		if( args.length > 0 ) {
			size = Integer.parseInt(args[0]);
		}
		MazeGenerator gen = new MazeGenerator(size);
		gen.initializeMatrix();
		MazeMatrixInt bluejay = new MazeMatrixInt( gen.getMatrix(), "bluejay" );
		bftseq veejay = new bftseq(bluejay);
		veejay.rainbowTraverse( new Pair(0,0), 1 );
		bluejay.printImage();
	}
}
