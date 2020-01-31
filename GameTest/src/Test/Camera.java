package Test;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Camera {
	private double x;
	private double y;
	private int width;
	private int height;
	private int widthDiv3;
	private int heightDiv3;
	
	public Camera(int width, int height) throws SlickException
	{
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
		this.widthDiv3 = width/3;
		this.heightDiv3 = height/3;
	}
		
	public double getX(){ return x; }
	public double getY(){ return y; }
	public double getWidth(){ return width; }
	public double getHeight(){ return height; }
	public void setX(double x1){ x=x1; }
	public void setY(double y1){ y=y1; }
	public void addX(double x1){ x+=x1; }
	public void addY(double y1){ y+=y1; }
	public void reset(Player player, GameContainer container){
		//x=player.getX()-container.getWidth()/2-16;
		//y=player.getY()-container.getHeight()/2-16;
		x=container.getWidth()/2-16-player.getX();
		y=container.getHeight()/2-16-player.getY();
		//x=player.getX()-50;
		//y=player.getY()-50;
		//x=-464.0;
		//y=238;
	}
	public void updateCoords(Player player, double delta){
		if(player.getDangerousCollision()==false){
		if(x+player.getX()+32>width-widthDiv3 && player.getVelX()>0){
			x-=player.getVelX()*delta;
		}
		else if(x+player.getX()<widthDiv3 && player.getVelX()<0){
			x-=player.getVelX()*delta;
		}
		if(y+player.getY()+32>height-heightDiv3 && player.getVelY()>0){
			y-=player.getVelY()*delta;
		}
		else if(y+player.getY()<heightDiv3 && player.getVelY()<0){
			y-=player.getVelY()*delta;
		}
		}
		
	}
}
