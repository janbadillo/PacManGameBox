package Game.PacMan.entities.Dynamics;

import Game.GameStates.PacManState;
import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.util.ArrayList;
import java.util.Random;

public class Ghost extends BaseDynamic{

    protected double speed = 2;
    public int facing = 3, chanceToTurn, vulnerableTime, ghostColor; //0 is Up, 1 is Right, 2 is Down, 3 is Left
    public boolean gamestart = true, vulnerable = false, dead = false, toRespawn = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim,deadBlueAnim,deadWhiteAnim;
    private int mainSpeed;
    int posX, posY, towardsX, towardsY, spawnX, spawnY;
    int pixelMultiplier = MapBuilder.pixelMultiplier;
    ArrayList<Integer> availableDirections = new ArrayList<>();
    Random random = new Random();
    PacManState gamestate;


    public Ghost(int x, int y, int width, int height, Handler handler, int color) {
        super(x, y, width, height, handler, Images.ghost);
        deadBlueAnim = new Animation(128,Images.deadBlue);
        deadWhiteAnim = new Animation(128,Images.deadWhite);
        if (color == 0) { // red
        	mainSpeed = 4;
	        leftAnim = new Animation(128,Images.redLeft);
	        rightAnim = new Animation(128,Images.redRight);
	        upAnim = new Animation(128,Images.redUp);
	        downAnim = new Animation(128,Images.redDown);
        } else if (color == 1) { // cyan
        	mainSpeed = 3;
        	leftAnim = new Animation(128,Images.cyanLeft);
	        rightAnim = new Animation(128,Images.cyanRight);
	        upAnim = new Animation(128,Images.cyanUp);
	        downAnim = new Animation(128,Images.cyanDown);
        } else if (color == 2) { // pink
        	mainSpeed = 2;
        	leftAnim = new Animation(128,Images.pinkLeft);
	        rightAnim = new Animation(128,Images.pinkRight);
	        upAnim = new Animation(128,Images.pinkUp);
	        downAnim = new Animation(128,Images.pinkDown);
        } else if (color == 3) { // tan
        	mainSpeed = 1;
        	leftAnim = new Animation(128,Images.tanLeft);
	        rightAnim = new Animation(128,Images.tanRight);
	        upAnim = new Animation(128,Images.tanUp);
	        downAnim = new Animation(128,Images.tanDown);
        }
        spawnX = x;
        spawnY = y;
        posX = x;
        posY = y;
    }

    @Override
    public void tick(){
    	if (gamestart) {
    		setTowardsPosition(facing);
    		gamestart=false;
    	}
    	if(vulnerableTime >= 0) {
    		vulnerableTime--;
    	} else if(vulnerableTime <= 0) {
    		vulnerable = false;
    	}
    	if (vulnerable) {
    		speed = 1;
    		if(vulnerableTime <= 60*3) {
    			deadWhiteAnim.tick();
    		} else {
    			deadBlueAnim.tick();
    		}
    			
    	}else {
    		speed = mainSpeed;
    	}
    	if(ded) {
            if ((spawnY + 8 < y || y < spawnY - 8) || (spawnX + 8 < x || x < spawnX - 8)) { // if not centered
                if (y > spawnY) {
                    y -= 2;
                } else {
                    y += 2;
                }
                if (x > spawnX) {
                    x -= 2;
                } else {
                    x += 2;
                }
            } else {
            	y = spawnY;
            	x = spawnX;
            	posY = y;
            	posX = x;
            	setTowardsPosition(facing);
            	vulnerable = false;
            	toRespawn = true;
            }

    	}else {
	        switch (facing){
	            case 1: //Right
	            	y = towardsY;
	            	if (this.x < towardsX) {
		                x += speed;
		                if (!vulnerable) {
		                	rightAnim.tick();
		                }
	            	} else {
	            		x = towardsX;
	            		setNewPosition();
	            		setAvailableTurns();
	            		int chanceToTurn = random.nextInt(3); // 1/3 chance of turning
	            		if (availableDirections.size() == 1) {
	            			facing = availableDirections.get(0);
	            		}else if (availableDirections.size() != 0 && chanceToTurn == 0) {
	            			int newFacing = random.nextInt(availableDirections.size());
	            			facing = availableDirections.get(newFacing);
	            		}
	            		setTowardsPosition(facing);
	            	}
	                break;
	            case 3: // Left
	            	y = towardsY;
	            	if (this.x > towardsX) {
		                x-=speed;
		                if (!vulnerable) {
		                	leftAnim.tick();
		                }
	            	} else {
	            		x = towardsX;
	            		setNewPosition();
	            		setAvailableTurns();
	            		int chanceToTurn = random.nextInt(3); // 1/3 chance of turning
	            		if (availableDirections.size() == 1) {
	            			facing = availableDirections.get(0);
	            		}else if (availableDirections.size() != 0 && chanceToTurn == 0) {
	            			int newFacing = random.nextInt(availableDirections.size());
	            			facing = availableDirections.get(newFacing);
	            		}
	            		setTowardsPosition(facing);
	            	}
	                break;
	            case 0: //Up
	            	x = towardsX;
	            	if (this.y > towardsY) {
		                y-=speed;
		                if (!vulnerable) {
		                	upAnim.tick();
		                }
	            	} else {
	            		y = towardsY;
	            		setNewPosition();
	            		setAvailableTurns();
	            		int chanceToTurn = random.nextInt(3); // 1/3 chance of turning
	            		if (availableDirections.size() == 1) {
	            			facing = availableDirections.get(0);
	            		}else if (availableDirections.size() != 0 && chanceToTurn == 0) {
	            			int newFacing = random.nextInt(availableDirections.size());
	            			facing = availableDirections.get(newFacing);
	            		}
	            		setTowardsPosition(facing);
	            	}
	                break;
	            case 2: //Down
	            	x = towardsX;
	            	if (this.y < towardsY) {
		                y+=speed;
		                if (!vulnerable) {
		                	downAnim.tick();
		                }
	            	} else {
	            		y = towardsY;
	            		setNewPosition();
	            		setAvailableTurns();
	            		int chanceToTurn = random.nextInt(3); // 1/3 chance of turning
	            		if (availableDirections.size() == 1) {
	            			facing = availableDirections.get(0);
	            		}else if (availableDirections.size() != 0 && chanceToTurn == 0) {
	            			int newFacing = random.nextInt(availableDirections.size());
	            			facing = availableDirections.get(newFacing);
	            		}
	            		setTowardsPosition(facing);
	            	}
	                break;
	        }
    	}
    }

	  public void makeVulnerable() {
		vulnerable = true;
		vulnerableTime = 60*10;
	  }
	  
	  public void die() {
		    ded = true;
	  }
	  public void revive() {
		    ded = false;
	  }
	  public void setToRespawn(boolean a) {
		    toRespawn = a;
	  }
	  
    
    
    public void setAvailableTurns() {
    	availableDirections.clear();
    	boolean addUp = true, addDown = true, addLeft = true, addRight = true;
    	for (BaseStatic bloku: handler.getMap().getBlocksOnMap()) {
    		if (bloku instanceof BoundBlock) {
	    		if (bloku.y == posY && bloku.x == posX - pixelMultiplier) {
	    			 addLeft = false;// Left
	    		}
	    		if (bloku.y == posY && bloku.x == posX + pixelMultiplier) {
	    			addRight = false;
	    		}
	    		if (bloku.y == posY - pixelMultiplier && bloku.x == posX) {
	    			addUp = false;
    			}
	    		if (bloku.y == posY + pixelMultiplier && bloku.x == posX) {
	    			addDown = false;
    			}
    		}
    	}
    	if (addLeft && facing != 1) {
    		availableDirections.add(3);
    	}
    	if (addRight && facing != 3) {
    		availableDirections.add(1);
    	}
    	if (addUp && facing != 2) {
    		availableDirections.add(0);
    	}
    	if (addDown && facing != 0) {
    		availableDirections.add(2);
    	}
    }
    
    public void setTowardsPosition(int direction) {
    	int testX = posX;
    	int testY = posY;
    	boolean testpass = true;
    	switch (direction){
	        case 1:
	        	testX += pixelMultiplier;
	            break;
	        case 3:
	        	testX -= pixelMultiplier;
	            break;
	        case 0:
	        	testY -= pixelMultiplier;
	            break;
	        case 2:
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
    
    public void colorCycle() {
    	if (ghostColor == 3) {
    		ghostColor = 0;
    	}else {
    		ghostColor += 1;
    	}
    }

}
