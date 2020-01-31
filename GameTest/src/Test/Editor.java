package Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Editor {
	private int pointerX;
	private int pointerY;
	private String editMode;
	private String editAxis;
	private int editSheetX;
	private int editSheetY;
	private int editWidth;
	private int editHeight;
	private int editLimitMin;
	private int editLimitMax;
	private int editDirection;
	private int editPlayerX;
	private int editPlayerY;
	private boolean editIsPlayerSet;
	private boolean sawsMoving;
	private Image player;
	private Image saw;
	private Image largesaw;
	private Image diskette;
	
	public Editor(Player player) throws SlickException
	{
		this.pointerX=32*12;
		this.pointerY=32*8;
		this.editMode="player";
		this.editAxis="Horizontal";
		this.editSheetX=0;
		this.editSheetY=0;
		this.editWidth=32;
		this.editHeight=32;
		this.editLimitMin=0;
		this.editLimitMax=0;
		this.editDirection=1;
		this.editPlayerX=0;
		this.editPlayerY=0;
		this.sawsMoving=true;
		this.editIsPlayerSet=player.getIsPlaced();
		this.player=new Image("sprites/player.png");
		this.saw=new Image("sprites/saw.png");
		this.largesaw=new Image("sprites/largesaw.png");
		this.diskette=new Image("sprites/diskette.png");
	}

	public void selectMode(Input input){
		if(input.isKeyPressed(Input.KEY_1) || input.isKeyPressed(Input.KEY_NUMPAD1))
			editMode="player";
		else if(input.isKeyPressed(Input.KEY_2) || input.isKeyPressed(Input.KEY_NUMPAD2))
			editMode="checkpoint";
		if(input.isKeyPressed(Input.KEY_3) || input.isKeyPressed(Input.KEY_NUMPAD3))
			editMode="tile";
		if(input.isKeyPressed(Input.KEY_4) || input.isKeyPressed(Input.KEY_NUMPAD4))
			editMode="collision";
		if(input.isKeyPressed(Input.KEY_5) || input.isKeyPressed(Input.KEY_NUMPAD5))
			editMode="staticSaw";
		if(input.isKeyPressed(Input.KEY_6) || input.isKeyPressed(Input.KEY_NUMPAD6))
			editMode="movingSawLimitMin";
	}
	
	public void moveAround(Input input, Camera camera){
		if(input.isKeyPressed(Input.KEY_LEFT)){pointerX-=32;camera.addX(32);}
		if(input.isKeyPressed(Input.KEY_RIGHT)){pointerX+=32;camera.addX(-32);}
		if(input.isKeyPressed(Input.KEY_UP)){pointerY-=32;camera.addY(32);}
		if(input.isKeyPressed(Input.KEY_DOWN)){pointerY+=32;camera.addY(-32);}
	}
	
	public void update(Input input, String loadLevelName, SpriteSheet sprites, ArrayList<Tile> tiles, ArrayList<Rectangle> collisions, ArrayList<Checkpoint> checkpoints, ArrayList<StaticSaw> staticSaws, ArrayList<MovingSaw> movingSaws) throws SlickException, IOException{
		if(input.isKeyPressed(Input.KEY_ENTER)){
			saveLevel(loadLevelName,tiles,collisions,checkpoints,staticSaws,movingSaws);
			System.out.println(loadLevelName + " saved!");
		}
		if(input.isKeyPressed(Input.KEY_Z)){
			for(int i=0;i<tiles.size();i++)
				if(tiles.get(i).getX()==pointerX && tiles.get(i).getY()==pointerY)
					tiles.remove(i);
			for(int i=0;i<checkpoints.size();i++)
				if(checkpoints.get(i).getX()==pointerX && checkpoints.get(i).getY()==pointerY)
					checkpoints.remove(i);
			for(int i=0;i<collisions.size();i++)
				if(collisions.get(i).getX()==pointerX && collisions.get(i).getY()==pointerY)
					collisions.remove(i);
			for(int i=0;i<staticSaws.size();i++)
				if(staticSaws.get(i).getX()==pointerX && staticSaws.get(i).getY()==pointerY)
					staticSaws.remove(i);
			for(int i=0;i<movingSaws.size();i++)
				if(movingSaws.get(i).getInitX()==pointerX && movingSaws.get(i).getInitY()==pointerY)
					movingSaws.remove(i);
		}
		if(input.isKeyPressed(Input.KEY_X)){
			if(sawsMoving)
				sawsMoving=false;
			else
				sawsMoving=true;
		}
		if(sawsMoving==false)
			for (MovingSaw tmp : movingSaws)
				tmp.restart();
		if(editMode.equals("tile"))
		{
			if(input.isKeyPressed(Input.KEY_Q)){
				if(editSheetX>0)
					editSheetX-=32;
				else if(editSheetX==0 && editSheetY!=0){
					editSheetX=sprites.getWidth()-32;
					editSheetY-=32;
				}
				else if(editSheetX==0 && editSheetY==0){
					editSheetX=sprites.getWidth()-32;
					editSheetY=sprites.getHeight()-32;
				}
			}
			if(input.isKeyPressed(Input.KEY_W)){
				if(editSheetX<sprites.getWidth()-32)
					editSheetX+=32;
				else if(editSheetX==sprites.getWidth()-32 && editSheetY!=sprites.getHeight()-32){
					editSheetX=0;
					editSheetY+=32;
				}
				else if(editSheetX==sprites.getWidth()-32 && editSheetY==sprites.getHeight()-32){
					editSheetX=0;
					editSheetY=0;
				}
			}
			if(input.isKeyPressed(Input.KEY_SPACE)){
				System.out.println("tile="+pointerX+","+pointerY+","+32+","+32+","+editSheetX+","+editSheetY);
				tiles.add(new Tile(pointerX,pointerY,32,32,editSheetX,editSheetY));
			}
		}
		if(editMode.equals("collision"))
		{
			if(input.isKeyPressed(Input.KEY_Q)){
				if(editWidth>0)
					editWidth-=32;
			}
			if(input.isKeyPressed(Input.KEY_W)){
					editWidth+=32;
			}
			if(input.isKeyPressed(Input.KEY_A)){
				if(editHeight>0)
					editHeight-=32;
			}
			if(input.isKeyPressed(Input.KEY_S)){
					editHeight+=32;
			}
			if(input.isKeyPressed(Input.KEY_SPACE)){
				System.out.println("collision="+pointerX+","+pointerY+","+editWidth+","+editHeight);
				collisions.add(new Rectangle(pointerX,pointerY,editWidth,editHeight));
			}
		}
		if(editMode.equals("checkpoint")){
			if(input.isKeyPressed(Input.KEY_SPACE)){
				System.out.println("checkpoint="+pointerX+","+pointerY);
				checkpoints.add(new Checkpoint(pointerX, pointerY));
			}
		}
		if(editMode.equals("player")){
			if(input.isKeyPressed(Input.KEY_SPACE)){
				editPlayerX=pointerX;
				editPlayerY=pointerY;
				editIsPlayerSet=true;
				System.out.println("player=sprites/player.png,"+editPlayerX+","+editPlayerY);
			}
		}
		if(editMode.equals("staticSaw")){
			if(input.isKeyPressed(Input.KEY_SPACE)){
				System.out.println("staticSaw=sprites/saw.png,"+(double)pointerX+","+(double)pointerY+","+32+","+32+","+16);
				staticSaws.add(new StaticSaw("sprites/saw.png",(double)pointerX, (double)pointerY, 32, 32, 16));
			}
		}
		if(editMode.equals("movingSawLimitMin")){
			if(input.isKeyPressed(Input.KEY_Q))
				editAxis="Horizontal";
			else if(input.isKeyPressed(Input.KEY_W))
				editAxis="Vertical";
			
			if(editAxis.equals("Horizontal"))
				editLimitMin=pointerX;
			else if(editAxis.equals("Vertical"))
				editLimitMin=pointerY;
			
			if(input.isKeyPressed(Input.KEY_SPACE)){
				editMode="movingSawLimitMax";
			}
		}
		if(editMode.equals("movingSawLimitMax")){
			if(editAxis.equals("Horizontal"))
				editLimitMax=pointerX;
			else if(editAxis.equals("Vertical"))
				editLimitMax=pointerY;
			
			if(input.isKeyPressed(Input.KEY_SPACE)){
				if(editLimitMax<editLimitMin){
					editLimitMax=editLimitMin;
					if(editAxis.equals("Horizontal"))
						editLimitMin=pointerX;
					else if(editAxis.equals("Vertical"))
						editLimitMin=pointerY;
				}
				editMode="movingSawSize";
			}
		}
		if(editMode.equals("movingSawSize")){
			if(input.isKeyPressed(Input.KEY_Q)){
				editWidth=32;editHeight=32;
			}
			else if(input.isKeyPressed(Input.KEY_W)){
				editWidth=64;editHeight=64;
			}
			if(input.isKeyPressed(Input.KEY_A)){
				editDirection=-1;
			}
			else if(input.isKeyPressed(Input.KEY_S)){
				editDirection=1;
			}
			
			if(input.isKeyPressed(Input.KEY_SPACE)){
				if(editAxis.equals("Horizontal"))
					if(pointerX + editWidth >= editLimitMax)
						pointerX=editLimitMax-editWidth;
					else if(pointerX <= editLimitMin)
						pointerX=editLimitMin;
				else if(editAxis.equals("Vertical"))
						if(pointerY + editHeight >= editLimitMax)
						pointerY=editLimitMax-editHeight;
					else if(pointerY <= editLimitMin)
						pointerY=editLimitMin;

				if(editWidth==32){
					System.out.println("movingSaw=sprites/saw.png,"+pointerX+","+pointerY+","+editWidth+","+editHeight+","+editWidth/2+","+editLimitMin+","+editLimitMax+","+editDirection+","+editAxis);
					movingSaws.add(new MovingSaw("sprites/saw.png",pointerX,pointerY,editWidth,editHeight,editWidth/2,editLimitMin,editLimitMax,editDirection,editAxis));
				}
				else if(editWidth==64){
					System.out.println("movingSaw=sprites/largesaw.png,"+pointerX+","+pointerY+","+editWidth+","+editHeight+","+editWidth/2+","+editLimitMin+","+editLimitMax+","+editDirection+","+editAxis);
					movingSaws.add(new MovingSaw("sprites/largesaw.png",pointerX,pointerY,editWidth,editHeight,editWidth/2,editLimitMin,editLimitMax,editDirection,editAxis));
				}
				for (MovingSaw tmp : movingSaws){
					tmp.restart();
				}
				editMode="movingSawLimitMin";
			}
		}
	}
	
	public void drawCollisions(Camera camera, Graphics g, ArrayList<Rectangle> collisions){
		g.setColor(Color.white);
		for(Rectangle tmp : collisions){
			g.drawRect((int)camera.getX()+tmp.getX(), (int)camera.getY()+tmp.getY(), tmp.getWidth(), tmp.getHeight());
			g.drawRect((int)camera.getX()+tmp.getX()+1, (int)camera.getY()+tmp.getY()+1, tmp.getWidth(), tmp.getHeight());
		}
	}

	public void drawModeItems(GameContainer container, SpriteSheet sprites, Graphics g, Camera camera){
		if(editMode.equals("tile")){
			sprites.startUse();
			sprites.getSubImage(editSheetX, editSheetY, 32, 32).drawEmbedded((int)camera.getX()+pointerX,(int)camera.getY()+pointerY, 32, 32);
			sprites.endUse();
		}
		else if(editMode.equals("collision")){
			g.drawRect((int)camera.getX()+pointerX, (int)camera.getY()+pointerY, editWidth, editHeight);
		}
		else if(editMode.equals("player")){
			player.draw((int)camera.getX()+pointerX, (int)camera.getY()+pointerY);
		}
		else if(editMode.equals("checkpoint")){
			diskette.draw((int)camera.getX()+pointerX, (int)camera.getY()+pointerY);
		}
		else if(editMode.equals("staticSaw")){
				saw.draw((int)camera.getX()+pointerX, (int)camera.getY()+pointerY);
		}
		else if(editMode.equals("movingSawLimitMin")){
			g.setColor(Color.red);
			if(editAxis.equals("Horizontal"))
				g.drawRect((int)camera.getX()+editLimitMin, 0, 2, container.getHeight());
			if(editAxis.equals("Vertical"))
				g.drawRect(0, (int)camera.getY()+editLimitMin, container.getWidth(), 2);
		}
		else if(editMode.equals("movingSawLimitMax") || editMode.equals("movingSawSize")){
			g.setColor(Color.red);
			if(editAxis.equals("Horizontal")){
				g.drawRect((int)camera.getX()+editLimitMin, 0, 2, container.getHeight());
				g.drawRect((int)camera.getX()+editLimitMax, 0, 2, container.getHeight());
			}
			if(editAxis.equals("Vertical")){
				g.drawRect(0, (int)camera.getY()+editLimitMin, container.getWidth(), 2);
				g.drawRect(0, (int)camera.getY()+editLimitMax, container.getWidth(), 2);
			}
		}
		if(editMode.equals("movingSawSize")){
			if(editWidth==32)
				saw.draw((int)camera.getX()+pointerX, (int)camera.getY()+pointerY);
			else if(editWidth==64)
				largesaw.draw((int)camera.getX()+pointerX, (int)camera.getY()+pointerY);
		}
	}
	
	public void drawPlayer(Camera camera){
		if(editIsPlayerSet)
			player.draw((int)camera.getX()+editPlayerX, (int)camera.getY()+editPlayerY);
	}

	public void drawPointer(Graphics g, Camera camera){
		g.setColor(Color.black);
		g.drawRect((int)camera.getX()+pointerX, (int)camera.getY()+pointerY, 16, 1);
		g.drawRect((int)camera.getX()+pointerX, (int)camera.getY()+pointerY, 1, 16);
	}

	public void drawText(GameContainer container, Graphics g){
		g.drawString("Edit mode: " + editMode, container.getWidth()-260, 6);
		if(editMode.equals("tile"))
			g.drawString("Select tile: <[Q] [W]>", container.getWidth()-260, 21);
		else if(editMode.equals("collision")){
			g.drawString("Adjust width: <[Q] [W]>", container.getWidth()-260, 21);
			g.drawString("Adjust height: <[A] [S]>", container.getWidth()-260, 36);
		}
		else if(editMode.equals("movingSawLimitMin")){
			g.drawString("Select axis: <[Q] [W]>", container.getWidth()-260, 21);
		}
		else if(editMode.equals("movingSawSize")){
			g.drawString("Set size: <[Q] [W]>", container.getWidth()-260, 21);
			g.drawString("Set direction: <[A] [S]>", container.getWidth()-260, 36);
			g.drawString("Current direction:" + editDirection, container.getWidth()-260, 51);
		}
		g.drawString("Stop moving saws: [X]",container.getWidth()-200, container.getHeight()-81);
		g.drawString("Add item: [SPACE]",container.getWidth()-200, container.getHeight()-66);
		g.drawString("Switch modes: [1]-[6]",container.getWidth()-200, container.getHeight()-51);
		g.drawString("Delete item: [Z]",container.getWidth()-200, container.getHeight()-36);
		g.drawString("Save level: [ENTER]",container.getWidth()-200, container.getHeight()-21);
	}
	public void saveLevel(String loadLevelName, ArrayList<Tile> tiles, ArrayList<Rectangle> collisions, ArrayList<Checkpoint> checkpoints, ArrayList<StaticSaw> staticSaws, ArrayList<MovingSaw> movingSaws) throws IOException{
		PrintWriter pw = new PrintWriter(new FileWriter("levels/"+loadLevelName));
		pw.write("player=sprites/player.png,"+editPlayerX+","+editPlayerY+"\r\n");
		for(Tile tmp : tiles)
			pw.write("tile="+(int)tmp.getX()+","+(int)tmp.getY()+",32,32,"+(int)tmp.getSheetX()+","+(int)tmp.getSheetY()+"\r\n");
		for(Rectangle tmp : collisions)
			pw.write("collision="+(int)tmp.getX()+","+(int)tmp.getY()+","+(int)tmp.getWidth()+","+(int)tmp.getHeight()+"\r\n");
		for(Checkpoint tmp : checkpoints)
			pw.write("checkpoint="+(int)tmp.getX()+","+(int)tmp.getY()+"\r\n");
		for(StaticSaw tmp : staticSaws)
			pw.write("staticSaw=sprites/saw.png,"+(int)tmp.getX()+","+(int)tmp.getY()+",32,32,16"+"\r\n");
		for(MovingSaw tmp : movingSaws)
			pw.write("movingSaw="+tmp.getImagePath()+","+(int)tmp.getInitX()+","+(int)tmp.getInitY()+","+(int)tmp.getW()+","+(int)tmp.getH()+","+(int)tmp.getRadius()+","+(int)tmp.getLimitMin()+","+(int)tmp.getLimitMax()+","+(int)tmp.getInitDirection()+","+tmp.getAxis()+"\r\n");
		pw.close();
	}
}
