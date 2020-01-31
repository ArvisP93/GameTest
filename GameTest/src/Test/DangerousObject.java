package Test;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class DangerousObject {
	private Image sprite = null;
	private String imagePath;
	private double x;
	private double y;
	private int w;
	private int h;
	
	public DangerousObject(String imagePath, double x1, double y1, int w1, int h1) throws SlickException
	{
		this.sprite = new Image(imagePath);
		this.imagePath = imagePath;
		this.x=x1;
		this.y=y1;
		this.w=w1;
		this.h=h1;
	}
	
	
	public int getX(){ return (int)x; }
	public int getY(){ return (int)y; }
	public void setX(double x1){ x=x1; }
	public void setY(double y1){ y=y1; }
	public void addX(double x1){ x+=x1; }
	public void addY(double y1){ y+=y1; }
	public int getW(){ return w; }
	public int getH(){ return h; }
	public void setW(int w1){ w=w1; }
	public void setH(int h1){ h=h1; }
	public void draw(Camera camera){ sprite.draw((int)camera.getX()+(int)x, (int)camera.getY()+(int)y); }
	public Image getImage(){ return sprite; }
	public String getImagePath(){ return imagePath; }
}
