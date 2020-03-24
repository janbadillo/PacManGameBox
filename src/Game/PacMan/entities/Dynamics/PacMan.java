package Game.PacMan.entities.Dynamics;

import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PacMan extends BaseDynamic{

    protected double speed = 2;
    public String facing = "Left", preTurn;
    public boolean moving = true, turnFlag = false, turning = false, gamestart = true, invinsible = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim,deathAnim;
    int turnCooldown = 20, turnDuration = 0, posX, posY, towardsX, towardsY, deadCounter, invinsibleTime;
    int pixelMultiplier = MapBuilder.pixelMultiplier;


    public PacMan(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.pacmanRight[0]);
        leftAnim = new Animation(128,Images.pacmanLeft);
        rightAnim = new Animation(128,Images.pacmanRight);
        upAnim = new Animation(128,Images.pacmanUp);
        downAnim = new Animation(128,Images.pacmanDown);
        deathAnim = new Animation(128,Images.pacmanDeath);
        posX = x;
        posY = y;
    }

    @Override
    public void tick(){
    	if (gamestart) {
    		setTowardsPosition(facing);
    		gamestart=false;
    	}
    	if (turnDuration > 0){
    		turnDuration--;
        }
    	if (turnDuration <= 0 && turning) {
    		turning = false;
    		preTurn = null;
    	}
        if (turnCooldown<=0){
            turnFlag= false;
        }
        if (turnFlag){
            turnCooldown--;
        }
        if (invinsibleTime<=0){
            invinsible = false;
        }
        if (invinsible){
            invinsibleTime--;
        }
        
        if (ded) {
        	if (deadCounter <= 0) {
        		invinsible = true;
        		invinsibleTime = 60*3;
        		ded = false;
        		deathAnim.reset();
        	}else {
        		deadCounter--;
        	}
        	if(!deathAnim.end) {
        		deathAnim.tick();
        	}
        } else {
	        switch (facing){
	            case "Right":
	            	y = towardsY;
	            	if (this.x < towardsX) {
		                x += speed;
		                rightAnim.tick();
	            	} else {
	            		x = towardsX;
	            		setNewPosition();
	            		if (preTurn != null && canTurn()) {
	            			facing = preTurn;
	            		}
	            		setTowardsPosition(facing);
	            	}
	                break;
	            case "Left":
	            	y = towardsY;
	            	if (this.x > towardsX) {
		                x-=speed;
		                leftAnim.tick();
	            	} else {
	            		x = towardsX;
	            		setNewPosition();
	            		if (preTurn != null && canTurn()) {
	            			facing = preTurn;
	            		}
	            		setTowardsPosition(facing);
	            	}
	                break;
	            case "Up":
	            	x = towardsX;
	            	if (this.y > towardsY) {
		                y-=speed;
		                upAnim.tick();
	            	} else {
	            		y = towardsY;
	            		setNewPosition();
	            		if (preTurn != null && canTurn()) {
	            			facing = preTurn;
	            		}
	            		setTowardsPosition(facing);
	            	}
	                break;
	            case "Down":
	            	x = towardsX;
	            	if (this.y < towardsY) {
		                y+=speed;
		                downAnim.tick();
	            	} else {
	            		y = towardsY;
	            		setNewPosition();
	            		if (preTurn != null && canTurn()) {
	            			facing = preTurn;
	            		}
	            		setTowardsPosition(facing);
	            	}
	                break;
	        }
        }

        
        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_D)) && !turnFlag){
        	if (facing == "Left") {
        		facing = "Right";
        		setTowardsPosition(facing);
        	} else {
        	preTurn = "Right";
        	turnReset();
        	}
        }
        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_A)) && !turnFlag){	
        	if (facing == "Right") {
        		facing = "Left";
        		setTowardsPosition(facing);
        	}else {
        	preTurn = "Left";
        	turnReset();
        	}
        }
        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)) && !turnFlag){	
        	if (facing == "Down") {
        		facing = "Up";
        		setTowardsPosition(facing);
        	}else {
        	preTurn = "Up";
        	turnReset();
        	}
        }
        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) && !turnFlag){	
        	if (facing == "Up") {
        		facing = "Down";
        		setTowardsPosition(facing);
        	}else {
        	preTurn = "Down";
        	turnReset();
        	}
        }
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)){	
        	die();
        }
    }
    
    public void die() {
    	ded = true;
    	deadCounter = 60*3;
    }
    
    public void turnReset() {
    	turning = true;
    	turnDuration = 20;
    	turnFlag = true;
        turnCooldown = 5;
    }
    
    public boolean canTurn() {
    	for (BaseStatic bloku: handler.getMap().getBlocksOnMap()) {
    		if (bloku instanceof BoundBlock) {
	    		if (preTurn == "Up" && bloku.y == towardsY - pixelMultiplier && bloku.x == towardsX) {
	    			return false; // cannot turn Up
	    		} else if (preTurn == "Down" && bloku.y == towardsY + pixelMultiplier && bloku.x == towardsX) {
	    			return false; // cannot turn Down
	    		} else if (preTurn == "Left" && bloku.y == towardsY && bloku.x == towardsX - pixelMultiplier) {
	    			return false; // cannot turn Left
	    		} else if (preTurn == "Right" && bloku.y == towardsY && bloku.x == towardsX + pixelMultiplier) {
	    			return false; // cannot turn Right
	    		} 
    		}
    	}
    	return true;
    }
    
    public void setTowardsPosition(String direction) {
    	int testX = posX;
    	int testY = posY;
    	boolean testpass = true;
    	switch (direction){
	        case "Right":
	        	testX += pixelMultiplier;
	            break;
	        case "Left":
	        	testX -= pixelMultiplier;
	            break;
	        case "Up":
	        	testY -= pixelMultiplier;
	            break;
	        case "Down":
	        	testY += pixelMultiplier;
	            break;
    	}
    	 
    	for (BaseStatic bloku: handler.getMap().getBlocksOnMap()) {
    		if (bloku instanceof BoundBlock) {
		    	if (testX == bloku.getX() && testY == bloku.getY()) {
		    		testpass = false;
		    	}
    		}
    	}
    	if (testpass) {
    		towardsX = testX;
    		towardsY = testY;
    	}
    }
    
    public void setNewPosition() {
    	posX = towardsX;
    	posY = towardsY;
    }




}
