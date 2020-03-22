package Game.PacMan.entities.Dynamics;

import Game.GameStates.PacManState;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Ghost extends BaseDynamic{

    protected double speed = 2;
    public int facing = 3, chanceToTurn, vulnerableTime;//0 is Up, 1 is Right, 2 is Down, 3 is Left
    public boolean gamestart = true, vulnerable = false, dead = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim,deadBlueAnim,deadWhiteAnim;
    int turnCooldown = 20, turnDuration = 0;
    int row, col;
    BaseStatic towardsBlock;
    ArrayList<Integer> availableDirections = new ArrayList<>();
    Random random = new Random();
    PacManState gamestate;


    public Ghost(int x, int y, int width, int height, Handler handler, int col, int row, int color) {
        super(x, y, width, height, handler, Images.ghost);
        // for color: 0 is red, 1 is cyan, 2 is pink, 3 is tan
        deadBlueAnim = new Animation(128,Images.deadBlue);
        deadWhiteAnim = new Animation(128,Images.deadWhite);
        if (color == 0) {
	        leftAnim = new Animation(128,Images.redLeft);
	        rightAnim = new Animation(128,Images.redRight);
	        upAnim = new Animation(128,Images.redUp);
	        downAnim = new Animation(128,Images.redDown);
        } else if (color == 1) {
        	leftAnim = new Animation(128,Images.cyanLeft);
	        rightAnim = new Animation(128,Images.cyanRight);
	        upAnim = new Animation(128,Images.cyanUp);
	        downAnim = new Animation(128,Images.cyanDown);
        } else if (color == 2) {
        	leftAnim = new Animation(128,Images.pinkLeft);
	        rightAnim = new Animation(128,Images.pinkRight);
	        upAnim = new Animation(128,Images.pinkUp);
	        downAnim = new Animation(128,Images.pinkDown);
        } else if (color == 3) {
        	leftAnim = new Animation(128,Images.tanLeft);
	        rightAnim = new Animation(128,Images.tanRight);
	        upAnim = new Animation(128,Images.tanUp);
	        downAnim = new Animation(128,Images.tanDown);
        }
        this.row = row;
        this.col = col;
    }

    @Override
    public void tick(){
    	if (gamestart) {
    		setTowardsBlock(facing);
    		gamestart=false;
    	}
    	if(vulnerableTime >= 0) {
    		vulnerableTime--;
    	}
    	if (vulnerable) {
    		if (vulnerableTime <= 0) {
    			resetVulnerableTime();
    		}
    		if(vulnerableTime <= 60*3) {
    			deadWhiteAnim.tick();
    		} else {
    			deadBlueAnim.tick();
    		}
    			
    	}
        switch (facing){
            case 1: //Right
            	y = towardsBlock.y;
            	if (this.x < towardsBlock.x) {
	                x += speed;
	                if (!vulnerable) {
	                	rightAnim.tick();
	                }
            	} else {
            		x = towardsBlock.x;
            		setNewPosition();
            		setAvailableTurns();
            		int chanceToTurn = random.nextInt(4); // 1/4 chance of turning
            		if (availableDirections.size() == 1) {
            			facing = availableDirections.get(0);
            		}else if (availableDirections.size() != 0 && chanceToTurn == 0) {
            			int newFacing = random.nextInt(availableDirections.size());
            			facing = availableDirections.get(newFacing);
            		}
            		setTowardsBlock(facing);
            	}
                break;
            case 3: // Left
            	y = towardsBlock.y;
            	if (this.x > towardsBlock.x) {
	                x-=speed;
	                if (!vulnerable) {
	                	leftAnim.tick();
	                }
            	} else {
            		x = towardsBlock.x;
            		setNewPosition();
            		setAvailableTurns();
            		int chanceToTurn = random.nextInt(4); // 1/4 chance of turning
            		if (availableDirections.size() == 1) {
            			facing = availableDirections.get(0);
            		}else if (availableDirections.size() != 0 && chanceToTurn == 0) {
            			int newFacing = random.nextInt(availableDirections.size());
            			facing = availableDirections.get(newFacing);
            		}
            		setTowardsBlock(facing);
            	}
                break;
            case 0: //Up
            	x = towardsBlock.x;
            	if (this.y > towardsBlock.y) {
	                y-=speed;
	                if (!vulnerable) {
	                	upAnim.tick();
	                }
            	} else {
            		y = towardsBlock.y;
            		setNewPosition();
            		setAvailableTurns();
            		int chanceToTurn = random.nextInt(4); // 1/4 chance of turning
            		if (availableDirections.size() == 1) {
            			facing = availableDirections.get(0);
            		}else if (availableDirections.size() != 0 && chanceToTurn == 0) {
            			int newFacing = random.nextInt(availableDirections.size());
            			facing = availableDirections.get(newFacing);
            		}
            		setTowardsBlock(facing);
            	}
                break;
            case 2: //Down
            	x = towardsBlock.x;
            	if (this.y < towardsBlock.y) {
	                y+=speed;
	                if (!vulnerable) {
	                	downAnim.tick();
	                }
            	} else {
            		y = towardsBlock.y;
            		setNewPosition();
            		setAvailableTurns();
            		int chanceToTurn = random.nextInt(4); // 1/4 chance of turning
            		if (availableDirections.size() == 1) {
            			facing = availableDirections.get(0);
            		}else if (availableDirections.size() != 0 && chanceToTurn == 0) {
            			int newFacing = random.nextInt(availableDirections.size());
            			facing = availableDirections.get(newFacing);
            		}
            		setTowardsBlock(facing);
            	}
                break;
        }
    }
    
    public void resetVulnerableTime() {
    	vulnerableTime = 60*7;
    }
    public int getVulnerableTime() {
    	return vulnerableTime;
    }
    
    public void setVulnerability(boolean a) {
    	vulnerable = a;
    }
    public boolean getVulnerability() {
    	return vulnerable;
    }
    
    public void setAvailableTurns() {
    	availableDirections.clear();
    	for (BaseStatic bloku: handler.getMap().getBlocksOnMap()) {
    		if (!(bloku instanceof BoundBlock)) {
	    		if (bloku.getRow() == this.row && bloku.getCol() == this.col - 1 && facing != 1) {
	    			availableDirections.add(3);
	    		}
	    		if (bloku.getRow() == this.row && bloku.getCol() == this.col + 1 && facing != 3) {
	    			availableDirections.add(1);
	    		}
	    		if (bloku.getRow() == this.row - 1 && bloku.getCol() == this.col && facing != 2) {
	    			availableDirections.add(0);
	    		}
	    		if (bloku.getRow() == this.row + 1 && bloku.getCol() == this.col && facing != 0) {
	    			availableDirections.add(2);
	    		}
    		}
    	}
    }
    
    public void setTowardsBlock(int direction) {
    	for (BaseStatic bloku: handler.getMap().getBlocksOnMap()) {
	    	switch (direction){
		        case 1: // Right
	        		if (bloku.getRow() == this.row && bloku.getCol() == this.col + 1 && !(bloku instanceof BoundBlock)) {
	        			towardsBlock = bloku;
	        		}
		            break;
		        case 3: // Left
		        	if (bloku.getRow() == this.row && bloku.getCol() == this.col - 1 && !(bloku instanceof BoundBlock)) {
	        			towardsBlock = bloku;
	        		}
		            break;
		        case 0: // Up
		        	if (bloku.getRow() == this.row - 1 && bloku.getCol() == this.col && !(bloku instanceof BoundBlock)) {
	        			towardsBlock = bloku;
	        		}
		            break;
		        case 2: // Down
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
