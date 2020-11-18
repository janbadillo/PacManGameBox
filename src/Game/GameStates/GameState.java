package Game.GameStates;

import Display.UI.ClickListlener;
import Display.UI.UIImageButton;
import Display.UI.UIManager;
import Main.Handler;
import Resources.Images;

import java.awt.*;

/**
 * Created by AlexVR on 1/24/2020.
 */
public class GameState extends State {

    private UIManager uiManager;
    public UIImageButton logoButton;
    private int currentScreenWidth = handler.getWidth();
    private int currentScreenHeight = handler.getHeight();

    public GameState(Handler handler){
        super(handler);
        refresh();
    }


    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        if (currentScreenHeight != handler.getHeight() || currentScreenWidth != handler.getWidth()){
            currentScreenHeight = handler.getHeight();
            currentScreenWidth = handler.getWidth();
            uiManager.removeObsjects(logoButton);
            logoButton = new UIImageButton((float)(handler.getWidth()*(936.0/2160.0)), (float)(handler.getHeight()*(834.0/1620.0)), (int)(handler.getWidth()*(345.0/2160.0)), (int)(handler.getHeight()*(249.0/1620.0)), Images.pacmanLogo, new ClickListlener() {
                @Override
                public void onClick() {
                    handler.getMouseManager().setUimanager(null);
                    handler.getMusicHandler().stopMusic();
                    State.setState(handler.getPacManState());
                }
            });
            uiManager.addObjects(logoButton);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.selectionBackground,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);

    }

    @Override
    public void refresh() {
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);
        logoButton = new UIImageButton((float)(handler.getWidth()*(936.0/2160.0)), (float)(handler.getHeight()*(834.0/1620.0)), (int)(handler.getWidth()*(345.0/2160.0)), (int)(handler.getHeight()*(249.0/1620.0)), Images.pacmanLogo, new ClickListlener() {
            @Override
            public void onClick() {
                handler.getMouseManager().setUimanager(null);
                handler.getMusicHandler().stopMusic();
                State.setState(handler.getPacManState());
            }
        });
        uiManager.addObjects(logoButton);
    }
}
