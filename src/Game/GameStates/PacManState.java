package Game.GameStates;

import Display.UI.UIManager;
import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.GhostSpawner;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.BlankSpace;
import Game.PacMan.entities.Statics.Dot;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PacManState extends State {

    private UIManager uiManager;
    public String Mode = "Intro";
    public int startCooldown = 60*4, vulnerableTime = 0; //seven seconds for the music to finish
    boolean spawn, vulnerable;
    Ghost newGhost;

    public PacManState(Handler handler){
        super(handler);
        handler.setMap(MapBuilder.createMap(Images.map1, handler));
    }


    @Override
    public void tick() {
        if (Mode.equals("Stage")){
        	if(vulnerableTime > 0) {
        		vulnerableTime--;
        	} else if (vulnerableTime <= 0) {
        		vulnerable = false;
        	}
            if (startCooldown<=0) {
                for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
                	if (entity instanceof Ghost) {
                		if(vulnerable) {
                			((Ghost) entity).setVulnerability(true);
                			if(entity.getBounds().intersects(handler.getPacman().getBounds())) {
                				// The ghost dies
                			}
                		} else {
                			((Ghost) entity).setVulnerability(false);
                			if(entity.getBounds().intersects(handler.getPacman().getBounds())) {
                				// PacMan dies
                			}
                		}
                	}
                	if (entity instanceof GhostSpawner) {
                		if(((GhostSpawner) entity).getSpawn()) {
                			spawn = true;
                			((GhostSpawner) entity).setSpawn(false);
                			newGhost = ((GhostSpawner) entity).getNewGhost();
                		}
                	}
                    entity.tick();
                }
                
                if (spawn) {
                	handler.getMap().getEnemiesOnMap().add(newGhost);
                	spawn = false;
                }
                ArrayList<BaseStatic> toREmove = new ArrayList<>();
                for (BaseStatic blocks: handler.getMap().getBlocksOnMap()){
                    if (blocks instanceof Dot){
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                            handler.getMusicHandler().playEffect("pacman_chomp.wav");
                            toREmove.add(blocks);
                            handler.getScoreManager().addPacmanCurrentScore(10);
                        }
                    }else if (blocks instanceof BigDot){
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                        	vulnerableTime = 60*7;
                        	vulnerable = true;
                            handler.getMusicHandler().playEffect("pacman_chomp.wav");
                            toREmove.add(blocks);
                            handler.getScoreManager().addPacmanCurrentScore(100);

                        }
                    } 
                }
                for (BaseStatic removing: toREmove){
                    handler.getMap().getBlocksOnMap().remove(removing);
                    BaseStatic bloku =  new BlankSpace(removing.x,removing.y,removing.width,removing.height,handler,removing.getCol(),removing.getRow());
                    handler.getMap().getBlocksOnMap().add(bloku);
                }
            }else{
                startCooldown--;
            }
        }else if (Mode.equals("Menu")){
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Stage";
                handler.getMusicHandler().playEffect("pacman_beginning.wav");
            }
        }else{
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Menu";
            }
        }



    }

    @Override
    public void render(Graphics g) {

        if (Mode.equals("Stage")){
            Graphics2D g2 = (Graphics2D) g.create();
            handler.getMap().drawMap(g2);
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
            g.drawString("Score: " + handler.getScoreManager().getPacmanCurrentScore(),(handler.getWidth()/2) + handler.getWidth()/6, 25);
            g.drawString("High-Score: " + handler.getScoreManager().getPacmanHighScore(),(handler.getWidth()/2) + handler.getWidth()/6, 75);
        }else if (Mode.equals("Menu")){
            g.drawImage(Images.start,0,0,handler.getWidth()/2,handler.getHeight(),null);
        }else{
            g.drawImage(Images.intro,0,0,handler.getWidth()/2,handler.getHeight(),null);

        }
    }
    
    public int getVulnerableTime() {
    	return vulnerableTime;
    }

    @Override
    public void refresh() {

    }


}
