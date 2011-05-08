public class Pair {
private int x,y;

public Pair( int a, int b ) { x = a; y = b;
}

public int dist( Pair other ) {
    return Math.abs( this.x - other.x ) + Math.abs( this.y - other.y );
}//dist

public String toString() {
    return "("+x+","+y+")";
}

public int getX() {return x;}
public int getY() {return y;}
}//class
