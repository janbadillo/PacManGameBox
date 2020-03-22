package Game.PacMan.World;

import Game.GameStates.PacManState;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Main.Handler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Map {

    ArrayList<BaseStatic> blocksOnMap;
    ArrayList<BaseDynamic> enemiesOnMap;
    Handler handler;
    PacManState gamestate;
    private double bottomBorder;
    private Random rand;
    private int mapBackground;

    public Map(Handler handler) {
        this.handler=handler;
        this.rand = new Random();
        this.blocksOnMap = new ArrayList<>();
        this.enemiesOnMap = new ArrayList<>();
        bottomBorder=handler.getHeight();
        this.mapBackground = this.rand.nextInt(6);
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
            if (entity instanceof PacMan) {
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
            } else if (entity instanceof Ghost) {
            	if(((Ghost) entity).getVulnerability()) {
            		if (((Ghost) entity).getVulnerableTime() <= 60*3) {
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
