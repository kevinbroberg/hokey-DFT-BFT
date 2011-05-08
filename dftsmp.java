/*******************************************************************************
 *
 * Parallel Depth-First Traversal: SMP ver.
 * 
 * I'm a pretty kitty, meow meow meow
 *
 * @author Kevin Broberg
 * @author Harold Mendoza
 * @author C Oliver Wing
 *
 * @date May 7, 2011
 *
 ******************************************************************************/


import edu.rit.pj.Comm;

import edu.rit.pj.BarrierAction;
import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;

import edu.rit.pj.reduction.SharedInteger;


import edu.rit.util.Random;


import java.util.Collections;
import java.util.List;
import java.util.LinkedList;


public class dftsmp {
    public static final int TRAVERSED = 2;

    public static Traversable graph;
    
    // Communication between threads.
    static SharedInteger working;
    static LinkedList<Pair> commonStack;
    
    // Privatize constructor.
    private dftsmp() {}
    
    public static void traverse( int color ) {
        while( !commonStack.isEmpty() ) {
            Pair w = commonStack.removeLast();
	    System.out.println(w);
            for( Pair neighbor : graph.getNeighbors(w) ) {
		int mdata = graph.get(neighbor);
		if (mdata == MazeMatrixInt.WALL ||
		    mdata == MazeMatrixInt.MAZE_PATH) { // i.e., unvisited
		    graph.process( w, neighbor, color );				
		    commonStack.addLast(neighbor);
		}
            }
        }       
    }

    public static void main( String[] args ) throws Exception {
	Comm.init(args);

        if (args.length < 1) args = new String[] {"chicken"};
        int matsize = 100;
        int[][] mat = new int[matsize][matsize];
        for( int i = 0 ; i < matsize; ++i ) {
            for( int j = 0 ; j < matsize; ++j ) {
                mat[i][j] = 0;
            }
        }

		// Generate the maze (not parallelized).
        MazeGenerator mgen = new MazeGenerator( mat, matsize );
	mgen.initializeMatrix();

        MazeMatrixInt tmaze = new MazeMatrixInt( mgen.getMatrix(),
                                                  args[0], matsize,
                                                  matsize );


        tmaze.printImage();

	graph = tmaze;
	
	// Begin traversing.
	commonStack = new LinkedList<Pair>();
	commonStack.addLast( new Pair(0,0) );

	working = new SharedInteger();
	
	// Parallelize the DFT.
	new ParallelTeam().execute (new ParallelRegion()
	    {
		public void run() throws Exception
		{		    
		    int w = 0;
		    do {
			// Jump in and help!
			working.incrementAndGet();
			
			dftsmp.traverse( dftsmp.TRAVERSED );
			
			// Didn't see any more work.
			w = working.decrementAndGet();		
		    } while (w != 0);  // Somebody still working?
		}
	    });
	
        System.out.println( "SWEET MOTHER OF PEARL" );
        tmaze.printImage( "mopo" );

    }


    // smp parallel
    /*
      Traversable d = new whatever();
      new ParallelTeam.execute( new ParallelRegion() {
      Stack big;
      boolean needwork;

      public void start() {
      big = new LinkedList();
      needwork = false;
      getwork = new ParallelSection() { public void run(){
          if( !big.isEmpty() ) {
              mystack = big.removeLast()
          }
      }};
      putwork = new ParallelSection() { public void run() {
          big.addFirst( mystack.removeLast() );
      }};

      public void run() {
      mystack = new Stack();
      while( !big.isEmpty() ) {
        while( !mystack.isEmpty() ) {
          Pair w = stack.pop();
          for( Pair neighbor : dicking.getNeighbors(w) ) {
              dicking.process( w, neighbor, 1 );
              mystack.push(neighbor);
          }
          if( needwork[k-1] || needwork[k+1] ) {
              critical( putwork );
          }
        }
        needwork[i] = true;
        // sleep for a little while
        critical( getwork );
      }
    */
}

    