package Test;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

public class Player {
	private Image player = null;
	private Circle colCircle = null;
	private Rectangle colDown = null;
	private Rectangle colUp = null;
	private Rectangle colSides = null;
	private double x;
	private double y;
	private double checkpointX;
	private double checkpointY;
	private double velX;
	private double velY;
	private boolean downCollision;
	private boolean upCollision;
	private boolean sideCollision;
	private boolean dangerousCollision;
	private boolean alive;
	private boolean onCheckpoint;
	private boolean isPlaced;

	
	
	public Player(String imagePath, int x1, int y1) throws SlickException//konstruktors
	{
		this.player = new Image("sprites/player.png");
		this.x = x1;
		this.y = y1;
		this.checkpointX = x1;
		this.checkpointY = y1;
		this.colCircle = new Circle((float)x1+16, (float)y1+16, 15);
		this.colDown = new Rectangle((int)x1+1,(int)y1+32,30,1);
		this.colUp = new Rectangle((int)x1+1,(int)y1-1,30,1);
		this.colSides = new Rectangle((int)x1,(int)y1,32,32);
		this.velX=0;
		this.velY=0;
		this.downCollision=false;
		this.upCollision=false;
		this.sideCollision=false;
		this.dangerousCollision=false;
		this.alive=true;
		this.onCheckpoint=false;
		this.isPlaced=false;//are the coords x y readed from editor

	}
	public void restart(Camera camera){
		x=checkpointX;
		y=checkpointY;
		alive=true;
		dangerousCollision=false;
		camera.setX(camera.getWidth()/2-x);
		camera.setY(camera.getHeight()/2-y);
	}
	public void velXadd(double additionX){ velX+=additionX; }
	public void velYadd(double additionY){ velY+=additionY; }
	public void velXstop(){ velX=0; }
	public void velYstop(){ velY=0; }
	public int getX(){ return (int)x; }
	public int getY(){ return (int)y; }
	public double getVelX(){ return velX; }
	public double getVelY(){ return velY; }
	public void setX(double x1){ x=x1; }
	public void setY(double y1){ y=y1; }
	public void setIsPlaced(boolean tmp){ isPlaced=tmp; }
	public boolean getIsPlaced(){ return isPlaced;}
	
	public boolean getDownCollision(){ return downCollision; }
	public boolean getDangerousCollision(){ return dangerousCollision; }
	public boolean isOnCheckpoint(){ return onCheckpoint; }
	public boolean isAlive(){
		if(y>600)
		{
			alive=false;
			velYstop();
		}
		else
			alive=true;
		return alive;
	}
	public Circle getCollisionCircle(){
		return colCircle;
	}
	public void checkCollisions(ArrayList<Rectangle> collisions){
		if(!dangerousCollision){//if alive
			downCollision=false;
			upCollision=false;
			sideCollision=false;
			for (Rectangle tmp : collisions){
				if(colDown.intersects(tmp)) downCollision=true;
				if(colUp.intersects(tmp)) upCollision=true;
				if(colSides.intersects(tmp)) sideCollision=true;
			}
		}
		else
		{
			downCollision=false;
		}
			
	}
	public void setCheckpointXY(int x, int y){
		checkpointX=x;
		checkpointY=y;
	}
	public void updateCollidersX(){
		colDown.setX((int)x+1);
		colSides.setX((int)x);
		colUp.setX((int)x+1);
		
		colCircle.setX((int)x+1);
	}
	public void updateCollidersY(){
		colDown.setY((int)y+32);
		colSides.setY((int)y);
		colUp.setY((int)y-1);
		
		colCircle.setY((int)y+1);
	}
	public void reduceVelocity(){ velX=velX*0.9; }

	public Image getImage(){ return player; }
	public void updateCoords(double delta){
		x+=velX*delta;
		y+=velY*delta;
	}
	public void draw(Camera camera){
		player.draw((int)(x+camera.getX()), (int)(y+camera.getY()));
	}
	public void updateAfterCollisions(double delta){
		if(downCollision){
			y-=velY*delta;
			velY=0;
			colDown.setY((int)y+32);
			colSides.setY((int)y);
			colUp.setY((int)y-1);
		}
		if(upCollision){
			y-=velY*delta;
			velY=0;
			colDown.setY((int)y+32);
			colSides.setY((int)y);
			colUp.setY((int)y-1);
		}
		if(sideCollision){
			x-=velX*delta;
			velX=0;
			colDown.setX((int)x+1);
			colSides.setX((int)x);
			colUp.setX((int)x+1);
		}
	}
	public void checkCollisionsWithSaws(ArrayList<StaticSaw> staticSaws){
		for (StaticSaw tmp : staticSaws){
			if(colCircle.intersects(tmp.getCollisionCircle())){
				dangerousCollision=true;
				break;
			}
		}
	}
	public void checkCollisionsWithMovingSaws(ArrayList<MovingSaw> movingSaws){
		for (MovingSaw tmp : movingSaws){
			if(colCircle.intersects(tmp.getCollisionCircle())){
				dangerousCollision=true;
				break;
			}
		}
	}
	public void checkCheckpoints(ArrayList<Checkpoint> checkpoints){
		onCheckpoint=false;
		if(dangerousCollision==false)
			{
			for (Checkpoint tmp : checkpoints){
				if(colCircle.intersects(tmp.getCollisionRectangle())){
					onCheckpoint=true;
					checkpointX=tmp.getX();
					checkpointY=tmp.getY()-2;
				}
			}
		}
	}
	public void moveAround(Input input){
		if(input.isKeyDown(Input.KEY_LEFT) && getDangerousCollision()==false){ velXadd(-32); }
		if(input.isKeyDown(Input.KEY_RIGHT) && getDangerousCollision()==false){ velXadd(32); }
	}
	public void jump(Input input){
		if(input.isKeyPressed(Input.KEY_SPACE) && getDownCollision()){ velYadd(-576); }
	}

}
