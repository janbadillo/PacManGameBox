package Game.PacMan.entities.Statics;

import Main.Handler;
import Resources.Images;

public class BlankSpace extends BaseStatic{
    public BlankSpace(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.blankSpace);
    }
}
