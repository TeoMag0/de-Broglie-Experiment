import java.util.ArrayList;
import java.awt.*;

public class GraphiteLayer {
    private LineSegment segment;
    public static final ArrayList<GraphiteLayer> allGraphites = new ArrayList<>();

    public GraphiteLayer(Vector2 p1, Vector2 p2){
        allGraphites.add(this);
        segment = new LineSegment(p1, p2);
    }

    public LineSegment lineSegment(){
        return segment;
    }

    public void drawMe(Graphics g){
        segment.drawMe(g);
    }

    public static void drawAll(Graphics g){
        g.setColor(Color.BLUE);
        for(GraphiteLayer each : allGraphites){
            each.drawMe(g);
        }
    }
}
