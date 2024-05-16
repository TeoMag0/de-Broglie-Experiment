import java.awt.Graphics;

public class LineSegment {
    public final Vector2[] points;
    private float slope;
    private float yInt;

    public LineSegment(Vector2 p1, Vector2 p2){
        points = new Vector2[2];
        
        points[0] = p1.clone();
        points[1] = p2.clone();

        if(p2.getX() == p1.getX()){
            slope = Float.POSITIVE_INFINITY;
            yInt = Float.NaN;
        }else{
            slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
            yInt = p1.getY() - slope * p1.getX();
        }
    }
    public LineSegment(Vector2 point, float angle, float magnitude){
        this(point.clone(), Vector2.sum(point, new Vector2(magnitude, angle, true)));
    }

    public void drawMe(Graphics g){
        Vector2 p1Screen = Screen.getScreenCoords(points[0]);
        Vector2 p2Screen = Screen.getScreenCoords(points[1]);
        g.drawLine(p1Screen.intX(), p1Screen.intY(), p2Screen.intX(), p2Screen.intY());
    }
    public float length(){
        return Vector2.distance(points[0], points[1]);
    }
    public void setPoint2(Vector2 p2){
        points[1] = p2.clone();

        if(points[1].getX() == points[0].getX()){
            slope = Float.POSITIVE_INFINITY;
            yInt = Float.NaN;
        }else{
            slope = (points[1].getY() - points[0].getY()) / (points[1].getX() - points[0].getX());
            yInt = points[0].getY() - slope * points[0].getX();
        }
    }
    public float slope(){
        return slope;
    }
    public float yInt(){
        return yInt;
    }

    public static Vector2 intersection(LineSegment l1, LineSegment l2){
        if(l1.slope == l2.slope){
            if(l1.yInt == l2.yInt){
                System.out.println("all solutions");
            }else{
                System.out.println("no solutions");
            }
            return null;
        }
        if(l1.points[0].equals(l1.points[1]) || l2.points[0].equals(l2.points[1])){
            return null;
        }

        float x, y;
        if(Float.isNaN(l1.yInt())){
            x = l1.points[0].getX();
            y = l2.slope()*x+l2.yInt();
        }else if(Float.isNaN(l2.yInt())){
            x = l2.points[0].getX();
            y = l1.slope() * x + l1.yInt();
        }else{
            x = (l2.yInt() - l1.yInt()) / (l1.slope() - l2.slope());
            y = l1.slope()* x + l1.yInt();
        }

        //check domains
        if(x > l1.points[0].getX() && x > l1.points[1].getX() || x < l1.points[0].getX() && x < l1.points[1].getX()){
            return null;
        }
        if(x > l2.points[0].getX() && x > l2.points[1].getX() || x < l2.points[0].getX() && x < l2.points[1].getX()){
            return null;
        }
        if(y > l1.points[0].getY() && y > l1.points[1].getY() || y < l1.points[0].getY() && y < l1.points[1].getY()){
            return null;
        }
        if(y > l2.points[0].getY() && y > l2.points[1].getY() || y < l2.points[0].getY() && y < l2.points[1].getY()){
            return null;
        }

        return new Vector2(x, y);
    }

    public Vector2 getAxis() {
        return Vector2.difference(points[1], points[0]).normalized();
    }
}
