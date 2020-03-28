package Game.PacMan.entities.Dynamics;


import java.awt.image.BufferedImage;

import Main.Handler;
import Resources.Images;


public class ScoreImage extends BaseDynamic{

    private static BufferedImage image;
	protected double speed = 2;
	private int counter = 5, endTime = 60*3;
	public int type;

    public ScoreImage(int x, int y, int width, int height, Handler handler, int type) {
        super(x, y, width, height, handler, image);
        if (type == 0) { // 120 score
        	image = Images.scoreSprite[0];
        } else if (type == 1){ //500 score
        	image = Images.scoreSprite[1];
        }
        this.type = type;
    }

    @Override
    public void tick(){
    	if (endTime <= 0) {
    		ded = true;
    	} else {
    	    if (counter <= 0) {
    	    	y--;
    	    	counter = 5;
    	    } else {
    	    	counter--;
    	    }
    	    endTime--;
    	}


	}
    
}
