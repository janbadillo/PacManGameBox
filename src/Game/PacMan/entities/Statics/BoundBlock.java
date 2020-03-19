package Game.PacMan.entities.Statics;

import Main.Handler;

import java.awt.image.BufferedImage;

public class BoundBlock extends BaseStatic {

    public BoundBlock(int x, int y, int width, int height, Handler handler, BufferedImage sprite, int col, int row) {
        super(x, y, width, height, handler, sprite, col, row);
    }

}
