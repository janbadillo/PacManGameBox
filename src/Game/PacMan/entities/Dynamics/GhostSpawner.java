package Game.PacMan.entities.Dynamics;

import Main.Handler;
import Resources.Images;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GhostSpawner extends BaseDynamic{

	public int col, row, ghostColor;
	private int spawnCooldown = 30, counter = 4, respawnCounter = 0;
	private boolean spawn = false;
	public Ghost ghost;
	public static ArrayList<BaseDynamic> ghostRespawn;
	private Random rand = new Random();
	
    public GhostSpawner(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.blankSpace);
        ghostRespawn = new ArrayList<BaseDynamic>();
    }

    @Override
    public void tick(){
    	//System.out.println(ghostRespawn.size());
    	if(respawnCounter > 0) {
    		respawnCounter--;
    	}else{
    		if(ghostRespawn.size() > 0) {
    			respawnGhost(ghostRespawn.get(0));
    			ghostRespawn.remove(0);
    		}
    		respawnCounter = (rand.nextInt(7)+3)*60;
    	}
    	if(spawnCooldown > 0) {
    		spawnCooldown--;
    	}
    	if (spawnCooldown <= 0 && counter > 0) {
    		spawnGhost();
    		spawnCooldown = 30;
    		counter--;
    	}

        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_C)){
        	spawnGhost();
        }

    }
    
    public void spawnGhost() {
    	spawn = true;
    	ghost = new Ghost(this.x,this.y,this.width,this.height,handler, ghostColor);
    	colorCycle();
    }
    public void respawnGhost(BaseDynamic beep) {
    	spawn = true;
    	ghost = (Ghost)beep;
    }
    
    public static void addGhost(BaseDynamic e) {
    	ghostRespawn.add(e);
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
