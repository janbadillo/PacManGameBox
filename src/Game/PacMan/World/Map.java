package Game.PacMan.World;

import Game.GameStates.PacManState;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.GhostSpawner;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Dynamics.ScoreImage;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.util.ArrayList;

public class Map {

    ArrayList<BaseStatic> blocksOnMap;
    ArrayList<BaseDynamic> enemiesOnMap;
    Handler handler;
    PacManState gamestate;
    private double bottomBorder;
    private int pixelmultiplier = MapBuilder.getPixelMultiplier();

    public Map(Handler handler) {
        this.handler=handler;
        this.blocksOnMap = new ArrayList<>();
        this.enemiesOnMap = new ArrayList<>();
        bottomBorder = handler.getHeight(); 
    }

    public void addBlock(BaseStatic block){
        blocksOnMap.add(block);
    }

    public void addEnemy(BaseDynamic entity){
        enemiesOnMap.add(entity);
    }
    
    float opacity = 1;
    boolean increase = false;
    public void drawMap(Graphics2D g2) {
    	
    	if (increase) { // opacity for BigDot will loop increase and decrease 0.1 between 1 and 0
			opacity += 0.05;
			if (opacity >= 0.95)
				increase = false;
		} else {
			opacity -= 0.05;
			if (opacity <= 0.05)
				increase = true;
		}
    	
        for (BaseStatic block:blocksOnMap) {
        	if (block instanceof BigDot) {
        		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        		g2.drawImage(block.sprite, block.x, block.y, block.width, block.height, null);
        		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        	}else {
            g2.drawImage(block.sprite, block.x, block.y, block.width, block.height, null);
        	}
        }
        for (BaseDynamic entity:enemiesOnMap) {
        	if (entity instanceof GhostSpawner) {
        		g2.drawImage(entity.sprite, entity.x, entity.y, entity.width, entity.height, null);
        		
        	} else if (entity instanceof PacMan) {
        		int posX = Images.map1.getWidth()*pixelmultiplier + pixelmultiplier*2;
        		for(int i = 0; i < handler.getPacman().getLives(); i++) {
        			g2.drawImage(Images.pacmanRight[0], posX, Images.map1.getHeight()*pixelmultiplier - pixelmultiplier*3, pixelmultiplier*2, pixelmultiplier*2 , null);
        			posX += pixelmultiplier*3;
        		}
        		if(((PacMan) entity).invinsible == true) {
        			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        		}	  		
            	if(entity.ded) {
            		if (((PacMan) entity).deathAnim.end) {
            			g2.drawImage(((PacMan) entity).deathAnim.getLastFrame(), entity.x, entity.y, entity.width, entity.height, null);
            		}else {
            			g2.drawImage(((PacMan) entity).deathAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
            		}
            	} else {
	                switch (((PacMan) entity).facing){
	                    case "Right":
	                        g2.drawImage(((PacMan) entity).rightAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
	                        break;
	                    case "Left":
	                        g2.drawImage(((PacMan) entity).leftAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
	                        break;
	                    case "Up":
	                        g2.drawImage(((PacMan) entity).upAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
	                        break;
	                    case "Down":
	                        g2.drawImage(((PacMan) entity).downAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
	                        break;
	                }
            	}
            	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            	
            } else if (entity instanceof Ghost) {
            	if(entity.ded) {
            		g2.drawImage(Images.deadEyesUp, entity.x, entity.y, entity.width, entity.height, null);
            	} else if(((Ghost) entity).vulnerable) {
            		if (((Ghost) entity).vulnerableTime <= 60*3) {
            			g2.drawImage(((Ghost) entity).deadWhiteAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
            		} else {
            			g2.drawImage(((Ghost) entity).deadBlueAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
            		}
            		
            	} else {
            		
	                switch (((Ghost) entity).facing) {
		                case 1: // Right
		                    g2.drawImage(((Ghost) entity).rightAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
		                    break;
		                case 3: // Left
		                    g2.drawImage(((Ghost) entity).leftAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
		                    break;
		                case 0: // Up
		                    g2.drawImage(((Ghost) entity).upAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
		                    break;
		                case 2: // Down
		                    g2.drawImage(((Ghost) entity).downAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
		                    break;
	                }
            	}
            } else if(entity instanceof ScoreImage) {
            	if (((ScoreImage) entity).type == 0) {
            		g2.drawImage(Images.scoreSprite[0], entity.x, entity.y, entity.width, entity.height, null);
            	} else if (((ScoreImage) entity).type == 1) {
            		g2.drawImage(Images.scoreSprite[1], entity.x, entity.y, entity.width, entity.height, null);
            	}
            	 
            }
        	
            else {
                g2.drawImage(entity.sprite, entity.x, entity.y, entity.width, entity.height, null);
            }
        }

    }

    public ArrayList<BaseStatic> getBlocksOnMap() {
        return blocksOnMap;
    }

    public ArrayList<BaseDynamic> getEnemiesOnMap() {
        return enemiesOnMap;
    }

    public double getBottomBorder() {
        return bottomBorder;
    }

    public void reset() {
    }
}
