import java.util.ArrayList;
import java.awt.*;

public class ElectronWave {
    private Vector2 originPoint;
    private float angle;
    private ArrayList<LineSegment> waveSegments;

    public ElectronWave(Vector2 originPoint, float angle){
        this.originPoint = originPoint.clone();
        this.angle = angle;

        waveSegments = new ArrayList<>();
    }

    public void drawWave(Graphics g){
        for(LineSegment line : waveSegments){
            line.drawMe(g);
        }
    }

    public void calculateWave(){
        calculateWave(originPoint, angle);
    }
    private void calculateWave(Vector2 origin, float angle){
        //domain from -pi to pi
        angle = (float)Math.atan2(Math.sin(angle), Math.cos(angle));

        float sMagnitude = 2*Screen.getWorldCoords(Screen.screenPixelDimensions).magnitude();
        LineSegment segment = new LineSegment(origin, angle, sMagnitude);

        Vector2 screenBottomRight = Screen.getWorldCoords(Screen.screenPixelDimensions);
        Vector2 screenTopLeft = Vector2.multiply(screenBottomRight, -1);
        
        for(int i=0;i<segment.points.length;i++){
            float p1x = segment.points[i].getX() > screenBottomRight.getX() ? screenBottomRight.getX() : segment.points[i].getX();

            float p1y = segment.points[i].getY() > screenBottomRight.getY() ? screenBottomRight.getY() : segment.points[i].getY();
        }
    }
    private LineSegment clampSegment(Vector2 min, Vector2 max){
        return null;
    }
}
