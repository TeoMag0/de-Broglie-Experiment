import java.util.ArrayList;
import java.awt.*;
import java.util.HashSet;

public class ElectronWave {
    private Vector2 originPoint;
    private float angle;
    private ArrayList<LineSegment> waveSegments;
    private static final ArrayList<ElectronWave> allWaves = new ArrayList<>();
    private static final int maxWaveBounces = 100;
    private int waveBounces;
    private final float tickMarkInterval;
    private final float tickMarkLength;

    private int layer;
    private HashSet<LineSegment> braggSegments;

    public ElectronWave(Vector2 originPoint, float angle, int layer, float wavelength){
        allWaves.add(this);
        this.originPoint = originPoint.clone();
        this.angle = angle;
        waveBounces = 0;
        tickMarkInterval = wavelength;
        tickMarkLength = .2f;

        waveSegments = new ArrayList<>();
        this.layer = layer;
        braggSegments = new HashSet<>();
    }
    
    public void drawWave(Graphics g){
        float waveOffset = 0;
        for(LineSegment line : waveSegments){
            line.drawMe(g);
            if(line.length() + waveOffset - tickMarkInterval < 0){
                waveOffset += line.length();
                continue;
            }

            int numTicks = (int)((line.length()-waveOffset)/tickMarkInterval)+1;

            Vector2 newStart = Vector2.sum(line.points[0], Vector2.multiply(line.getAxis(), waveOffset));
            Vector2 tickDisp = Vector2.multiply(line.getAxis(), tickMarkInterval);
            for(int i=0;i<numTicks;i++){
                Vector2 tickLoc = Vector2.sum(newStart, Vector2.multiply(tickDisp, i));
                float tickAngle = (float)(Math.PI/2 + Math.atan2(line.getAxis().getY(), line.getAxis().getX()));

                drawTick(g, tickLoc, tickAngle);
            }
            
            waveOffset = tickMarkInterval-(line.length()-waveOffset - (numTicks-1)*tickMarkInterval);
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

        Vector2 screenTopRight = Screen.getWorldCoords(new Vector2(Screen.screenPixelDimensions.getX(), 0));
        Vector2 screenBottomLeft = Vector2.multiply(screenTopRight, -1);
        LineSegment clampedSeg = clampSegment(segment, screenBottomLeft, screenTopRight);

        // Object[] {Vector2 origin, float direction}
        Object[] reflectedWave = getReflectedWave(clampedSeg, origin);
        if (reflectedWave != null) {
            LineSegment newSeg = new LineSegment(origin, (Vector2)reflectedWave[0]);
            waveSegments.add(newSeg);
            if(newSeg.getAxis().equals(clampedSeg.getAxis())){
                braggSegments.add(newSeg);
            }
            calculateWave((Vector2)reflectedWave[0], (float)reflectedWave[1]);
        }else{
            waveSegments.add(clampedSeg);
        }

    }
    private LineSegment clampSegment(LineSegment seg, Vector2 min, Vector2 max){
        Vector2 p1 = null;
        Vector2 p2 = null;
        for (int i = 0; i < seg.points.length; i++) {

            float px = seg.points[i].getX();
            if (px > max.getX()) {
                px = max.getX();
            } else if (px < min.getX()) {
                px = min.getX();
            }
            float py = seg.slope()*px+seg.yInt();
            if(py > max.getY()){
                py = max.getY();
                px = (py - seg.yInt()) / seg.slope();
            }else if(py < min.getY()){
                py = min.getY();
                px = (py - seg.yInt()) / seg.slope();
            }


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

        if(layer == graphite.layer()){
            Vector2 graphiteNormal = new Vector2(graphite.lineSegment().getAxis().getY(), -graphite.lineSegment().getAxis().getX());
            Vector2 segAxis = seg.getAxis();

            Vector2 deltaV = Vector2.multiply(graphiteNormal, -2*Vector2.dot(graphiteNormal, segAxis));

            Vector2 newDirection = Vector2.sum(segAxis, deltaV);

            return new Object[] {colPoint, (float)Math.atan2(newDirection.getY(), newDirection.getX())};
        }else{
            return new Object[] { colPoint, (float) Math.atan2(seg.getAxis().getY(), seg.getAxis().getX()) };
        }
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
    public int layer(){
        return layer;
    }
}
