package Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;


public class TestClass extends BasicGame {
	
	private ArrayList<Rectangle> collisions = new ArrayList<Rectangle>();
	private ArrayList<StaticSaw> staticSaws = new ArrayList<StaticSaw>();
	private ArrayList<MovingSaw> movingSaws = new ArrayList<MovingSaw>();
	private ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private ArrayList<String> levels = new ArrayList<String>();
	
	private Player me = null;
	private Camera camera = null;
	private ReadLevelData reader = null;
	private Image background = null;
	private Image dead = null;
	private Image diskette = null;
	private Image menu = null;
	private SpriteSheet sprites = null;
	private Editor lvlEditor;
	private String newLevelName;
	private String loadLevelName;
	private int newLevelNum;
	private boolean paused = true;
	private boolean editor = false;

	private int menuX;
	private int menuY;
	private int menuLimitMin;
	private int menuLimitMax;
	private int levelIndex;
	
	public TestClass(String title) {
		super(title);
	}


	public void init(GameContainer container) throws SlickException {
		loadImages();
		initCamera(container);
		setMenuValues(container);
		getLevelList();
		newLevelNum=0;
		setNewLevelName();
	}
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		
		if(paused)
			updateMenu(input,container);
		else if(paused==false && editor==true)
			try{editorUpdate(delta, input);}
			catch (IOException e) {e.printStackTrace();}
		else if(paused==false && editor==false)//PLAY MODE
		{
			playUpdate(delta, input);
			//System.out.println("camX: " + camera.getX() + ", camY: " + camera.getY());
			//System.out.println("playerX: " + me.getX() + ", playerY: " + me.getY());
		}
	}
	public void render(GameContainer container, Graphics g) throws SlickException {
		background.draw(0,0);
		if(paused){
			drawMenu(g, container);
		}
		else if(paused==false && editor==true){//EDITOR MODE
			drawLevel();
			drawEditor(g, container);
		}
		else if(paused==false && editor==false){//PLAY MODE
			drawLevel();
			drawPlayer(g, container);
		}
	}
	
	public void updatePlayer(Input input, int delta){
		me.moveAround(input);
		me.jump(input);
		me.velYadd((double)7.0);//gravity
		me.updateCoords((double)delta/1000.0);
		me.updateCollidersX();
		me.updateCollidersY();
		me.checkCollisions(collisions);
		me.updateAfterCollisions((double)delta/1000.0);
		me.reduceVelocity();//reduce velocity x
	
		me.checkCollisionsWithSaws(staticSaws);//check for dangerous collision
		if(me.getDangerousCollision()==false){
			me.checkCollisionsWithMovingSaws(movingSaws);
		}
		me.checkCheckpoints(checkpoints);
	
		if(me.isAlive()==false){
			if(input.isKeyDown(Input.KEY_SPACE))
			{
				me.restart(camera);
			}
		}
	}
	public void updateObjects(int delta){
		for (MovingSaw tmp : movingSaws){
			tmp.updateCoords((double)delta/1000.0);
			tmp.updateCollisionCircle();
			tmp.rotate(delta);
		}
		for (StaticSaw tmp : staticSaws){
			tmp.rotate(delta);
		}
	}
	public void updateMenu(Input input, GameContainer container) throws SlickException{
		if(input.isKeyPressed(Input.KEY_UP)){
			if(menuY>menuLimitMin)
				menuY-=64;
			else
				menuY+=128;
		}
		else if(input.isKeyPressed(Input.KEY_DOWN)){
			if(menuY<menuLimitMax)
				menuY+=64;
			else
				menuY-=128;
		}
		if(input.isKeyPressed(Input.KEY_LEFT)){
			if(levelIndex>0)
				levelIndex-=1;
			else{
				if(menuY==204)//if editor not selected
					levelIndex=levels.size()-2;
				else
					levelIndex=levels.size()-1;
			}
		}
		else if(input.isKeyPressed(Input.KEY_RIGHT)){
			if(menuY==204){
				if(levelIndex<levels.size()-2)
					levelIndex+=1;
				else
					levelIndex=0;
			}
			else{
				if(levelIndex<levels.size()-1)
					levelIndex+=1;
				else
					levelIndex=0;
			}
		}
		loadLevelName=levels.get(levelIndex);
		if(levelIndex==levels.size()-1 && menuY==204)
			levelIndex=levels.size()-2;

		if(input.isKeyPressed(Input.KEY_SPACE)){
			if(menuY==204){
				me = new Player("sprites/player.png", 0, 0);
				clearLevel();
				loadLevel(loadLevelName);
				camera.reset(me,container);
				paused=false;
				editor=false;
			}
			if(menuY==268){
				me = new Player("sprites/player.png", 0, 0);
				clearLevel();
				if(levelIndex<levels.size()-1){
					//loadLevelName=levels.get(levelIndex);
					loadLevel(loadLevelName);
					
				}
				lvlEditor=new Editor(me);
				paused=false;
				editor=true;
			}
		}
	}
	public void clearLevel(){
		collisions.clear();
		tiles.clear();
		staticSaws.clear();
		movingSaws.clear();
		checkpoints.clear();
	}
	public void loadLevel(String path) throws SlickException{
		try { reader = new ReadLevelData(path, me, tiles, checkpoints, staticSaws, movingSaws, collisions); reader.readLines();}
		catch (IOException e) { e.printStackTrace(); }
	}
	public void loadImages() throws SlickException{
		background=new Image("sprites/background.png");
		sprites = new SpriteSheet("sprites/tileset.png", 32, 32);
		dead = new Image("sprites/dead.png");
		diskette = new Image("sprites/diskette.png");
		menu = new Image("sprites/menu.png");
	}
	public void setMenuValues(GameContainer container){
		menuX=container.getWidth()/8;
		menuY=container.getHeight()/2-96;
		menuLimitMin=container.getHeight()/2-96;
		menuLimitMax=container.getHeight()/2-96+128;
		levelIndex=0;
	}
	public void initCamera(GameContainer container) throws SlickException{
		camera = new Camera(container.getWidth(), container.getHeight());
	}
	public void editorUpdate(int delta, Input input) throws SlickException, IOException{
		updateObjects(delta);
		lvlEditor.selectMode(input);
		lvlEditor.moveAround(input, camera);
		lvlEditor.update(input, loadLevelName, sprites, tiles, collisions, checkpoints, staticSaws, movingSaws);
	}
	public void playUpdate(int delta, Input input){
		updateObjects(delta);
		camera.updateCoords(me, (double)delta/1000.0);
		updatePlayer(input, delta);
	}
	public void drawMenu(Graphics g, GameContainer container){
		g.setColor(Color.black);
		g.fillRect(menuX, menuY, 192, 64);
		menu.draw(container.getWidth()/8,container.getHeight()/2-96);
		drawLevelSelector(g, container);
	}
	public void drawLevel(){
		for (MovingSaw tmp : movingSaws) tmp.draw(camera);
		for (StaticSaw tmp : staticSaws) tmp.draw(camera);
		for (Checkpoint tmp : checkpoints) tmp.draw(diskette, camera);
	
		sprites.startUse();
		for(Tile tmp : tiles) tmp.draw(sprites, camera);
		sprites.endUse();
	}
	public void drawPlayer(Graphics g, GameContainer container){
		me.draw(camera);
		if(!me.isAlive()) 
			dead.draw(container.getWidth()/2-512/2,container.getHeight()/2-128/2);
		if(me.isOnCheckpoint()){
			g.setColor(Color.black);
			g.drawString("Checkpoint saved", container.getWidth()-156, 6);
		}
	}
	public void drawEditor(Graphics g, GameContainer container){
		lvlEditor.drawCollisions(camera, g, collisions);
		lvlEditor.drawModeItems(container, sprites, g, camera);
		lvlEditor.drawPlayer(camera);
		lvlEditor.drawPointer(g, camera);
		lvlEditor.drawText(container, g);
	}
	public void getLevelList(){
		File folder = new File("levels/");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles)
		    if (file.isFile()){
		    	levels.add(file.getName());
		    }
	}
	public void setNewLevelName(){
		for(int i=0; i<levels.size(); i++){
	    	if(levels.get(i).equals("LEVEL"+newLevelNum+".txt")){
	    		newLevelNum+=1;
	    		if(i!=0)
	    			i=-1;
	    	}
		}
		newLevelName="LEVEL"+newLevelNum+".txt";
		levels.add(newLevelName);
	}
	public void drawLevelSelector(Graphics g, GameContainer container){
		if(levels.get(levelIndex).equals(newLevelName))
			g.drawString("Current level: " + levels.get(levelIndex) + " [CREATE NEW]", container.getWidth()/8, container.getHeight()/2-96-32);
		else
			g.drawString("Current level: " + levels.get(levelIndex), container.getWidth()/8, container.getHeight()/2-96-32);
	}
	public static void main(String[] args) throws SlickException {

		AppGameContainer app = new AppGameContainer(new TestClass("Test"));
		app.setUpdateOnlyWhenVisible(true);
		app.setDisplayMode(800, 600, false);
		app.setAlwaysRender(true);
		app.setTargetFrameRate(120);
		//app.setVSync(true);
		app.start();
	}
}