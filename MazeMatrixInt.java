/**
 * @author: Kevin Broberg
 *
 * MazeMatrixInt is a wrapper around a two-dimensional integer matrix
 * to provide support for depth- or breadth-first traversal of a maze.
 * It supports printing a colorful version of paths through the maze
 * (expecting most matrix values to be fairly small -- corresponding
 * to a cluster computer's rank, for example)
 */
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import edu.rit.color.HSB;
import edu.rit.image.PJGColorImage;
import edu.rit.util.Range;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
public class MazeMatrixInt implements Traversable {
    public static final int WALL = 0;
    public static final int MAZE_PATH = 1;
    public static final int WALLCOLOR = HSB.pack( 0.0f, 0.0f, 0.0f);
    public static final int PATHCOLOR = HSB.pack( 0.5f, 1.0f, 1.0f);
    public static final float MAGICNUMBER = 31/36f;
    public static final String IMGEXTENSION = ".PNG";
    public static final String IMGTYPE = "PNG";
    public static final String DEFAULTOUTFILE = "default";
    private int[][] matrix;
    private String outfile;
    private int height, width;
    // colorful mode translates whatever random numbers you stuff
    // into the matrix into distinct colors
    private boolean colorful;
    private TreeMap<Integer, Integer> visits;
    private int hueindex;

    /**
     * Makes a new maze with the given matrix (namely, points at the matrix)
     * and guesses at height/width params
     */
    public MazeMatrixInt( int[][] themat ) {
        this( themat, DEFAULTOUTFILE, themat.length, themat[0].length );
    }
    
    /**
     * Makes a new maze with the given params and no outfile
     */
    public MazeMatrixInt( int[][] themat, int height, int width ) {
        this( themat, DEFAULTOUTFILE, height, width );
    }

    /**
     * Makes a maze with given matrix and outfile, guesses at height/width
     */
    public MazeMatrixInt( int[][] themat, String outfile ) {
        this( themat, outfile, themat.length, themat[0].length  );
    }

    /**
     * Makes a maze with given args
     */
    public MazeMatrixInt( int[][] themat, String outfile, 
                          int height, int width ) {
        matrix = themat;
        this.outfile = outfile;
        this.height = height;
        this.width = width;
        colorful = false; 
        visits = new TreeMap<Integer, Integer>();
        visits.put( WALL, WALLCOLOR );
        visits.put( MAZE_PATH, PATHCOLOR );
    }

    /**
     * Changes small ints in the matrix to numbers for bright colors.
     * Doesn't do so if the matrix has already been processed
     *
     * @return if the matrix was already colored
     */
    public boolean colorMatrix() {
        if( !colorful ) {
            return colorMatrix( true );
        }
        return true;
    }

    /**
     * Changes small ints in the matrix to numbers for bright colors regardless
     * of whether the matrix has already been colored
     *
     * @return if the matrix was already colored (it'll be recolored anyway)
     */
    public boolean colorMatrix( boolean force ) {
        // visit every point in the maze
        for( int i = 0; i < height; ++i ) {
            for( int j = 0; j < width; ++j ) {
                matrix[i][j] = hues(matrix[i][j]);
            }
        }    
        boolean ret = colorful;
        colorful = true;
        return ret;
    }    

    /**
     * Prints this maze into the file it was given at construction,
     * or the default output file
     *
     * @returns if the print job was successful
     */
    public boolean printImage() throws IOException {
        if( outfile == null ) this.outfile = DEFAULTOUTFILE;
        return printImage( this.outfile );
    }

    /**
     * Prints this maze into "<outfile>.PNG"
     * This method also colors the matrix if it isn't already colored
     * 
     * @returns false if ImageIO couldn't find an appropriate writer
     */
    public boolean printImage( String outfile ) throws IOException {
        colorMatrix();
        PJGColorImage pjimg = new PJGColorImage( height, width, matrix );
        File fi = new File( outfile + IMGEXTENSION );
        boolean ret = ImageIO.write( pjimg.getBufferedImage(),
                                     IMGTYPE, fi );
        return ret;
    }

    /**
     * Returns true if this coordinate points at a wall
     */
    public boolean isPath( Pair coord ) {
        int w = colorful ? hues(MAZE_PATH) : MAZE_PATH;
        if( get(coord) == w ) {
            process( coord, coord, 5 );
            return true;
        } return false;
    }

    /**
     * Returns true if both coordinates of the given 
     */
    public boolean inBounds (Pair coord) {
        int x = coord.getX(); int y = coord.getY();
        return (x >= 0 && y >= 0 && x < this.width && y < this.height);
    }

    /**
     * Places the given value into the matrix (translating it to a color
     * if the matrix has been colored)
     *
     * @returns the value in the matrix
     */
    public int put( Pair coord, int value ) {
        if( colorful ) { 
            value = hues( value );
        }
        int x = coord.getX(); 
        int y = coord.getY();
        int ret = matrix[x][y];
        matrix[x][y] = value;
        return matrix[x][y];
    }

    /**
     * For silly interface reasons, we need this method in any traversable object,
	 * as it is a more general version of put( Pair, int ). On MazeMatrixInts,
	 * it process( start, end, value ) is exactly the same as put(end,value) -
	 * in other words, the first arg is utterly ignored
	 *
	 * @arg start  ignored
	 * @arg end    the point in the matrix to color
	 * @arg value  the number to color the spot (subject to coloration by MMI)
     */
    public void process( Pair start, Pair end, int value ) {
        if( colorful ) { 
            value = hues( value );
        }
        //System.out.println( "Processing " + end + " value : " + value);
        /*
        int smallx = Math.min( start.getX(), end.getX() ),
            bigx = Math.max( start.getX(), end.getX() ),
            smally = Math.min( start.getY(), end.getY()),
            bigy = Math.max( start.getY(), end.getY());
        for( int i = smallx; i <= bigx; ++i ) {
            for( int j = smally; j <= bigy; ++j ) {
                matrix[i][j] = value;
            }
            }*/
        matrix[end.getX()][end.getY()] = value;
    }
            

    /**
     * Simple accessor function for the matrix. Doesn't do any correcting
     * for colorful mode
     */
    public int get( Pair coord ) { 
        return matrix[ coord.getX() ][ coord.getY() ];
    } //get

    // could return whatever kind of variable-length container
    public ArrayList<Pair> getNeighbors( Pair coord ) {
        ArrayList<Pair> ret = new ArrayList<Pair>();
        int x = coord.getX();
        int y = coord.getY();
        Pair[] cand = { new Pair(x-1,y),
                        new Pair(x+1,y),
                        new Pair(x,y-1),
                        new Pair(x,y+1)};
        for( Pair p : cand ) {
            if( this.inBounds(p) && this.isPath( p ) ) {
                ret.add( p );
            }
        }
        return ret;
    } // neighbors

	
    // could return whatever kind of variable-length container
    public ArrayList<Pair> getNeighbors( Pair coord, Range rowrange, Range colrange ) {
        ArrayList<Pair> ret = new ArrayList<Pair>();
        int x = coord.getX();
        int y = coord.getY();
        Pair[] cand = { new Pair(x-1,y),
                        new Pair(x+1,y),
                        new Pair(x,y-1),
                        new Pair(x,y+1)};
        for( Pair p : cand ) {
		/*
        int x = coord.getX(); int y = coord.getY();
        return (x >= 0 && y >= 0 && x < this.width && y < this.height);
		*/
			int xval = p.getX();
			int yval = p.getY();
			if( xval > rowrange.lb() && xval <= rowrange.ub() &&
			    yval > colrange.lb() && xval <= colrange.ub() &&
			    this.isPath( p ) ) 
			{
                ret.add( p );
            }
        }
        return ret;
    } // neighbors
	
    /**
     * Manages pretty colors using a TreeMap
     */
    private int hues( int me ) {
        if( visits.containsKey( me ) ) {
            return visits.get(me);
        } else {
            float floater = hueindex++ * MAGICNUMBER;
            floater -= Math.floor( floater );
            int color = HSB.pack( floater, 1.0f, 1.0f );
            visits.put( me, color );
            return color;
        }       
    }
}//class
