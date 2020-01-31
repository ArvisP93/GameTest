package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class ReadLevelData {
	private String filepath;
	private File file=null;
	private FileReader filereader=null;
	private BufferedReader bufferedreader=null;
	private ArrayList<Checkpoint> checkpoints = null;
	private ArrayList<StaticSaw> staticSaws = null;
	private ArrayList<MovingSaw> movingSaws = null;
	private ArrayList<Rectangle> collisions = null;
	private ArrayList<Tile> tiles = null;
	private Player player=null;
	private String line;
	
	public ReadLevelData(String filename, Player player, ArrayList<Tile> tiles, ArrayList<Checkpoint> checkpoints, ArrayList<StaticSaw> staticSaws, ArrayList<MovingSaw> movingSaws, ArrayList<Rectangle> collisions) throws SlickException, FileNotFoundException
	{
		this.filepath = "levels/" + filename;
		this.file = new File(this.filepath);
		this.filereader = new FileReader(this.file);
		this.bufferedreader = new BufferedReader(this.filereader);
		this.checkpoints=checkpoints;
		this.staticSaws=staticSaws;
		this.movingSaws=movingSaws;
		this.collisions=collisions;
		this.player=player;
		this.tiles=tiles;
		//this.stringbuffer = new StringBuffer();
		
	}

	public void readLines() throws IOException, NumberFormatException, SlickException{
		while ((line = bufferedreader.readLine()) != null) {
			 String[] linedata = line.split("=");
			 String[] info = linedata[1].split(","); 

			 if(linedata[0].equals("player")){
				 player.setX(Integer.parseInt(info[1]));
				 player.setY(Integer.parseInt(info[2]));
				 player.setCheckpointXY(player.getX(), player.getY()-2);
				 player.setIsPlaced(true);
			 }
			 else if(linedata[0].equals("checkpoint")){
				 checkpoints.add( new Checkpoint(Integer.parseInt(info[0]), Integer.parseInt(info[1])) );
			 }
			 else if(linedata[0].equals("staticSaw")){
				 staticSaws.add( new StaticSaw(info[0], Double.parseDouble(info[1]),Double.parseDouble(info[2]),Integer.parseInt(info[3]),Integer.parseInt(info[4]),Integer.parseInt(info[5])));
			 }
			 else if(linedata[0].equals("movingSaw")){
				 movingSaws.add( new MovingSaw(info[0], Double.parseDouble(info[1]),Double.parseDouble(info[2]),Integer.parseInt(info[3]),Integer.parseInt(info[4]),Integer.parseInt(info[5]),Integer.parseInt(info[6]),Integer.parseInt(info[7]),Integer.parseInt(info[8]),info[9]));
			 }
			 else if(linedata[0].equals("collision")){
				 collisions.add( new Rectangle(Integer.parseInt(info[0]),Integer.parseInt(info[1]),Integer.parseInt(info[2]),Integer.parseInt(info[3])));
			 }
			 else if(linedata[0].equals("tile")){
				 tiles.add(new Tile(Integer.parseInt(info[0]),Integer.parseInt(info[1]),Integer.parseInt(info[2]),Integer.parseInt(info[3]),Integer.parseInt(info[4]),Integer.parseInt(info[5])));
			 }
		}
		filereader.close();
		
		//System.out.println("Contents of file:");
		//System.out.println(stringbuffer.toString());
	}

}
