import java.util.LinkedList;
public class dftseq {

    private Traversable graph;

    public dftseq( Traversable t ) {
        this.graph = t;
    }
	
    public void traverse( Pair source, int color ) {
		traverse( source, color, new Range(0,t.getSize()), new Range(0,t.getSize()));
		/*
        LinkedList< Pair > stack = new LinkedList<Pair>();
        stack.add( source );
        while( !stack.isEmpty() ) {
            Pair w = stack.removeLast();
            for( Pair neighbor : graph.getNeighbors(w) ) {
                graph.process( w, neighbor, color );
                stack.addLast(neighbor);
            }
        }            
		*/
    }

    public void traverse( Pair source, int color, Range rowrange, Range colrange ) {
        LinkedList< Pair > stack = new LinkedList<Pair>();
        stack.add( source );
        while( !stack.isEmpty() ) {
            Pair w = stack.removeLast();
            for( Pair neighbor : graph.getNeighbors(w, rowrange, colrange) ) {
                graph.process( w, neighbor, color );
                stack.addLast(neighbor);
            }
        }            
    }

	
    public static void main( String[] args ) throws Exception {
        if (args.length < 1 ) args = new String[] {"chicken"};
        int matsize = 100;
        int[][] mat = new int[matsize][matsize];
        for( int i = 0 ; i < matsize; ++i ) {
            for( int j = 0 ; j < matsize; ++j ) {
                mat[i][j] = 0;
            }
        }
        MazeGenerator dicking = new MazeGenerator( mat, matsize );
        dftseq meatshot = new dftseq( dicking );
        meatshot.traverse( new Pair(0,0), MazeMatrixInt.MAZE_PATH );
        MazeMatrixInt hotdog = new MazeMatrixInt( dicking.getMatrix(),
                                                  args[0], matsize,
                                                  matsize );
        hotdog.printImage();
        dftseq garbageplate = new dftseq( hotdog );
        System.out.println( "SHITDICK");
        garbageplate.traverse( new Pair( 0, 0 ), 2 );
        hotdog.printImage( "shitdick" );

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

    
