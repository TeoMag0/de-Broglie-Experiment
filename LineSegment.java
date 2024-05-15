import java.awt.Graphics;

public class LineSegment {
    public final Vector2[] points;
    public final float slope;
    public final float yInt;

    public LineSegment(Vector2 p1, Vector2 p2){
        points = new Vector2[2];
        
        points[0] = p1.clone();
        points[1] = p2.clone();

        slope = (p2.getY()-p1.getY())/(p2.getX()-p1.getX());
        yInt = p1.getY()-slope*p1.getX();
    }
    public LineSegment(Vector2 point, float angle, float magnitude){
        this(point.clone(), Vector2.sum(point, new Vector2(magnitude, angle, true)));
    }

    public void drawMe(Graphics g){
        Vector2 p1Screen = Screen.getScreenCoords(points[0]);
        Vector2 p2Screen = Screen.getScreenCoords(points[1]);
        g.drawLine(p1Screen.intX(), p1Screen.intY(), p2Screen.intX(), p2Screen.intY());
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

        float x = (l2.yInt - l1.yInt)/(l1.slope - l2.slope);
        float y = (l1.slope*l2.slope*(l2.slope*l1.yInt - l1.slope*l2.yInt))/(l2.slope-l1.slope);

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
}
