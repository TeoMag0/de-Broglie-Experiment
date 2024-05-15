import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel {

	private static int pixelsPerUnit = 100;
	public static final Vector2 screenPixelDimensions = new Vector2(1280, 720);
	public static final Screen Singleton = new Screen();
	private static Vector2 cameraPos = Vector2.zero();

	public Screen() {
		this.setLayout(null);
		this.setFocusable(true);
		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);

	}
    
	public Dimension getPreferredSize() {
        return new Dimension(screenPixelDimensions.intX(),screenPixelDimensions.intY());
	}

	public static Vector2 getWorldCoords(Vector2 coords){
		float newX = (coords.getX()-screenPixelDimensions.getX()/2)/pixelsPerUnit+cameraPos.getX();
		float newY = (screenPixelDimensions.getY()/2-coords.getY())/pixelsPerUnit+cameraPos.getY();
		return new Vector2(newX, newY);
	}
	public static Vector2 getScreenCoords(Vector2 coords){
		int newX = (int)((coords.getX()-cameraPos.getX())*pixelsPerUnit + screenPixelDimensions.getX()/2);
		int newY = (int)(screenPixelDimensions.getY()/2 - (coords.getY()-cameraPos.getY())*pixelsPerUnit);
		return new Vector2(newX, newY);
	}
	public static int toPixels(float num){
		return (int)Math.ceil(num*pixelsPerUnit);
	}
	public static void setPixelsPerUnit(int ppu){
		pixelsPerUnit = ppu;
	}

	@Override
	public synchronized void repaint(){
		super.repaint();
	}
}