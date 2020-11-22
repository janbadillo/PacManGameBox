package Game.PacMan.World;

import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.GhostSpawner;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.BoundBlock;
import Game.PacMan.entities.Statics.Dot;
import Game.PacMan.entities.Statics.Fruit;
import Game.PacMan.entities.Statics.SpawnGate;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MapBuilder {

	public static int pixelMultiplier = 17;  //change this for size of blocks  (original is 18)
	public static int boundBlock = new Color(0,0,0).getRGB();
	public static int pacman = new Color(255, 255,0).getRGB();
	public static int ghostC = new Color(25, 255,0).getRGB();
	public static int dotC = new Color(255, 10, 0).getRGB();
	public static int bigDotC = new Color(167, 0, 150).getRGB();
	public static int spawnGate = new Color(0, 0, 255).getRGB();
	static Random random = new Random();
	public static int fruitType = 0;
	public static Map createMap(BufferedImage mapImage, Handler handler){
		Map mapInCreation = new Map(handler);
		for (int i = 0; i < mapImage.getWidth(); i++) {
			for (int j = 0; j < mapImage.getHeight(); j++) {
				int currentPixel = mapImage.getRGB(i, j);
				int xPos = i*pixelMultiplier;
				int yPos = j*pixelMultiplier;

				
				if(currentPixel == boundBlock){
					BaseStatic BoundBlock = new BoundBlock(xPos,yPos,pixelMultiplier,pixelMultiplier,handler,getSprite(mapImage,i,j));
					mapInCreation.addBlock(BoundBlock);
				}else if(currentPixel == pacman){
					BaseDynamic PacMan = new PacMan(xPos,yPos,pixelMultiplier,pixelMultiplier,handler);
					mapInCreation.addEnemy(PacMan);
					handler.setPacman((Game.PacMan.entities.Dynamics.PacMan) PacMan);
				}else if(currentPixel == ghostC){
					BaseDynamic ghostspawn = new GhostSpawner(xPos,yPos,pixelMultiplier,pixelMultiplier,handler);
					mapInCreation.addEnemy(ghostspawn);
				}else if(currentPixel == spawnGate){
					BaseStatic spwngate = new SpawnGate(xPos,yPos,pixelMultiplier,pixelMultiplier,handler, Images.spawngate);
					mapInCreation.addBlock(spwngate);
				}
				else if(currentPixel == dotC){
					int fruitchance = random.nextInt(30);
					BaseStatic dot;
					if (fruitchance == 0) {
						dot = new Fruit(xPos,yPos,pixelMultiplier,pixelMultiplier,handler, fruitType);
						if (fruitType >= 6) {
							fruitType = 0;
						} else {
							fruitType ++;
						}
					}else {
						dot = new Dot(xPos,yPos,pixelMultiplier,pixelMultiplier,handler);
					}
					mapInCreation.addBlock(dot);
				}else if(currentPixel == bigDotC){
					BaseStatic bigDot = new BigDot(xPos,yPos,pixelMultiplier,pixelMultiplier,handler);
					mapInCreation.addBlock(bigDot);
				}
			}

		}
		return mapInCreation;
	}

	private static BufferedImage getSprite(BufferedImage mapImage, int i, int j) {
		int currentPixel = boundBlock;
		int leftPixel;
		int rightPixel;
		int upPixel;
		int downPixel;
		if (i>0) {
			 leftPixel = mapImage.getRGB(i - 1, j);
		}else{
			 leftPixel = pacman;
		}
		if (i<mapImage.getWidth()-1) {
			 rightPixel = mapImage.getRGB(i + 1, j);
		}else{
			 rightPixel= pacman;

		}
		if (j>0) {
			 upPixel = mapImage.getRGB(i, j - 1);
		}else{
			 upPixel = pacman;

		}
		if (j<mapImage.getHeight()-1) {
			 downPixel = mapImage.getRGB(i, j + 1);
		}else{
			 downPixel = pacman;
		}

		if (currentPixel != leftPixel && currentPixel != upPixel && currentPixel != downPixel && currentPixel == rightPixel){

			return Images.bound[1];
		}else if (currentPixel != leftPixel && currentPixel != upPixel && currentPixel == downPixel && currentPixel != rightPixel){

			return Images.bound[2];
		}else if (currentPixel == leftPixel && currentPixel != upPixel && currentPixel != downPixel && currentPixel != rightPixel){

			return Images.bound[3];
		}else if (currentPixel != leftPixel && currentPixel == upPixel && currentPixel != downPixel && currentPixel != rightPixel){

			return Images.bound[4];
		}else if (currentPixel != leftPixel && currentPixel == upPixel && currentPixel == downPixel && currentPixel != rightPixel){

			return Images.bound[5];
		}else if (currentPixel == leftPixel && currentPixel != upPixel && currentPixel != downPixel && currentPixel == rightPixel){

			return Images.bound[6];
		}else if (currentPixel != leftPixel && currentPixel == upPixel && currentPixel != downPixel && currentPixel == rightPixel){

			return Images.bound[7];
		}else if (currentPixel == leftPixel && currentPixel == upPixel && currentPixel != downPixel && currentPixel != rightPixel){

			return Images.bound[8];
		}else if (currentPixel != leftPixel && currentPixel != upPixel && currentPixel == downPixel && currentPixel == rightPixel){

			return Images.bound[9];
		}else if (currentPixel == leftPixel && currentPixel != upPixel && currentPixel == downPixel && currentPixel != rightPixel){

			return Images.bound[10];
		}else if (currentPixel == leftPixel && currentPixel == upPixel && currentPixel == downPixel && currentPixel == rightPixel){

			return  Images.bound[11];
		}else if (currentPixel != leftPixel && currentPixel == upPixel && currentPixel == downPixel && currentPixel == rightPixel){

			return Images.bound[12];
		}else if (currentPixel == leftPixel && currentPixel == upPixel && currentPixel == downPixel && currentPixel != rightPixel){

			return Images.bound[13];
		}else if (currentPixel == leftPixel && currentPixel != upPixel && currentPixel == downPixel && currentPixel == rightPixel){

			return Images.bound[14];
		}else if (currentPixel == leftPixel && currentPixel == upPixel && currentPixel != downPixel && currentPixel == rightPixel){

			return Images.bound[15];
		}else{

			return  Images.bound[0];
		}


	}
	
	public static int getPixelMultiplier() {
		return pixelMultiplier;
	}


}
