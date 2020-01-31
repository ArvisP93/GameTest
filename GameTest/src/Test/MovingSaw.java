package Test;

import org.newdawn.slick.SlickException;

public class MovingSaw extends StaticSaw{
	private int limitMin;
	private int limitMax;
	private int direction;
	private int initialDirection;
	private double initialX;
	private double initialY;
	private String axis;
	public MovingSaw(String imagePath, double x1, double y1, int w1, int h1, int r, int limitMin, int limitMax, int initialDirection, String axis) throws SlickException
	{
	    super(imagePath,x1,y1,w1,h1,r);
	    setLimitMin(limitMin);
	    setLimitMax(limitMax);
	    setDirection(initialDirection);
	    setInitialValues(x1,y1,initialDirection);
	    setAxis(axis);
	}
	

	public int getLimitMin(){ return limitMin; }
	public int getLimitMax(){ return limitMax; }
	public void setLimitMin(int min){ limitMin = min; }
	public void setLimitMax(int max){ limitMax = max; }
	public int getInitX(){ return (int)initialX; }
	public int getInitY(){ return (int)initialY; }
	public int getInitDirection(){ return initialDirection; }
	public void setDirection(int dir){ direction = dir; }
	public void setInitialValues(double x, double y, int dir){ initialDirection=dir;initialX=x;initialY=y; }
	public void reverseDirection(){ direction = -direction; }
	public String getAxis(){ return axis; }
	public void setAxis(String axis1){ axis=axis1; }//Horizontal or Vertical
	
	public void restart(){ setX(initialX);setY(initialY);setDirection(initialDirection); }
	
	public void updateCoords(double delta){
		if(getAxis().equals("Horizontal")){
			if(getX()+getW()>=getLimitMax()){
				setX(getLimitMax()-getW()-2);
				reverseDirection();
			}
			else if(getX()<=getLimitMin()){
				setX(getLimitMin()+2);
				reverseDirection();
			}
			addX(128*direction*delta);
		}
		else if(getAxis().equals("Vertical")){
			if(getY()+getH()>=getLimitMax()){
				setY(getLimitMax()-getH()-2);
				reverseDirection();
			}
			else if(getY()<=getLimitMin()){
				setY(getLimitMin()+2);
				reverseDirection();
			}
			addY(128*direction*delta);
		}
	}
	public void updateCollisionCircle(){
		getCollisionCircle().setCenterX(getX()+getW()/2);
		getCollisionCircle().setCenterY(getY()+getH()/2);
	}
}
