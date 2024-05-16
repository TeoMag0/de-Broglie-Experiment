import java.util.ArrayList;
import java.awt.*;

public class ElectronWave {
    private Vector2 originPoint;
    private float angle;
    private ArrayList<LineSegment> waveSegments;
    private static final ArrayList<ElectronWave> allWaves = new ArrayList<>();
    private static final int maxWaveBounces = 100;
    private int waveBounces;
    private final float tickMarkInterval;
    private final float tickMarkLength;

    public ElectronWave(Vector2 originPoint, float angle){
        allWaves.add(this);
        this.originPoint = originPoint.clone();
        this.angle = angle;
        waveBounces = 0;
        tickMarkInterval = .3f;
        tickMarkLength = .5f;

        waveSegments = new ArrayList<>();
    }

    public void drawWave(Graphics g){
        float lastWaveDist = 0;
        for(LineSegment line : waveSegments){
            line.drawMe(g);

            int numTicks = (int)((line.length()-lastWaveDist)/tickMarkInterval);
            lastWaveDist = line.length() - numTicks*tickMarkInterval;
            Vector2 newDisp = Vector2.multiply(line.getAxis(), lastWaveDist);
            Vector2 tickDisp = Vector2.multiply(line.getAxis(), tickMarkInterval);
            for(int i=0;i<numTicks;i++){
                Vector2 tickLoc = Vector2.sum(Vector2.sum(line.points[0], newDisp), Vector2.multiply(tickDisp, i));
                float tickAngle = (float)(Math.PI/2 + Math.atan2(line.getAxis().getY(), line.getAxis().getX()));

                drawTick(g, tickLoc, tickAngle);
            }
        }
    }
    private void drawTick(Graphics g, Vector2 point, float angle){
        Vector2 halfLength = new Vector2(tickMarkLength/2, angle, true);
        Vector2 startLoc = Vector2.difference(point, halfLength);
        Vector2 endLoc = Vector2.sum(point, halfLength);

        Vector2 screenStart = Screen.getScreenCoords(startLoc);
        Vector2 screenEnd = Screen.getScreenCoords(endLoc);

        g.drawLine(screenStart.intX(), screenStart.intY(), screenEnd.intX(), screenEnd.intY());
    }

    public float length(){
        float length = 0;
        for(LineSegment each : waveSegments){
            length += each.length();
        }
        return length;
    }

    public synchronized void calculateWave(){
        waveSegments.clear();
        waveBounces = 0;
        calculateWave(originPoint, angle);
    }
    private void calculateWave(Vector2 origin, float angle){
        waveBounces++;
        if(waveBounces > maxWaveBounces){
            return;
        }
        float sMagnitude = 2*Screen.getWorldCoords(Screen.screenPixelDimensions).magnitude();
        LineSegment segment = new LineSegment(origin, angle, sMagnitude);

        Vector2 screenTopRight = Screen.getWorldCoords(new Vector2(Screen.screenPixelDimensions.getX(), -Screen.screenPixelDimensions.getY()));
        Vector2 screenBottomLeft = Vector2.multiply(screenTopRight, -1);
        LineSegment clampedSeg = clampSegment(segment, screenBottomLeft, screenTopRight);

        // Object[] {Vector2 origin, float direction}
        Object[] reflectedWave = getReflectedWave(clampedSeg, origin);
        if (reflectedWave != null) {
            waveSegments.add(new LineSegment(origin, (Vector2)reflectedWave[0]));
            calculateWave((Vector2)reflectedWave[0], (float)reflectedWave[1]);
        }else{
            waveSegments.add(clampedSeg);
        }

    }
    private LineSegment clampSegment(LineSegment seg, Vector2 min, Vector2 max){
        Vector2 p1 = null;
        Vector2 p2 = null;
        for (int i = 0; i < seg.points.length; i++) {
            float px = seg.points[i].getX() > max.getX() ? max.getX() : seg.points[i].getX();
            px = px < min.getX() ? min.getX() : px;
            float py = seg.slope()*px+seg.yInt();

            py = seg.points[i].getY() > max.getY() ? max.getY() : seg.points[i].getY();
            py = py < min.getY() ? min.getY() : py;
            px = (py-seg.yInt())/seg.slope();

            if(i == 0){
                p1 = new Vector2(px, py);
            }else{
                p2 = new Vector2(px, py);
            }
        }
        return new LineSegment(p1, p2);
    }
    private Object[] getReflectedWave(LineSegment seg, Vector2 origin){
        GraphiteLayer graphite = null;
        Vector2 colPoint = null;
        for(GraphiteLayer each : GraphiteLayer.allGraphites){
            Vector2 intersection = LineSegment.intersection(seg, each.lineSegment());
            if(intersection == null){
                continue;
            }
            if(origin.equals(intersection)){
                continue;
            }
            if(colPoint == null || Vector2.distance(colPoint, origin) > Vector2.distance(intersection, origin)){
                colPoint = intersection;
                graphite = each;
            }
        }
        
        if(colPoint == null){
            return null;
        }

        Vector2 graphiteNormal = new Vector2(graphite.lineSegment().getAxis().getY(), -graphite.lineSegment().getAxis().getX());
        Vector2 segAxis = seg.getAxis();

        Vector2 deltaV = Vector2.multiply(graphiteNormal, -2*Vector2.dot(graphiteNormal, segAxis));

        Vector2 newDirection = Vector2.sum(segAxis, deltaV);

        return new Object[] {colPoint, (float)Math.atan2(newDirection.getY(), newDirection.getX())};
    }

    public static void drawAll(Graphics g){
        g.setColor(Color.RED);
        for(ElectronWave each : allWaves){
            each.drawWave(g);
        }
    }
    public static void calculateAll(){
        for(ElectronWave each : allWaves){
            each.calculateWave();
        }
    }
}
