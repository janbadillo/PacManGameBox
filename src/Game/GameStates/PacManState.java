package Game.GameStates;

import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.GhostSpawner;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Dynamics.ScoreImage;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.Dot;
import Game.PacMan.entities.Statics.Fruit;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PacManState extends State {

    public String Mode = "Intro";
    public int startCooldown = 60*4, killTimer; //seven seconds for the music to finish and a pause after pacman kills ghost
    private boolean spawn, spawnScore, vulnerable;
    Ghost newGhost;
    ScoreImage score;
    public PacManState(Handler handler){
        super(handler);
        handler.setMap(MapBuilder.createMap(Images.map1, handler));
    }


    @Override
    public void tick() {
        if (Mode.equals("Stage")){
        	
            if (startCooldown<=0) {
            	if (killTimer > 0) {
            		killTimer--;
            	}
                for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
                	if (entity instanceof Ghost) {
                		if (vulnerable) {
                			((Ghost) entity).makeVulnerable();
                		}
                		if(((Ghost) entity).vulnerable) {
                			if(entity.getBounds().intersects(handler.getPacman().getBounds())) {
                				if (!entity.ded) {
                					((Ghost) entity).die();
                					handler.getMusicHandler().playEffect("pacman_eatghost.wav");
                					handler.getScoreManager().addPacmanCurrentScore(500);
                					score = new ScoreImage(entity.x, entity.y, handler.getPacman().width, handler.getPacman().height, handler, 1);
                    				killTimer = 15;
                    				spawnScore = true;
                				}
                			}
                		} else {
                			if(entity.getBounds().intersects(handler.getPacman().getHitbox().getBounds())) {
                				if(!entity.ded && !handler.getPacman().ded && !handler.getPacman().invinsible) {
                					handler.getPacman().die();
                					handler.getMusicHandler().playEffect("pacman_death.wav");
                				}
                			}
                		}
                	}                               
                	if (entity instanceof GhostSpawner) {

                			if (((GhostSpawner) entity).getSpawn()) {
                    			spawn = true;
                    			((GhostSpawner) entity).setSpawn(false);
                    			newGhost = ((GhostSpawner) entity).getNewGhost();
                    		}
                		
                	}
                	if (handler.getPacman().ded && entity instanceof PacMan) {
                		handler.getPacman().tick();
                	} else if (!handler.getPacman().ded && killTimer <= 0){
                		entity.tick();
                	}
                }
                vulnerable = false;

                if (spawn) {
                	handler.getMap().getEnemiesOnMap().add(newGhost);
                	spawn = false;
                }
                if (spawnScore) {
                	handler.getMap().getEnemiesOnMap().add(score);
                	spawnScore = false;
                }
                ArrayList<BaseStatic> toREmove = new ArrayList<>();
                for (BaseStatic blocks: handler.getMap().getBlocksOnMap()){
                    if (blocks instanceof Dot){
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                            handler.getMusicHandler().playEffect("pacman_newchomp.wav");
                        	handler.getScoreManager().addPacmanCurrentScore(10);
                            toREmove.add(blocks);
                        }
                    }else if (blocks instanceof BigDot){
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                        	vulnerable = true;
                            handler.getMusicHandler().playEffect("pacman_newchomp.wav");
                            toREmove.add(blocks);
                            handler.getScoreManager().addPacmanCurrentScore(100);
                        }
                    } else if (blocks instanceof Fruit){
                    	if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                            handler.getMusicHandler().playEffect("pacman_eatfruit.wav");
                        	handler.getScoreManager().addPacmanCurrentScore(120);
                            toREmove.add(blocks);
        					ScoreImage score = new ScoreImage(blocks.x, blocks.y, blocks.width, blocks.height, handler, 0);
        					handler.getMap().getEnemiesOnMap().add(score);
                        }
                    }
                }
                for (BaseStatic removing: toREmove){
                    handler.getMap().getBlocksOnMap().remove(removing);
                }
                ArrayList<BaseDynamic> toRemove = new ArrayList<>();
                for (BaseDynamic entity: handler.getMap().getEnemiesOnMap()){
                	if (entity instanceof ScoreImage) {
	            		if (entity.ded) {
	            			toRemove.add(entity);
	            		}
            		}
                }
                for (BaseDynamic removing: toRemove){
                    handler.getMap().getEnemiesOnMap().remove(removing);
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
    

    @Override
    public void refresh() {

    }


}
