import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Screen extends JPanel implements MouseInputListener{

	private static int pixelsPerUnit = 100;
	public static final Vector2 screenPixelDimensions = new Vector2(1280, 720);
	public static final Screen Singleton = new Screen();
	private static Vector2 cameraPos = Vector2.zero();

	private GraphiteLayer draggingGraphite;
	private Vector2 mousePos = Vector2.zero();

	public Screen() {
		setLayout(null);
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);

		draggingGraphite = null;
		
		new ElectronWave(new Vector2(-4, .5f), 0, 0);
		new ElectronWave(new Vector2(-4, -.5f), 0, 1);

		new GraphiteCrystal(new Vector2(3,0), (float)(Math.PI/6), 1f, 6);
	}

	public synchronized void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, screenPixelDimensions.intX(), screenPixelDimensions.intY());

		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(3));

		if (draggingGraphite != null) {
			draggingGraphite.lineSegment().setPoint2(mousePos);
		}

		ElectronWave.calculateAll();
		ElectronWave.drawAll(g2);

		GraphiteLayer.drawAll(g2);
	}

	public void animate(){
		while(true){

			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
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



	private Vector2 clickPoint = null;
	@Override
	public void mousePressed(MouseEvent e) {
		clickPoint = Screen.getWorldCoords(new Vector2(e.getX(), e.getY()));
		System.out.println(clickPoint);
	}
	@Override
	public void mouseDragged(MouseEvent e){
		mousePos = Screen.getWorldCoords(new Vector2(e.getX(), e.getY()));
		if (draggingGraphite == null) {
			draggingGraphite = new GraphiteLayer(clickPoint, mousePos, 0);
		}
		repaint();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		draggingGraphite = null;
		repaint();
	}


	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseMoved(MouseEvent e){
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
}