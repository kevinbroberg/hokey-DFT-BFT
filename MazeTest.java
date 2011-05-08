
public class MazeTest {

    public static void main( String[] args ) throws Exception {
        int rnsize = 5,
            x = 800,
            y = 600;
        switch( args.length ) { 
        case 3: 
            y = Integer.parseInt( args[2] );
        case 2: 
            x = Integer.parseInt( args[1] );
        case 1: 
            rnsize = Integer.parseInt( args[0] );
        }
        int[][] mat = new int [x][y];
        java.util.Random rng = new java.util.Random();


        for( int i = 0 ; i < x; ++i ) { for( int j = 0; j < y; ++j ) {
             mat[i][j] = 0;
        }}
        for( int i = 1 ; i < x; i+=2 ) { for( int j = 1; j < y; j+=2 ) {
             mat[i][j] = 1;
        }}
                    
        MazeMatrixInt m = new MazeMatrixInt( mat, "testout", x, y );
        m.colorMatrix();
        m.printImage();
    }
}
