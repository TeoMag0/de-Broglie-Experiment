import java.io.Serializable;

public class Vector2 implements Serializable{
    private float x, y;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }
    public Vector2(float mag, float angle, boolean a){
        x = mag * (float)Math.cos(angle);
        y = mag * (float)Math.sin(angle);
    }

    /**
     * Returns the x value of this vector.
     * @return
     */
    public float getX(){
        return x;
    }
    /**
     * Returns the x value of this vector as an int.
     * @return
     */
    public int intX(){
        return (int)x;
    }
    /**
     * Returns the y value of this vector as an int.
     * @return
     */
    public int intY(){
        return (int)y;
    }
    /**
     * Returns the y value of this vector.
     * @return
     */
    public float getY(){
        return y;
    }
    public Vector2 getDrawnCoords(){
        return new Vector2(x*200f+640, -y*200f+360);
    }
    /**
     * Sets the velocity using components.
     * @param x
     * @param y
     */
    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }
    /**
     * Sets the velocity using a vector
     * @param v
     */
    public void set(Vector2 v){
        x = v.getX();
        y = v.getY();
    }
    /**
     * Adds the inputted vector to this vector.
     * @param vector
     */
    public void add(Vector2 vector){
        x += vector.x;
        y += vector.y;
    }
    /**
     * Subtracts the inputted vector from this vector.
     * @param vector
     */
    public void subtract(Vector2 vector){
        x -= vector.getX();
        y -= vector.getY();
    }
    /**
     * Returns a new unit vector in the same direction as the vector
     * @return
     */
    public Vector2 normalized(){
        return new Vector2(x/magnitude(), y/magnitude());
    }
    /**
     * Returns the magnitude of this vector
     * @return
     */
    public float magnitude(){
        return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    /**
     * Returns the angle of this vector in radians in [-pi,pi]
     * @return
     */
    public float getAngle(){
        return (float)Math.atan2(y,x);
    }
    /*
     * Returns a clone of this vector
     */
    public Vector2 clone(){
        return new Vector2(x, y);
    }

    /**
     * Returns the sum of two vectors.
     * @param vector1
     * @param vector2
     * @return
     */
    public static Vector2 sum(Vector2 vector1, Vector2 vector2){
        return new Vector2(vector1.x + vector2.x, vector1.y + vector2.y);
    }

    /**
     * Returns the difference of two vectors.
     * @param vector1
     * @param vector2
     * @return
     */
    public static Vector2 difference(Vector2 vector1, Vector2 vector2){
        return new Vector2(vector1.x - vector2.x, vector1.y - vector2.y);
    }
    /**
     * Return the dot product between two vectors
     * @param v1
     * @param v2
     * @return
     */
    public static float dot(Vector2 v1, Vector2 v2){
        return v1.getX()*v2.getX() + v1.getY()*v2.getY();
    }
    /**
     * Return the magnitude of the cross product of two vectors
     * @param v1
     * @param v2
     * @return
     */
    public static float cross(Vector2 v1, Vector2 v2){
        return v1.getX()*v2.getY()-v1.getY()*v2.getX();
    }
    /*
     * Return a vector cross product of two vectors (one in the k direction)
     */
    public static Vector2 cross(float v1, Vector2 v2){
        return new Vector2(v1*v2.getY(), v1*v2.getX());
    }
    /*
     * Return a vector cross product of two vectors (one in the k direction)
     */
    public static Vector2 cross(Vector2 v1, float v2) {
        return new Vector2(v1.getY()*v2, -v1.getX()*v2);
    }
    /**
     * returns the distance between the vectors
     * @return
     */
    public static float distance(Vector2 vector1, Vector2 vector2){
        return (float)Math.sqrt(Math.pow(Vector2.difference(vector1, vector2).x, 2) + Math.pow(Vector2.difference(vector1, vector2).y, 2));
    }
    /**
     * Returns new vector product of a vector and a scalar
     * @param vector
     * @param num
     * @return
     */
    public static Vector2 multiply(Vector2 vector, float num){
        return new Vector2(vector.getX() * num, vector.getY() * num);
    }

    /**
     * Returns a new Vector <0,0>
     * @return
     */
    public static Vector2 zero(){
        return new Vector2(0, 0);
    }
    /**
     * Returns a unit vector straight up
     * @return
     */
    public static Vector2 up(){
        return new Vector2(0,1);
    }
    /**
     * Returns a unit vector straight down
     * @return
     */
    public static Vector2 down(){
        return new Vector2(0,-1);
    }
    /**
     * Returns a unit vector to the right
     * @return
     */
    public static Vector2 right(){
        return new Vector2(1,0);
    }
    /**
     * Returns a unit vector to the left
     * @return
     */
    public static Vector2 left(){
        return new Vector2(-1,0);
    }
    /**
     * Returns whether Vectors are equal
     * @return
     */
    @Override
    public boolean equals(Object e){
        Vector2 v = (Vector2)e;
        if(Math.abs(getX() - v.getX()) < 0.001 && Math.abs(getY() - v.getY()) < 0.001)
            return true;
        return false;
    }
    @Override
    public int hashCode(){
        int code = 0;
        code += x > 0 ? Math.abs(x*100001) : Math.abs(x*1000001);
        code += y > 0 ? Math.abs(y*1001) : Math.abs(y*10001);
        return code;
    }

    @Override
    public String toString(){
        return "<"+getX()+", "+getY()+">";
    }
}
