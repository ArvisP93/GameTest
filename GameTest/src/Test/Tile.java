package Test;

import org.newdawn.slick.SpriteSheet;

public class Tile {
	private int x;
	private int y;
	private int w;
	private int h;
	private int sheetX;
	private int sheetY;
	
	public Tile(int x, int y, int w, int h, int sheetX, int sheetY)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.sheetX = sheetX;
		this.sheetY = sheetY;
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	public int getSheetX(){return sheetX;}
	public int getSheetY(){return sheetY;}
	public void draw(SpriteSheet spritesheet, Camera camera)
	{
		spritesheet.getSubImage(sheetX, sheetY, w, h).drawEmbedded((int)camera.getX()+x,(int)camera.getY()+y, w, h);
	}
}
