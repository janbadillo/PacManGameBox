package Game.PacMan.entities.Dynamics;

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
    public boolean moving = true, turnFlag = false, turning = false, gamestart = true, dead = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim;
    int turnCooldown = 20, turnDuration = 0;
    int row, col;
    BaseStatic towardsBlock;


    public PacMan(int x, int y, int width, int height, Handler handler, int col, int row) {
        super(x, y, width, height, handler, Images.pacmanRight[0]);
        leftAnim = new Animation(128,Images.pacmanLeft);
        rightAnim = new Animation(128,Images.pacmanRight);
        upAnim = new Animation(128,Images.pacmanUp);
        downAnim = new Animation(128,Images.pacmanDown);
        this.row = row;
        this.col = col;
    }

    @Override
    public void tick(){
    	if (gamestart) {
    		setTowardsBlock(facing);
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
        switch (facing){
            case "Right":
            	y = towardsBlock.y;
            	if (this.x < towardsBlock.x) {
	                x += speed;
	                rightAnim.tick();
            	} else {
            		x = towardsBlock.x;
            		setNewPosition();
            		if (preTurn != null && canTurn()) {
            			facing = preTurn;
            		}
            		setTowardsBlock(facing);
            	}
                break;
            case "Left":
            	y = towardsBlock.y;
            	if (this.x > towardsBlock.x) {
	                x-=speed;
	                leftAnim.tick();
            	} else {
            		x = towardsBlock.x;
            		setNewPosition();
            		if (preTurn != null && canTurn()) {
            			facing = preTurn;
            		}
            		setTowardsBlock(facing);
            	}
                break;
            case "Up":
            	x = towardsBlock.x;
            	if (this.y > towardsBlock.y) {
	                y-=speed;
	                upAnim.tick();
            	} else {
            		y = towardsBlock.y;
            		setNewPosition();
            		if (preTurn != null && canTurn()) {
            			facing = preTurn;
            		}
            		setTowardsBlock(facing);
            	}
                break;
            case "Down":
            	x = towardsBlock.x;
            	if (this.y < towardsBlock.y) {
	                y+=speed;
	                downAnim.tick();
            	} else {
            		y = towardsBlock.y;
            		setNewPosition();
            		if (preTurn != null && canTurn()) {
            			facing = preTurn;
            		}
            		setTowardsBlock(facing);
            	}
                break;
        }

        
        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_D)) && !turnFlag){
        	if (facing == "Left") {
        		facing = "Right";
        		setTowardsBlock(facing);
        	} else {
        	preTurn = "Right";
        	turnReset();
        	}
        }
        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_A)) && !turnFlag){	
        	if (facing == "Right") {
        		facing = "Left";
        		setTowardsBlock(facing);
        	}else {
        	preTurn = "Left";
        	turnReset();
        	}
        }
        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)) && !turnFlag){	
        	if (facing == "Down") {
        		facing = "Up";
        		setTowardsBlock(facing);
        	}else {
        	preTurn = "Up";
        	turnReset();
        	}
        }
        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) && !turnFlag){	
        	if (facing == "Up") {
        		facing = "Down";
        		setTowardsBlock(facing);
        	}else {
        	preTurn = "Down";
        	turnReset();
        	}
        }
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
	    		if (preTurn == "Up" && bloku.getRow() == towardsBlock.getRow() - 1 && bloku.getCol() == towardsBlock.getCol()) {
	    			return false; // cannot turn Up
	    		} else if (preTurn == "Down" && bloku.getRow() == towardsBlock.getRow() + 1 && bloku.getCol() == towardsBlock.getCol()) {
	    			return false; // cannot turn Down
	    		} else if (preTurn == "Left" && bloku.getRow() == towardsBlock.getRow() && bloku.getCol() == towardsBlock.getCol() - 1) {
	    			return false; // cannot turn Left
	    		} else if (preTurn == "Right" && bloku.getRow() == towardsBlock.getRow() && bloku.getCol() == towardsBlock.getCol() + 1) {
	    			return false; // cannot turn Right
	    		} 
    		}
    	}
    	return true;
    }
    
    public void setTowardsBlock(String direction) {
    	for (BaseStatic bloku: handler.getMap().getBlocksOnMap()) {
	    	switch (direction){
		        case "Right":
	        		if (bloku.getRow() == this.row && bloku.getCol() == this.col + 1 && !(bloku instanceof BoundBlock)) {
	        			towardsBlock = bloku;
	        		}
		            break;
		        case "Left":
		        	if (bloku.getRow() == this.row && bloku.getCol() == this.col - 1 && !(bloku instanceof BoundBlock)) {
	        			towardsBlock = bloku;
	        		}
		            break;
		        case "Up":
		        	if (bloku.getRow() == this.row - 1 && bloku.getCol() == this.col && !(bloku instanceof BoundBlock)) {
	        			towardsBlock = bloku;
	        		}
		            break;
		        case "Down":
		        	if (bloku.getRow() == this.row + 1 && bloku.getCol() == this.col && !(bloku instanceof BoundBlock)) {
	        			towardsBlock = bloku;
	        		}
		            break;
    		}
    	}
    }
    
    public void setNewPosition() {
    	this.row = towardsBlock.getRow();
    	this.col = towardsBlock.getCol();
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }


}
