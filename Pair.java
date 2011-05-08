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

public void setX(int x_new) {this.x = x_new;}
public void setY(int y_new) {this.y = y_new;}

public int getX() {return x;}
public int getY() {return y;}
}//class
