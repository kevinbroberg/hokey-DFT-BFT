/**
 * @author: Harold Mendoza
 *
 * MazeGenerator is  a two-dimensional integer matrix generrator
 * to provide support for depth- or breadth-first traversal.
 *
 */
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.io.*;
import edu.rit.color.HSB;
import edu.rit.image.PJGColorImage;
import edu.rit.util.Range;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.*;

public class MazeGenerator implements Traversable
{
     
    private int[][] matrix;
    private int matrixSize;
    public static Random gen;
  
	/**
	 * Generates a square matrix of the given size; all walls
	 */
    public MazeGenerator( int size ) {
		this( new int[size][size], size );
		for( int i = 0 ; i < size; ++i ) {
			for( int j = 0 ; j < size; ++j ) {
				matrix[i][j] = MazeMatrixInt.WALL;
			}
		}
		
	}
    /**
     * Constructor
     */
    public MazeGenerator (int my_matrix[][], int my_matrixSize )
    {
        matrix = my_matrix;
        matrixSize = my_matrixSize;
        if(gen == null)
            {
                gen = new Random();
            }
    }
	
	public void initializeMatrix() {
		dftseq meatshot = new dftseq(this);
		meatshot.traverse( new Pair(0,0), MazeMatrixInt.MAZE_PATH );
	}
	
	public void serialize(String filename) { 
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try
		{	
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(matrix);	
			out.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void deSerialize(String filename) {
		FileInputStream fis = null;
		ObjectInputStream oin = null;
		try
		{
			fis = new FileInputStream(filename);
			oin = new ObjectInputStream(fis);
			matrix = (int[][])oin.readObject();
			oin.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
    public void process( Pair start, Pair end, int value )
    { 
        int smallx = Math.min( start.getX(), end.getX() ),
            bigx = Math.max( start.getX(), end.getX() ),
            smally = Math.min( start.getY(), end.getY()),
            bigy = Math.max( start.getY(), end.getY());

        for( int i = smallx; i <= bigx; ++i ) 
            {
                for( int j = smally; j <= bigy; ++j ) 
                    {
                        matrix[i][j] = value;
                    }
            }
    }
    
    public void swap (ArrayList<Pair> list, int x, int y)
    {
        Pair temp;
        temp = list.get(y);
        list.set(y, list.get(x));
        list.set(x, temp);
    } 

    public ArrayList<Pair> getNeighbors(Pair w)
    {
        
        ArrayList <Pair> friends  = new ArrayList <Pair> ();
        int x = w.getX();
        int y = w.getY();
        int random = gen.nextInt(17);
        
        Pair something [] = { new Pair(x+2,y), new Pair(x-2, y), new Pair(x, y+2), new Pair(x,y-2)};
        

        for(int i=0; i<something.length;  i++)
            {
                if( ( (something[i].getX() >= 0) && (something[i].getY() >= 0) ) &&  ( (something[i].getX() < matrixSize ) && (something[i].getY() < matrixSize ) ) )
                    {
                        if(matrix[something[i].getX()][something[i].getY()] == 0)
                            {
                                friends.add(something[i]);
                            }
                    }                    
            }
        
        for(int k = 0; k < random; k++)
            {
                for(int i= 0; i<friends.size()-1; i++)
                    {
             
                        int j = gen.nextInt(friends.size()-i);
                        swap(friends, i, i+j);
                    }
            }
        
        return friends;
    }

	
    // could return whatever kind of variable-length container
    public ArrayList<Pair> getNeighbors( Pair coord, Range rowrange, Range colrange ) {

        
        ArrayList <Pair> friends  = new ArrayList <Pair> ();
        int x = coord.getX();
        int y = coord.getY();
        int random = gen.nextInt(17);
        
        Pair something [] = { new Pair(x+2,y), new Pair(x-2, y), new Pair(x, y+2), new Pair(x,y-2)};
        

        for(int i=0; i<something.length;  i++)
            {
                if( ( (something[i].getX() >= colrange.lb()) && (something[i].getY() >= rowrange.lb()) ) &&  ( (something[i].getX() <= colrange.ub() ) && (something[i].getY() <= rowrange.ub() ) ) )
                    {
                        if(matrix[something[i].getX()][something[i].getY()] == 0)
                            {
                                friends.add(something[i]);
                            }
                    }                    
            }
        
        for(int k = 0; k < random; k++)
            {
                for(int i= 0; i<friends.size()-1; i++)
                    {
             
                        int j = gen.nextInt(friends.size()-i);
                        swap(friends, i, i+j);
                    }
            }


        return friends;
    } // neighbors
	

    /**
     * Simple accessor function for the matrix
     */
    public int get( Pair whoever ) {
	return matrix[whoever.getY()][whoever.getX()];
    }

    public int getPosition(int r, int c)
    {
        return matrix[r][c];
    }

    public int[][] getMatrix()
    {
        return matrix;
    }
   /**
     * Makes a new maze with the given matrix (namely, points at the matrix)
     * and guesses at height/width params
     */
    public static void main (String args[]) throws Exception
    {
        if(args.length!=2)
            {
                System.out.println("Please enter the size for the matrix generation and the Seed: N & S");
                System.exit(-1);
            }

        //Matrix size read in from the command line
        int matrixSize = Integer.parseInt(args[0]); 

        int matrix [][] = new int [matrixSize][matrixSize];        

        //Set the seed for the Random genrator
        int seed = Integer.parseInt(args[1]); 
        
        //Give random a seed
        gen = new Random(seed);
        
        //Create an arraylist of storage 
        ArrayList<Pair> storage = new ArrayList <Pair>();

        ///Filled matrix with zeros
        for(int i=0; i<matrixSize; i++)
            {
                for(int j=0; j< matrixSize; j++)
                    {
                        matrix[i][j]=0;                      
                    }
            }

        MazeGenerator maze = new MazeGenerator(matrix, matrixSize);

        Pair testPair = new Pair(5,5);
        
        storage = maze.getNeighbors(testPair);

        for(Pair i: storage)
            {
                maze.process(testPair, i, 1);
            }
        
       
        for(int i=0; i<matrixSize; i++)
            {
                for(int j=0; j< matrixSize; j++)
                    {               
                        System.out.print(maze.getPosition(i,j)+" ");   
                    }
                System.out.println();
            }
        
        /*  ///Filled matrix with zeros
        for(int i=0; i<matrixSize; i++)
            {
                for(int j=0; j< matrixSize; j++)
                {

                    matrix[i][j]=0;
                      
                }
            }

             
        for(int i=0; i<matrixSize; i++)
            {
                for(int j=0; j< matrixSize; j++)
                {
                    System.out.println("Node at Matrix["+i+"]["+j+"]");

                    Pair testPair = new Pair(i,j);
        
                    storage = getNeighbors(testPair, matrix, matrixSize);

                    for(Pair k: storage)
                        {
                            System.out.println(k);
                        }
                    System.out.println();
                }
            }
        */  
    }
}