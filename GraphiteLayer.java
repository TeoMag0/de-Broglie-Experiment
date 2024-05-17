import java.util.ArrayList;
import java.awt.*;

public class GraphiteLayer {
    private LineSegment segment;
    public static final ArrayList<GraphiteLayer> allGraphites = new ArrayList<>();

    private int layer;

    public GraphiteLayer(Vector2 p1, Vector2 p2, int layer){
        allGraphites.add(this);
        segment = new LineSegment(p1, p2);
        this.layer = layer;
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

    public int layer(){
        return layer;
    }
}
