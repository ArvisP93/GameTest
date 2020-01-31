package Test;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

public class StaticSaw extends DangerousObject{
	private Circle colCircle = null;
	private int radius;
	
	public StaticSaw(String imagePath, double x1, double y1, int w1, int h1, int r) throws SlickException
	{
	    super(imagePath,x1,y1,w1,h1);
	    setRadius(r);
	    createCollisionCircle();
	}
	
	public void createCollisionCircle(){
		colCircle = new Circle(getX()+getW()/2, getY()+getH()/2, getRadius());
	}
	public Circle getCollisionCircle(){
		return colCircle;
	}
	public void setRadius(int r){ radius = r; }
	public int getRadius(){ return radius; }
	
	public void rotate(int deltaMS){ getImage().rotate(deltaMS); }//delta given in milliseconds, not seconds
	
	
	
}