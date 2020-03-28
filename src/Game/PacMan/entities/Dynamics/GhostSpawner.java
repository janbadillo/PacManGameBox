package Game.PacMan.entities.Dynamics;

import Main.Handler;
import Resources.Images;
import java.awt.event.KeyEvent;

public class GhostSpawner extends BaseDynamic{

	public int col, row, ghostColor;
	private int spawnCooldown = 30, counter = 4;
	private boolean spawn = false;
	public Ghost ghost;
	
    public GhostSpawner(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.blankSpace);
    }

    @Override
    public void tick(){
    	if(spawnCooldown > 0) {
    		spawnCooldown--;
    	}
    	if (spawnCooldown <= 0 && counter > 0) {
    		spawnGhost();
    		spawnCooldown = 30;
    		counter--;
    	}

        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_C)){ // must implement randomness to this spawner
        	spawnGhost();
        }

    }
    
    public void spawnGhost() {
    	spawn = true;
    	ghost = new Ghost(this.x,this.y,this.width,this.height,handler, ghostColor);
    	colorCycle();
    }
    
    public void colorCycle() {
    	if (ghostColor == 3) {
    		ghostColor = 0;
    	}else {
    		ghostColor += 1;
    	}
    }
    
    public void setSpawn(Boolean a) {
    	spawn = a;
    }
    public boolean getSpawn() {
    	return spawn;
    }
    
    public Ghost getNewGhost() {
    	return ghost;
    }
}
