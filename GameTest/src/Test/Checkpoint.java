package Test;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Checkpoint {
	private int x;
	private int y;
	private Rectangle colRect = null;
	
	public Checkpoint(int x, int y) throws SlickException//konstruktors
	{
		this.x = x;
		this.y = y;
		this.colRect = new Rectangle(x,y,32,32);
	}
	public void draw(Image diskette, Camera camera){
		diskette.draw((int)camera.getX()+x,(int)camera.getY()+y);
	}
	public int getX(){ return x; }
	public int getY(){ return y; }
	public void setX(int x1){ x=x1; colRect.setX(x1);}
	public void setY(int y1){ y=y1; colRect.setY(y1);}
	public Rectangle getCollisionRectangle(){
		return colRect;
	}
}
