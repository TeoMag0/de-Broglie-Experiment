import java.awt.*;

public class GraphiteCrystal {
    private Vector2 position;
    private float angle;
    private float separationDist;
    private float length;
    private GraphiteLayer[] layers;
    
    public GraphiteCrystal(Vector2 point, float angle, float separationDist, float length){
        layers = new GraphiteLayer[2];

        position = point.clone();
        this.angle = angle;
        this.separationDist = separationDist;
        this.length = length;

        float normalAngle = angle+(float)Math.PI/2;
        Vector2 layer1Midpoint = new Vector2(separationDist/2, normalAngle, true);
        Vector2 layer1P1 = Vector2.sum(layer1Midpoint, Vector2.multiply(new Vector2(1, angle, true), length/2));
        Vector2 layer1P2 = Vector2.sum(layer1Midpoint, Vector2.multiply(new Vector2(1, angle, true), -length/2));
        layers[0] = new GraphiteLayer(layer1P1, layer1P2, 0);

        Vector2 layer2Midpoint = new Vector2(separationDist / 2, normalAngle+(float)Math.PI, true);
        Vector2 layer2P1 = Vector2.sum(layer2Midpoint, Vector2.multiply(new Vector2(1, angle, true), length / 2));
        Vector2 layer2P2 = Vector2.sum(layer2Midpoint, Vector2.multiply(new Vector2(1, angle, true), -length / 2));
        layers[1] = new GraphiteLayer(layer2P1, layer2P2, 1);
    }

    public float separationDist(){
        return separationDist;
    }
}
