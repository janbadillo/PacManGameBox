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
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PacManState extends State {

    public static String Mode = "Intro";
    public int startCooldown = 60*4, killTimer; //seven seconds for the music to finish and a pause after pacman kills ghost
    private boolean spawn, spawnScore, vulnerable, gameReset = false, newHigh = false, yes = false;
    private Ghost newGhost;
    private ScoreImage score;
    private int pixelmultiplier = MapBuilder.getPixelMultiplier();
    private BufferedImage map = Images.map1; // Change image to testMap to add only 1 dot for testing the reset.
    public PacManState(Handler handler){
        super(handler);
        handler.setMap(MapBuilder.createMap(map, handler));
    }


    @Override
    public void tick() {
        if (Mode.equals("Stage")){
        	if(handler.getPacman().getLives() <= 0) {
        		Mode = "EndGame";
        	}
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
                int dotCount = 0;
                for (BaseStatic blocks: handler.getMap().getBlocksOnMap()){
                    if (blocks instanceof Dot){
                    	dotCount++;
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                            handler.getMusicHandler().playEffect("pacman_newchomp.wav");
                        	handler.getScoreManager().addPacmanCurrentScore(10);
                            toREmove.add(blocks);
                        }
                    }else if (blocks instanceof BigDot){
                    	dotCount++;
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                        	vulnerable = true;
                            handler.getMusicHandler().playEffect("pacman_newchomp.wav");
                            toREmove.add(blocks);
                            handler.getScoreManager().addPacmanCurrentScore(100);
                        }
                    } else if (blocks instanceof Fruit){
                    	dotCount++;
                    	if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                            handler.getMusicHandler().playEffect("pacman_eatfruit.wav");
                        	handler.getScoreManager().addPacmanCurrentScore(120);
                            toREmove.add(blocks);
        					ScoreImage score = new ScoreImage(blocks.x, blocks.y, blocks.width, blocks.height, handler, 0);
        					handler.getMap().getEnemiesOnMap().add(score);
                        }
                    }
                }
                if(dotCount == 0) {
                	mapReset();
                }else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_R)) { // press R to reset
                	mapReset();
                	handler.getScoreManager().setPacmanCurrentScore(0);
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
                	if (entity instanceof Ghost) {
                		if (((Ghost)entity).toRespawn) {
                			((Ghost) entity).setToRespawn(false);
                			((Ghost) entity).revive();
                			GhostSpawner.addGhost(entity);
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
        }else if (Mode.equals("EndGame")){ // All ghosts get removed and PacMan ticks his death animation. High score is updated.
        	handler.getPacman().deathAnim.tick();
        	ArrayList<BaseDynamic> toRemove = new ArrayList<>();
        	for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
            	if (entity instanceof Ghost) {
            		toRemove.add(entity);
            	}
        	}
        	for (BaseDynamic removing: toRemove){
                handler.getMap().getEnemiesOnMap().remove(removing);
            }
        	
        	if(handler.getScoreManager().getPacmanCurrentScore() > handler.getScoreManager().getPacmanHighScore()) {
        		handler.getScoreManager().setPacmanHighScore(handler.getScoreManager().getPacmanCurrentScore());
        		newHigh = true;
        	}
        	
        	if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)){
        		if (!yes) {
        			yes=true;
                    handler.getMusicHandler().playEffect("pacman_newchomp.wav");
        		}
                
            }else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)){
            	if (yes) {
            		yes=false;
                    handler.getMusicHandler().playEffect("pacman_newchomp.wav");
        		}
            }
        	
        	if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                if(yes) {
                	gameReset();      	
                }else {
                	System.exit(0);
                }
            }
        	
        }else{
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Menu";
            }
        }



    }
    
    public void mapReset() {
		int lives = handler.getPacman().getLives();
		handler.setMap(MapBuilder.createMap(map, handler));
		handler.getPacman().setLives(lives);
		startCooldown = 60*4;
		handler.getMusicHandler().playEffect("pacman_beginning.wav");
    }
    
    public void gameReset() {
    	Mode = "Stage";
		handler.setMap(MapBuilder.createMap(map, handler));
		handler.getPacman().setLives(3);
		startCooldown = 60*4;
		handler.getMusicHandler().playEffect("pacman_beginning.wav");
		handler.getScoreManager().setPacmanCurrentScore(0);
    }
    

    @Override
    public void render(Graphics g) {

        if (Mode.equals("Stage")){
            Graphics2D g2 = (Graphics2D) g.create();
            handler.getMap().drawMap(g2);
            

            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
            g.drawString("Score: " + handler.getScoreManager().getPacmanCurrentScore(),map.getWidth()*pixelmultiplier + pixelmultiplier*2, 25);
            g.drawString("High-Score: " + handler.getScoreManager().getPacmanHighScore(),map.getWidth()*pixelmultiplier + pixelmultiplier*2, 75);
            

        }else if (Mode.equals("Menu")){
            g.drawImage(Images.start,0,0,handler.getWidth()/2,handler.getHeight(),null);
        }else if (Mode.equals("EndGame")){
        	Graphics2D g2 = (Graphics2D) g.create();
        	handler.getMap().drawMap(g2);
        	g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
            g.drawString("Score: " + handler.getScoreManager().getPacmanCurrentScore(),map.getWidth()*pixelmultiplier + pixelmultiplier*2, 25);
            g.drawString("High-Score: " + handler.getScoreManager().getPacmanHighScore(),map.getWidth()*pixelmultiplier + pixelmultiplier*2, 75);
            
            
            g.drawString("Play again?",map.getWidth()*pixelmultiplier + pixelmultiplier*2,map.getHeight()*pixelmultiplier/2);
            g.drawString("YES",map.getWidth()*pixelmultiplier + pixelmultiplier*2,map.getHeight()*pixelmultiplier/2 + pixelmultiplier*2);
            g.drawString("NO",map.getWidth()*pixelmultiplier + pixelmultiplier*6,map.getHeight()*pixelmultiplier/2 + pixelmultiplier*2);
            
            if (yes){
                g.drawImage(Images.galagaSelect, map.getWidth()*pixelmultiplier + pixelmultiplier + 5 , (int)(map.getHeight()*pixelmultiplier/2 + pixelmultiplier*1.3) ,15,15,null);
            }else{
                g.drawImage(Images.galagaSelect, map.getWidth()*pixelmultiplier + pixelmultiplier*5 + 5 , (int)(map.getHeight()*pixelmultiplier/2 + pixelmultiplier*1.3) ,15,15,null);
            }
            
            g.setColor(Color.RED);
            if(newHigh) {
                g.drawString("NEW HIGH-SCORE!",map.getWidth()*pixelmultiplier + pixelmultiplier*2, 200);
            }
            g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
            g.drawString("GAME OVER",map.getWidth()*pixelmultiplier/6 ,handler.getHeight()/2);
            
            
        }else{
            g.drawImage(Images.intro,0,0,handler.getWidth()/2,handler.getHeight(),null);
        }
    }
    

    @Override
    public void refresh() {

    }
    


}
