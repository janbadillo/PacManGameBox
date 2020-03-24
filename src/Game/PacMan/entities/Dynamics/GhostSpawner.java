package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GhostSpawner extends BaseDynamic{

	int col, row, ghostColor;
	boolean spawn = false;
	Ghost ghost;
	
    public GhostSpawner(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.blankSpace);
    }

    @Override
    public void tick(){

        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_C)){ // must implement randomness to this spawner
        	spawn = true;
        	ghost = new Ghost(this.x,this.y,this.width,this.height,handler, ghostColor);
        	colorCycle();
        }

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
