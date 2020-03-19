package Game.PacMan.entities.Statics;

import Game.PacMan.entities.BaseEntity;
import Main.Handler;

import java.awt.image.BufferedImage;

public class BaseStatic extends BaseEntity {
	
	int col, row;
	
    public BaseStatic(int x, int y, int width, int height, Handler handler, BufferedImage sprite, int col, int row) {
        super(x, y, width, height, handler, sprite);
        this.row = row;
        this.col = col;
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
