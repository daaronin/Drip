/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drip;

import drip.Player.Temp;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
/**
 *
 * @author Dan Ford
 */
public class Environment {

    public boolean LEVEL_EDITOR = false;
    public boolean LEVEL_COMPLETE = false;
    public boolean CLICKED_CONTINUE = false;
    public boolean ROOTED = false;
    public boolean ROOT_PLANTED = false;
    
    public String GAME_BACKGROUND = "empty";
    public String GAME_FOREGROUND = "empty";
    public String GAME_SHADOW = "empty";
    public Rectangle GAME_BACKGROUND_RECT;
    public Rectangle GAME_FOREGROUND_RECT;
    public Rectangle GAME_SHADOW_RECT;
    private float BACK_SCROLL = 1f;
    private float FORE_SCROLL = 1f;
    
    private ArrayList<Player> players;
    private ArrayList<Scenery> sceneries;
    private ArrayList<Threat> threats;
    private int character_count = 0;
    private int scenery_count = 0;
    private int threat_count = 0;
    private int LEVEL_TEMPERATURE = 500;
    private boolean paused = false;
    public float xloc = -400;
    public float yloc = -400;
    
    private int OFFSET = 1;
    Point startingPoint;
    private int DEATH_COUNT = 0;
    
    boolean PICKED_UP = false;
    boolean FIRST = false;
    DefaultCharacter CLICKED;
    public int CLICKED_HEIGHT;
    public int CLICKED_WIDTH;
    
    public int level = 1;
    public int previouslevel = 1;
    
    public float SIZE_CHANGE = 1f;
    
    private boolean checkClick(DefaultCharacter object, int mousex, int mousey) {
        if (Mouse.isButtonDown(0)){
               if (mousex >= object.position.getX() 
                       && mousex <= object.position.getX() + object.position.getWidth() 
                       && mousey >= object.position.getY() 
                       && mousey <= object.position.getY() + object.position.getHeight()){
                    CLICKED = object;
                    CLICKED_WIDTH = object.position.getWidth();
                    CLICKED_HEIGHT = object.position.getHeight();
                    return true;
               }     
        }
        return false;
    }

    public enum CharacterType {

        PLAYER, ENEMY, THREAT, NPC, OBJECT, SCENERY, PERMEABLESCENERY, HEALINGSCENERY, DECORATION, TREE;
    }

     private void addObject(CharacterType TYPE, int x, int y, int width, int height, int health, int temperature, String tex, boolean editor) {
        //if(colony.getAntCount() < population){
        if (TYPE == CharacterType.PLAYER) {
            players.add(new Player(x, y, width, height, health, temperature, tex, editor, LEVEL_EDITOR));
            
            raiseCharacterCount();
        } else if (TYPE == CharacterType.ENEMY) {
            //characters_arr.add(new Player(250, 400, 64, 62, 100, "drippy"));
            //raiseCharacterCount();
        } else if (TYPE == CharacterType.SCENERY) {
            sceneries.add(new Scenery(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.PERMEABLESCENERY) {
            sceneries.add(new PermeableScenery(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.HEALINGSCENERY) {
            sceneries.add(new HealingScenery(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.TREE) {
            sceneries.add(new Tree(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.DECORATION) {
            sceneries.add(new Decoration(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.THREAT) {
            threats.add(new Threat(x, y, width, height, health, temperature, tex, editor));
            raiseThreatCount();
        }
        //}

    }
     
    public void setLevelEditor(boolean set){
        LEVEL_EDITOR = set;
    }
    
    public boolean getLevelEditor(){
        return LEVEL_EDITOR;
    }
    
    public boolean getPaused(){
        return paused;
    }
    
    public void pause(){
        paused = true;
    }
    
    public void unpause(){
        paused = false;
    }
     
    public ArrayList<Player> getCharacters(){
        return players;
    }
    
    public void setCharacters(ArrayList<Player> characters){
        this.players = characters;
    }
    
    public void raiseCharacterCount(){
        character_count++;
    }
    
    public void lowerCharacterCount(){
        character_count--;
    }
    
    public int getCharacterCount(){
        return character_count;
    }
    
    public ArrayList<Scenery> getSceneries(){
        return sceneries;
    }
    
    private void setScenery(ArrayList<Scenery> sceneries){
        this.sceneries = sceneries;
    }
    
    private void raiseSceneryCount() {
        scenery_count++;
    }
    
    private void lowerSceneryCount() {
        scenery_count--;
    }
    
    public int getSceneryCount(){
        return scenery_count;
    }
    
    public void setSceneryCount(int num){
        scenery_count = num;
    }
    
    public ArrayList<Threat> getThreats(){
        return threats;
    }
        
    private void raiseThreatCount() {
        threat_count++;
    }
    
    private void lowerThreatCount() {
        threat_count--;
    }
    
    public int getThreatCount(){
        return threat_count;
    }
    
    public void setThreatCount(int num){
        threat_count = num;
    }
    
    public int getPlayerHealth(){
        return players.get(0).getHealth();
    }
    
    public int getPlayerTemperature(){
        return players.get(0).getTemperature();
    }
    
    public Environment(boolean leveledit) {
        LEVEL_EDITOR = leveledit;
        players = new ArrayList<Player>();
        sceneries = new ArrayList<Scenery>();
        threats = new ArrayList<Threat>();
        {
            
            addObject(CharacterType.PLAYER, 368, 269, 64, 62, 1000, 500, "drippyL", true);
            if (LEVEL_EDITOR){
                levelEditor();
            }
            
            //-------------------------- LEVEL 1 ------------------------------//
            if (level == 1){
                levelOne();
            } else if (level == 2){
                levelTwo();
            } else if (level == 3){
                levelThree();
            } else if (level == 4){
                levelFour();
            } else if (level == 5){
                levelFive();
            } else if (level == 6){
                levelSix();
            }
            System.out.println(level);
        }
    }

    public Point onClockTick(int delta) {
        
        if (level != previouslevel){
            if (level == 1){
                
            } else if (level == 2){
                levelTwo();
            } else if (level ==  3){
                levelThree();
            } else if (level == 4){
                levelFour();
            } else if (level == 5){
                levelFive();
            } else if (level == 6){
                levelSix();
            }
        }
        previouslevel = level;
        
        int mousex = 0;
        int mousey = 0;
        if (LEVEL_EDITOR){
            mousex = Mouse.getX();
            mousey = 800-Mouse.getY();
        }
            if (Keyboard.isKeyDown(Keyboard.KEY_0)){
                SIZE_CHANGE = 4f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_1)){
                SIZE_CHANGE = .25f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_2)){
                SIZE_CHANGE = .5f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_3)){
                SIZE_CHANGE = .75f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_4)){
                SIZE_CHANGE = 1f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_5)){
                SIZE_CHANGE = 1.25f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_6)){
                SIZE_CHANGE = 1.5f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_7)){
                SIZE_CHANGE = 1.75f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_8)){
                SIZE_CHANGE = 2f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_9)){
                SIZE_CHANGE = 3f;
            }
        int TOTAL_BLOOMABLE = 0;
        int TOTAL_BLOOMED = 0;
        String collisionDirection = "";
            for (Player player : players) {
                player.setEditor(getLevelEditor());
                for (Scenery scenery : sceneries){
                    if (scenery instanceof Decoration){
                        if (scenery.isBloomable() && scenery instanceof Tree == false){
                            TOTAL_BLOOMABLE++;
                        }
                        if (scenery.getBloomed() == true && scenery instanceof Tree == false){
                            TOTAL_BLOOMED++;
                        }
                        if (scenery instanceof Tree && scenery.getBloomed() == true && ROOT_PLANTED == false){
                            ROOTED = true;
                        }
                    }
                    
                    if(scenery.getEditor() == false){
                        scenery.position.setX(((int)(scenery.getRelativeX() + xloc)));
                        scenery.position.setY(((int)(scenery.getRelativeY() + yloc)));
                    }
                    scenery.update(delta, "");
                    if (LEVEL_EDITOR){
                        if (Mouse.isButtonDown(0)&& !PICKED_UP){
                            PICKED_UP = checkClick(scenery, mousex, mousey);
                            FIRST = true;
                        }
                        if (scenery == CLICKED && PICKED_UP && !FIRST){
                            scenery.position.setX(mousex-scenery.position.getWidth()/2);
                            scenery.position.setY(mousey-scenery.position.getHeight()/2);

                            scenery.position.setWidth((int)(CLICKED_WIDTH*SIZE_CHANGE));
                            scenery.position.setHeight((int)(CLICKED_HEIGHT*SIZE_CHANGE));
                            
                            if (Mouse.isButtonDown(1)){
                                scenery.setRelativeX(mousex-(scenery.position.getWidth()/2)-(int)xloc);
                                scenery.setRelativeY(mousey-(scenery.position.getHeight()/2)-(int)yloc);
                                scenery.setEditor(false);
                                printOutLoc(scenery, mousex, mousey, xloc, yloc);
                                CLICKED = null;
                                PICKED_UP = false;
                            }
                        }
                    } else if (!LEVEL_EDITOR){
                        boolean possibleCollision = compareQuads(player.getQuad(), scenery.getQuad());
                        if (possibleCollision){
                            if (!paused){
                                String collisionDir = compareLocationBetter(player, scenery);
                                if (collisionDir != ""){
                                    collisionDirection = collisionDir;
                                    if (scenery instanceof HealingScenery){
                                        player.raiseHealth(1);
                                        //TODO
                                        //startingPoint = new Point(scenery.position.getX()-400, scenery.position.getY()+1600-40);
                                    } else if (scenery instanceof Tree){
                                        scenery.waterPlant(5);
                                        if (scenery.getTexture().equals("root")){
                                            System.out.println("YOU WON!!!");
                                            LEVEL_COMPLETE = true;
                                        }
                                    } else if (scenery instanceof Decoration){
                                        scenery.waterPlant(5);
                                    }
                                }
                            }
                        }
                    }
                }
                for (Threat threat : threats){
                    if(threat.getEditor() == false){
                        threat.position.setX((threat.getRelativeX() + ((int)xloc)));
                        threat.position.setY((threat.getRelativeY() + ((int)yloc)));
                    }
                    threat.update(delta, "");
                    threat.updateIterator(delta);
                    if (LEVEL_EDITOR){
                        if (Mouse.isButtonDown(0)&& !PICKED_UP){
                            PICKED_UP = checkClick(threat, mousex, mousey);
                            FIRST = true;
                        }
                        if (threat == CLICKED && PICKED_UP && !FIRST){
                            threat.position.setX(mousex-threat.position.getWidth()/2);
                            threat.position.setY(mousey-threat.position.getHeight()/2);

                            threat.position.setWidth((int)(CLICKED_WIDTH*SIZE_CHANGE));
                            threat.position.setHeight((int)(CLICKED_HEIGHT*SIZE_CHANGE));
                            
                            if (Mouse.isButtonDown(1)){
                                threat.setRelativeX(mousex-(threat.position.getWidth()/2)-(int)xloc);
                                threat.setRelativeY(mousey-(threat.position.getHeight()/2)-(int)yloc);
                                threat.setEditor(false);
                                printOutLoc(threat, mousex, mousey, xloc, yloc);
                                CLICKED = null;
                                PICKED_UP = false;
                            }
                        }
                    }else if (!LEVEL_EDITOR){
                        boolean possibleCollision = compareQuads(player.getQuad(), threat.getQuad());
                        if (possibleCollision){
                            if (!paused){
                                String collisionDir = compareLocationBetter(player, threat);
                                if (collisionDir != ""){
                                    collisionDirection = collisionDir;
                                    player.incTemperature(((threat.getTemperature() - player.getTemperature())));
                                    player.lowerHealth(1);
                                }
                            }
                        }
                    }
                }
                if (ROOT_PLANTED == false && ROOTED == true){
                    addObject(CharacterType.TREE, 1840, 1856, 59, 285, 500, 500, "root", false);
                    ROOT_PLANTED = true;
                }
                if (FIRST){
                    if (PICKED_UP && CLICKED.getEditor() == true){
                        DefaultCharacter object = CLICKED;
                        if (object instanceof HealingScenery){
                            addObject(CharacterType.HEALINGSCENERY, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);
                        } else if (object instanceof PermeableScenery){
                            addObject(CharacterType.PERMEABLESCENERY, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);                       
                        } else if (object instanceof Tree){
                            addObject(CharacterType.TREE, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);                       
                        } else if (object instanceof Decoration){
                            addObject(CharacterType.DECORATION, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);                       
                        } else if (object instanceof Threat){
                            addObject(CharacterType.THREAT, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);
                        } else if (object instanceof Scenery){
                            addObject(CharacterType.SCENERY, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);
                        }
                    }
                    FIRST = false;
                }
                if (TOTAL_BLOOMABLE == TOTAL_BLOOMED && TOTAL_BLOOMABLE != 0){
                    LEVEL_COMPLETE = true;
                }
                if (yloc < -1500 && level != 6){
                    player.setXLocal(startingPoint.getX());
                    xloc = startingPoint.getX();
                    player.setYLocal(startingPoint.getY());
                    yloc = startingPoint.getY();
                    DEATH_COUNT++;
                } else if (yloc < -4000 && level == 6){
                    player.setXLocal(startingPoint.getX());
                    xloc = startingPoint.getX();
                    player.setYLocal(startingPoint.getY());
                    yloc = startingPoint.getY();
                    DEATH_COUNT++;
                }
                
                if (!paused){
                    player.update(delta, collisionDirection);
                    xloc = -(player.getXLocal()+ player.position.getWidth());
                    yloc = -(player.getYLocal() + player.position.getHeight());
                    if (player.getTemperature() > LEVEL_TEMPERATURE){
                        player.decTemperature(1);
                    } else if (player.getTemperature() < LEVEL_TEMPERATURE){
                        player.incTemperature(1);
                    }
                }
        }
        updateBackground(xloc,yloc);
        Point locs = new Point((int)xloc, (int)yloc);
        return locs;
    }
    
    public void printOutLoc(DefaultCharacter object, int mousexx, int mouseyy, float xlocx, float ylocy){
        if (object instanceof HealingScenery){
            System.out.println("addObject(CharacterType.HEALINGSCENERY, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+", "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \""+object.getTexture()+"\", "+false+");");
        } else if (object instanceof PermeableScenery){
            System.out.println("addObject(CharacterType.PERMEABLESCENERY, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+", "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \""+object.getTexture()+"\", "+false+");");
        } else if (object instanceof Tree){
            System.out.println("addObject(CharacterType.TREE, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+", "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \""+object.getTexture()+"\", "+false+");");
        } else if (object instanceof Decoration){
            System.out.println("addObject(CharacterType.DECORATION, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+", "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \""+object.getTexture()+"\", "+false+");");
        } else if (object instanceof Threat){
            System.out.println("addObject(CharacterType.THREAT, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+", "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \""+object.getTexture()+"\", "+false+");");
        } else if (object instanceof Scenery){
            System.out.println("addObject(CharacterType.SCENERY, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+", "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \""+object.getTexture()+"\", "+false+");");
        }
    }
    
    private char compareLocation(Player character, DefaultCharacter object) {
        char direction = 'N';
        int characterrightx = character.position.getX() + character.position.getWidth();
        int characterbottomy = character.position.getY() + character.position.getHeight();
        int objectrightx = object.position.getX() + object.position.getWidth();
        int objectbottomy = object.position.getY() + object.position.getHeight(); 
        
        if(((character.position.getX() > object.position.getX()) && (character.position.getX() < objectrightx))
                ||((characterrightx > object.position.getX()) && (characterrightx < objectrightx)) 
                || (character.position.getX() < object.position.getX() && characterrightx > object.position.getX())
                || (character.position.getX() < objectrightx && characterrightx > objectrightx)){
            if(characterbottomy >= object.position.getY() && characterbottomy <= objectbottomy){
                //DOWN
                if(character.position.getY() < object.position.getY()){
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getY()+character.position.getHeight() > object.position.getY())
                            character.setYLocal(character.getYLocal()-OFFSET);
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction = 'B';
                        character.D = 0;
                    } else if (object instanceof Decoration){
                        direction = 'G';
                    } else {
                        direction = 'A';
                    }
                } else if (character.position.getX()+character.position.getWidth() > object.position.getX() && character.position.getX()< object.position.getX()){
                    //RIGHT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX()+character.position.getWidth() > object.position.getX()){
                            character.setXLocal(character.getXLocal()-OFFSET);
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction = 'R';
                        character.R = 0;
                        //System.out.println("RIGHT1-R=0");                        
                    } else if (object instanceof Decoration){
                        direction = 'G';
                    } else {
                        direction = 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                } else if (character.position.getX() < objectrightx && character.position.getX() + character.position.getWidth() > objectrightx){
                    //LEFT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX() < objectrightx){
                            character.setXLocal(character.getXLocal()+OFFSET);
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction = 'L';
                        character.L = 0;
                        //System.out.println("LEFT1-L=0");
                    } else if (object instanceof Decoration){
                        direction = 'G';
                    } else {
                        direction = 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                }
            } else if (character.position.getY() <= objectbottomy && character.position.getY() >= object.position.getY()){
                //UP
                if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                    if (character.position.getY() < objectbottomy){
                        character.setYLocal(character.getYLocal()+OFFSET);
                        //System.out.println("TOP");                        
                    }
                }
                if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction = 'T';
                        character.U = 0;
                        //System.out.println("TOP-U=0");                        
                    } else if (object instanceof Decoration){
                        direction = 'G';
                    } else {
                        direction = 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
            } else if (character.position.getY() <= object.position.getY() && characterbottomy >= objectbottomy){
                if (characterrightx > object.position.getX() && characterrightx < objectrightx){
                    //RIGHT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX()+character.position.getWidth() > object.position.getX()){
                            character.setXLocal(character.getXLocal()-OFFSET);
                            //System.out.println("RIGHT2");                            
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction = 'R';
                        character.R = 0;
                        //System.out.println("RIGHT2-R=2");                        
                    } else if (object instanceof Decoration){
                        direction = 'G';
                    } else {
                        direction = 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                } else if (character.position.getX() < objectrightx && character.position.getX() > object.position.getX()){
                    //LEFT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX() < objectrightx){
                            character.setXLocal(character.getXLocal()+OFFSET);
                            //System.out.println("LEFT2");                        
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction = 'L';
                        character.L = 0;
                        //System.out.println("LEFT2-L=0");                        
                    } else if (object instanceof Decoration){
                        direction = 'G';
                    } else {
                        direction = 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                }
            }
        }
        //System.out.println("NO Collision.");
        return direction;
    }
    
    private String compareLocationBetter(Player character, DefaultCharacter object) {
        String direction = "";
        int characterrightx = character.position.getX() + character.position.getWidth();
        int characterbottomy = character.position.getY() + character.position.getHeight();
        int objectrightx = object.position.getX() + object.position.getWidth();
        int objectbottomy = object.position.getY() + object.position.getHeight(); 
        
        if(((character.position.getX() > object.position.getX()) && (character.position.getX() < objectrightx))
                ||((characterrightx > object.position.getX()) && (characterrightx < objectrightx)) 
                || (character.position.getX() < object.position.getX() && characterrightx > object.position.getX())
                || (character.position.getX() < objectrightx && characterrightx > objectrightx)){
            if(characterbottomy >= object.position.getY() && characterbottomy <= objectbottomy){
                //DOWN
                if(character.position.getY() < object.position.getY()){
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (characterbottomy >= object.position.getY()){
                            if ((characterbottomy - object.position.getY()) >= 5*OFFSET+1){
                                character.setYLocal(character.getYLocal()-OFFSET*5);
                            } else if (characterbottomy - object.position.getY() >= 1) {
                                character.setYLocal(character.getYLocal()-OFFSET);
                            }
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'B';
                        character.D = 0;
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                } else if (characterrightx > object.position.getX() && character.position.getX()< object.position.getX()){
                    //RIGHT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX()+character.position.getWidth() > object.position.getX()){
                            character.setXLocal(character.getXLocal()-(characterrightx - object.position.getX()));
                            //System.out.println("RIGHT, XLOC:" + character.getXLocal() +", Object:" + object.position.getX());
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'R';
                        character.R = 0;
                        //System.out.println("RIGHT1-R=0");                        
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                } else if (character.position.getX() < objectrightx && characterrightx > objectrightx){
                    //LEFT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX() < objectrightx){
                            character.setXLocal(character.getXLocal()+(objectrightx-character.position.getX()));
                            //System.out.println("LEFT, XLOC:" + character.getXLocal() +", OBJECTRIGHTX:" + objectrightx);
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'L';
                        character.L = 0;
                        //System.out.println("LEFT1-L=0");
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                }
            } else if (character.position.getY() <= objectbottomy && character.position.getY() >= object.position.getY()){
                //UP
                if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                    if (character.position.getY() < objectbottomy){
                        if ((objectbottomy-character.position.getY()) >= 5*OFFSET){
                            character.setYLocal(character.getYLocal()+OFFSET*5);
                        } else {
                            character.setYLocal(character.getYLocal()+OFFSET);
                        }
                        //System.out.println("TOP");                        
                    }
                }
                if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'T';
                        character.U = 0;
                        //System.out.println("TOP-U=0");                        
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
            } else if (character.position.getY() <= object.position.getY() && characterbottomy >= objectbottomy){
                if (characterrightx > object.position.getX() && characterrightx < objectrightx){
                    //RIGHT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX()+character.position.getWidth() > object.position.getX()){
                            character.setXLocal(character.getXLocal()-(characterrightx - object.position.getX()));
                            //System.out.println("RIGHT2, XLOC:" + character.getXLocal() +", Object:" + object.position.getX());                            
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'R';
                        character.R = 0;
                        //System.out.println("RIGHT2-R=2");                        
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                } else if (character.position.getX() < objectrightx && character.position.getX() > object.position.getX()){
                    //LEFT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX() < objectrightx){
                            character.setXLocal(character.getXLocal()+(objectrightx-character.position.getX()));
                           //System.out.println("LEFT2, XLOC:" + character.getXLocal() +", OBJECTRIGHTX:" + objectrightx);                        
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'L';
                        character.L = 0;
                        //System.out.println("LEFT2-L=0");                        
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                }
            }
        }
        //System.out.println("NO Collision.");
       
        return direction;
    }
    
     private String compareLocationBetterOld(Player character, DefaultCharacter object) {
        String direction = "";
        int characterrightx = character.position.getX() + character.position.getWidth();
        int characterbottomy = character.position.getY() + character.position.getHeight();
        int objectrightx = object.position.getX() + object.position.getWidth();
        int objectbottomy = object.position.getY() + object.position.getHeight(); 
        
        if(((character.position.getX() > object.position.getX()) && (character.position.getX() < objectrightx))
                ||((characterrightx > object.position.getX()) && (characterrightx < objectrightx)) 
                || (character.position.getX() < object.position.getX() && characterrightx > object.position.getX())
                || (character.position.getX() < objectrightx && characterrightx > objectrightx)){
            if(characterbottomy >= object.position.getY() && characterbottomy <= objectbottomy){
                //DOWN
                if(character.position.getY() < object.position.getY()){
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getY()+character.position.getHeight() > object.position.getY())
                            character.setYLocal(character.getYLocal()-(characterbottomy-object.position.getY()));
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'B';
                        character.D = 0;
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                } else if (characterrightx > object.position.getX() && character.position.getX()< object.position.getX()){
                    //RIGHT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX()+character.position.getWidth() > object.position.getX()){
                            character.setXLocal(character.getXLocal()-(characterrightx - object.position.getX()));
                            System.out.println("RIGHT, XLOC:" + character.getXLocal() +", Object:" + object.position.getX());
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'R';
                        character.R = 0;
                        //System.out.println("RIGHT1-R=0");                        
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                } else if (character.position.getX() < objectrightx && characterrightx > objectrightx){
                    //LEFT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX() < objectrightx){
                            character.setXLocal(character.getXLocal()+(objectrightx-character.position.getX()));
                            System.out.println("LEFT, XLOC:" + character.getXLocal() +", OBJECTRIGHTX:" + objectrightx);
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'L';
                        character.L = 0;
                        //System.out.println("LEFT1-L=0");
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                }
            } else if (character.position.getY() <= objectbottomy && character.position.getY() >= object.position.getY()){
                //UP
                if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                    if (character.position.getY() < objectbottomy){
                        character.setYLocal(character.getYLocal()+OFFSET);
                        //System.out.println("TOP");                        
                    }
                }
                if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'T';
                        character.U = 0;
                        //System.out.println("TOP-U=0");                        
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
            } else if (character.position.getY() <= object.position.getY() && characterbottomy >= objectbottomy){
                if (characterrightx > object.position.getX() && characterrightx < objectrightx){
                    //RIGHT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX()+character.position.getWidth() > object.position.getX()){
                            character.setXLocal(character.getXLocal()-(characterrightx - object.position.getX()));
                            System.out.println("RIGHT2, XLOC:" + character.getXLocal() +", Object:" + object.position.getX());                            
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'R';
                        character.R = 0;
                        //System.out.println("RIGHT2-R=2");                        
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                } else if (character.position.getX() < objectrightx && character.position.getX() > object.position.getX()){
                    //LEFT
                    if ((object instanceof Scenery) && !((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration)){
                        if (character.position.getX() < objectrightx){
                            character.setXLocal(character.getXLocal()+(objectrightx-character.position.getX()));
                            System.out.println("LEFT2, XLOC:" + character.getXLocal() +", OBJECTRIGHTX:" + objectrightx);                        
                        }
                    }
                    if (!(object instanceof Threat ||((object instanceof PermeableScenery && character.getTemp()!= Temp.ICE) || object instanceof HealingScenery || object instanceof Decoration))){
                        direction += 'L';
                        character.L = 0;
                        //System.out.println("LEFT2-L=0");                        
                    } else if (object instanceof Decoration){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                    //System.out.println("DIRECTION: "+direction);
                }
            }
        }
        //System.out.println("NO Collision.");
        return direction;
    }
    
    public boolean compareQuads(int x, int y){
        boolean collision = false;
        int highestQuad = 32768;
        while (x > 0 && y > 0 && !collision){
            if (x >= highestQuad && y >= highestQuad){
                collision = true;
            } else {
                if (x >= highestQuad){
                    x -= highestQuad;
                }
                if (y >= highestQuad){
                    y -= highestQuad;
                }
            }
            highestQuad = highestQuad/2;
        }
        return collision;
    }
    
        public void clearLevel(){
            for (int i = getSceneryCount()-1; i >= 0; i--){
                sceneries.remove(i);
            }
            setSceneryCount(0);
            for (int i = getThreatCount()-1; i >= 0; i--){
                threats.remove(i);
            }
            setThreatCount(0);
        }
    
        private void levelEditor(){
            addObject(CharacterType.SCENERY, 840, 40, 51, 11, 500, 500, "mossflat", true);
            addObject(CharacterType.SCENERY, 910, 40, 51, 11, 500, 500, "mosscurve", true);

            addObject(CharacterType.SCENERY, 810, 70, 30, 11, 500, 500, "leftmoss", true);
            addObject(CharacterType.SCENERY, 850, 70, 30, 11, 500, 500, "rightmoss", true);

            addObject(CharacterType.SCENERY, 890, 70, 50, 15, 500, 500, "fatplatform", true);
            addObject(CharacterType.SCENERY, 950, 70, 30, 30, 500, 500, "squareplatform", true);

            addObject(CharacterType.SCENERY, 815, 100, 50, 9, 500, 500, "platform", true);

            addObject(CharacterType.SCENERY, 875, 105, 50, 5, 500, 500, "iceplatform", true);

            addObject(CharacterType.SCENERY, 815, 120, 50, 88, 500, 500, "largestalactite", true);
            addObject(CharacterType.SCENERY, 875, 120, 50, 88, 500, 500, "largestalagmite", true);

            addObject(CharacterType.SCENERY, 935, 120, 25, 44, 500, 500, "smallstalactite", true);
            addObject(CharacterType.SCENERY, 960, 120, 25, 44, 500, 500, "smallstalagmite", true);

            addObject(CharacterType.PERMEABLESCENERY, 815, 220, 17, 53, 500, 500, "iceright", true);
            addObject(CharacterType.PERMEABLESCENERY, 845, 220, 17, 53, 500, 500, "rockright", true);
            addObject(CharacterType.PERMEABLESCENERY, 875, 220, 17, 53, 500, 500, "iceleft", true);
            addObject(CharacterType.PERMEABLESCENERY, 905, 220, 17, 53, 500, 500, "rockleft", true);

            addObject(CharacterType.THREAT, 40, 620, 20, 50, 500, 900, "fire1", true);
            addObject(CharacterType.THREAT, 70, 620, 20, 50, 500, 100, "ice1", true);

            addObject(CharacterType.DECORATION, 150, 620, 153, 12, 500, 500, "grass", true);

            addObject(CharacterType.DECORATION, 40, 680, 53, 43, 500, 500, "shroom1", true);
            addObject(CharacterType.DECORATION, 100, 680, 53, 43, 500, 500, "shroom2", true);
            addObject(CharacterType.DECORATION, 160, 680, 53, 43, 500, 500, "shroom3", true);
            addObject(CharacterType.DECORATION, 220, 680, 53, 43, 500, 500, "shroom4", true);

            addObject(CharacterType.DECORATION, 40, 735, 37, 40, 500, 500, "shroom", true);

            addObject(CharacterType.DECORATION, 285, 620, 39, 93, 500, 500, "grass1", true);
            addObject(CharacterType.DECORATION, 330, 620, 39, 93, 500, 500, "grass2", true);
            addObject(CharacterType.DECORATION, 390, 620, 39, 93, 500, 500, "grass3", true);
            addObject(CharacterType.DECORATION, 450, 620, 39, 93, 500, 500, "grass4", true);

            addObject(CharacterType.HEALINGSCENERY, 90, 735, 100, 22, 500, 500, "pool1", true);
            addObject(CharacterType.TREE, 210, 735, 29, 50, 500, 500, "tree1", true);
            addObject(CharacterType.TREE, 510, 620, 110, 140, 500, 500, "tree2", true);
            addObject(CharacterType.TREE, 640, 620, 83, 127, 500, 500, "tree3", true);
            addObject(CharacterType.TREE, 950, 205, 30, 142, 500, 500, "root", true);
            

        }
    
        private void levelOne() {
            
        startingPoint = new Point(48, 469);
        players.get(0).setXLocal(startingPoint.getX());
        players.get(0).setYLocal(startingPoint.getY());

        GAME_BACKGROUND = "cavebackground";
        GAME_FOREGROUND = "caveforeground";
        GAME_SHADOW = "cavebackgrounddark";
        GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
        GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
        GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);

        addObject(CharacterType.SCENERY, 740, 962, 52, 52, 500, 500, "squareplatform", false);

        addObject(CharacterType.SCENERY, 1444, 742, 60, 60, 500, 500, "squareplatform", false);
        addObject(CharacterType.SCENERY, 1445, 739, 58, 13, 500, 500, "mosscurve", false);

        addObject(CharacterType.DECORATION, 555, 514, 39, 93, 500, 500, "grass1", false);
        addObject(CharacterType.DECORATION, 628, 515, 39, 93, 500, 500, "grass1", false);
        addObject(CharacterType.DECORATION, 585, 515, 39, 93, 500, 500, "grass2", false);

        addObject(CharacterType.DECORATION, 269, 593, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 562, 594, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 725, 595, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 417, 594, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 140, 1005, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 276, 1006, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 416, 1006, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 558, 1005, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 702, 1005, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 847, 1005, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 992, 1005, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 1134, 1005, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 1272, 1005, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 1413, 1005, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 1561, 1005, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 1389, 784, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 1531, 785, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 1131, 831, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 874, 884, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 949, 572, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 1192, 625, 153, 12, 500, 500, "grass", false);
        addObject(CharacterType.DECORATION, 86, 591, 191, 15, 500, 500, "grass", false);

        addObject(CharacterType.SCENERY, 797, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 696, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 595, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 493, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 391, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 289, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 187, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 85, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, -3, 1012, 60, 22, 500, 500, "leftmoss", false);
        addObject(CharacterType.SCENERY, -15, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 595, 600, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 56, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 158, 1012, 102, 22, 500, 500, "mossflat", false);

        addObject(CharacterType.SCENERY, 260, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 362, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 462, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 563, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 665, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 766, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 866, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 967, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1069, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1171, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1272, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1373, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1475, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1576, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1677, 1012, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1775, 1012, 60, 22, 500, 500, "rightmoss", false);
        addObject(CharacterType.SCENERY, 1387, 791, 60, 22, 500, 500, "leftmoss", false);
        addObject(CharacterType.SCENERY, 1446, 791, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1548, 791, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1649, 791, 102, 22, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1750, 791, 102, 22, 500, 500, "mossflat", false);

        addObject(CharacterType.SCENERY, -2, 784, 150, 264, 500, 500, "largestalagmite", false);
        addObject(CharacterType.SCENERY, 1650, 566, 150, 264, 500, 500, "largestalagmite", false);
        addObject(CharacterType.SCENERY, 1695, 860, 100, 176, 500, 500, "smallstalagmite", false);
        addObject(CharacterType.SCENERY, -7, 449, 100, 176, 500, 500, "smallstalagmite", false);
        addObject(CharacterType.SCENERY, -24, 171, 150, 264, 500, 500, "largestalactite", false);
        addObject(CharacterType.SCENERY, 877, 898, 150, 45, 500, 500, "fatplatform", false);
        addObject(CharacterType.SCENERY, 875, 890, 153, 33, 500, 500, "mosscurve", false);
        addObject(CharacterType.SCENERY, 1134, 847, 150, 45, 500, 500, "fatplatform", false);
        addObject(CharacterType.SCENERY, 1131, 837, 153, 33, 500, 500, "mossflat", false);
        addObject(CharacterType.SCENERY, 1194, 642, 150, 45, 500, 500, "fatplatform", false);
        addObject(CharacterType.SCENERY, 950, 588, 150, 45, 500, 500, "fatplatform", false);
        addObject(CharacterType.SCENERY, 1191, 632, 153, 33, 500, 500, "mosscurve", false);
        addObject(CharacterType.SCENERY, 948, 577, 153, 33, 500, 500, "mosscurve", false);

        addObject(CharacterType.SCENERY, 1634, 166, 200, 352, 500, 500, "largestalactite", false);
        addObject(CharacterType.SCENERY, 741, 960, 50, 11, 500, 500, "mosscurve", false);

        addObject(CharacterType.HEALINGSCENERY, 420, 990, 200, 44, 500, 500, "pool1", false);

        addObject(CharacterType.DECORATION, 181, 928, 39, 93, 500, 500, "grass1", false);
        //addObject(CharacterType.DECORATION, 1505, 979, 53, 43, 500, 500, "shroom1", false);
    }
        
        private void levelTwo(){
            startingPoint = new Point(-260,300);
            players.get(0).setXLocal(startingPoint.getX());
            players.get(0).setYLocal(startingPoint.getY());
            
            GAME_BACKGROUND = "cavebackground";
            GAME_FOREGROUND = "empty";
            GAME_SHADOW = "shade";
            GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
            GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
            GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            
            addObject(CharacterType.DECORATION, 405, 897, 191, 15, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 296, 899, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, -1, 900, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 589, 870, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 740, 840, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 641, 564, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 387, 478, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 868, 639, 191, 15, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1049, 638, 191, 15, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1212, 613, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1302, 641, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1470, 516, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1348, 900, 153, 12, 500, 500, "grass", false);
            
            addObject(CharacterType.SCENERY, 1, 906, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 150, 906, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 300, 906, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 448, 906, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 596, 906, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 743, 906, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1646, 906, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1497, 906, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1348, 906, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 743, 880, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 742, 855, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 594, 880, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 865, 653, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1013, 653, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1158, 653, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1302, 653, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 741, 847, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 590, 877, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 439, 906, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 286, 906, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 133, 906, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1, 906, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1367, 648, 90, 33, 500, 500, "rightmoss", false);
            addObject(CharacterType.SCENERY, 860, 648, 90, 33, 500, 500, "leftmoss", false);
            addObject(CharacterType.SCENERY, 950, 648, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1218, 648, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1102, 648, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1343, 906, 90, 33, 500, 500, "leftmoss", false);
            addObject(CharacterType.SCENERY, 1707, 906, 90, 33, 500, 500, "rightmoss", false);
            addObject(CharacterType.SCENERY, 1414, 906, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1561, 906, 153, 33, 500, 500, "mossflat", false);
            
            addObject(CharacterType.SCENERY, 862, 827, 30, 30, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 832, 827, 30, 30, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 802, 827, 30, 30, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 862, 797, 30, 30, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 832, 797, 30, 30, 500, 500, "squareplatform", false);
            
            addObject(CharacterType.SCENERY, 1674, 786, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1557, 786, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1498, 846, 60, 60, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1674, 677, 120, 120, 500, 500, "squareplatform", false);
            //addObject(CharacterType.SCENERY, 1614, 727, 60, 60, 500, 500, "squareplatform", false);
            //addObject(CharacterType.SCENERY, 1498, 843, 63, 13, 500, 500, "mossflat", false);
            //addObject(CharacterType.SCENERY, 1553, 783, 153, 33, 500, 500, "mosscurve", false);
            //addObject(CharacterType.SCENERY, 1677, 676, 153, 33, 500, 500, "mosscurve", false);
            
            addObject(CharacterType.SCENERY, 1471, 531, 150, 27, 500, 500, "platform", false);
            
            addObject(CharacterType.SCENERY, 1469, 522, 153, 33, 500, 500, "mosscurve", false);
            addObject(CharacterType.SCENERY, 1213, 626, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1211, 619, 153, 33, 500, 500, "mosscurve", false);
            addObject(CharacterType.SCENERY, 643, 582, 150, 45, 500, 500, "fatplatform", false);
            addObject(CharacterType.SCENERY, 641, 570, 153, 33, 500, 500, "mosscurve", false);
            addObject(CharacterType.SCENERY, 389, 493, 150, 45, 500, 500, "fatplatform", false);
            addObject(CharacterType.SCENERY, 386, 484, 153, 33, 500, 500, "mosscurve", false);
            addObject(CharacterType.DECORATION, 445, 403, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.DECORATION, 37, 823, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.DECORATION, 1528, 438, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.DECORATION, 1751, 592, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.DECORATION, 733, 484, 39, 93, 500, 500, "grass1", false);
            
            addObject(CharacterType.SCENERY, 804, 827, 63, 13, 500, 500, "mosscurve", false);
            addObject(CharacterType.SCENERY, 833, 797, 63, 13, 500, 500, "mosscurve", false);
            addObject(CharacterType.SCENERY, 1694, 676, 102, 22, 500, 500, "mosscurve", false);
            
            addObject(CharacterType.HEALINGSCENERY, 138, 890, 150, 33, 500, 500, "pool1", false);
        }
        
        private void levelThree(){
            startingPoint = new Point(48, 469);
            players.get(0).setXLocal(startingPoint.getX());
            players.get(0).setYLocal(startingPoint.getY());

            GAME_BACKGROUND = "cavebackground";
            GAME_FOREGROUND = "caveforeground";
            GAME_SHADOW = "cavebackgrounddark";
            GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
            GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
            GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);

            addObject(CharacterType.SCENERY, 740, 962, 52, 52, 500, 500, "squareplatform", false);

            addObject(CharacterType.SCENERY, 1444, 742, 60, 60, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1445, 739, 58, 13, 500, 500, "mosscurve", false);

            addObject(CharacterType.DECORATION, 555, 514, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.DECORATION, 628, 515, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.DECORATION, 585, 515, 39, 93, 500, 500, "grass2", false);

            addObject(CharacterType.DECORATION, 269, 593, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 562, 594, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 725, 595, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 417, 594, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 140, 1005, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 276, 1006, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 416, 1006, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 558, 1005, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 702, 1005, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 847, 1005, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 992, 1005, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1134, 1005, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1272, 1005, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1413, 1005, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1561, 1005, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1389, 784, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 1531, 785, 153, 12, 500, 500, "grass", false);
            //addObject(CharacterType.DECORATION, 1131, 831, 153, 12, 500, 500, "grass", false);
            //addObject(CharacterType.DECORATION, 874, 884, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 949, 572, 153, 12, 500, 500, "grass", false);
            //addObject(CharacterType.DECORATION, 1192, 625, 153, 12, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 86, 591, 191, 15, 500, 500, "grass", false);

            addObject(CharacterType.SCENERY, 797, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 696, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 595, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 493, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 391, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 289, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 187, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 85, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, -3, 1012, 60, 22, 500, 500, "leftmoss", false);
            addObject(CharacterType.SCENERY, -15, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 595, 600, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 56, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 158, 1012, 102, 22, 500, 500, "mossflat", false);

            addObject(CharacterType.SCENERY, 260, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 362, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 462, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 563, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 665, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 766, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 866, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 967, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1069, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1171, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1272, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1373, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1475, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1576, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1677, 1012, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1775, 1012, 60, 22, 500, 500, "rightmoss", false);
            addObject(CharacterType.SCENERY, 1387, 791, 60, 22, 500, 500, "leftmoss", false);
            addObject(CharacterType.SCENERY, 1446, 791, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1548, 791, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1649, 791, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1750, 791, 102, 22, 500, 500, "mossflat", false);

            addObject(CharacterType.SCENERY, -2, 784, 150, 264, 500, 500, "largestalagmite", false);
            addObject(CharacterType.SCENERY, 1650, 566, 150, 264, 500, 500, "largestalagmite", false);
            addObject(CharacterType.SCENERY, 1695, 860, 100, 176, 500, 500, "smallstalagmite", false);
            addObject(CharacterType.SCENERY, -7, 449, 100, 176, 500, 500, "smallstalagmite", false);
            addObject(CharacterType.SCENERY, -24, 171, 150, 264, 500, 500, "largestalactite", false);
            //addObject(CharacterType.SCENERY, 877, 898, 150, 45, 500, 500, "fatplatform", false);
            //addObject(CharacterType.SCENERY, 875, 890, 153, 33, 500, 500, "mosscurve", false);
            //addObject(CharacterType.SCENERY, 1134, 847, 150, 45, 500, 500, "fatplatform", false);
            //addObject(CharacterType.SCENERY, 1131, 837, 153, 33, 500, 500, "mossflat", false);
            //addObject(CharacterType.SCENERY, 1194, 642, 150, 45, 500, 500, "fatplatform", false);
            addObject(CharacterType.SCENERY, 950, 588, 150, 45, 500, 500, "fatplatform", false);
            //addObject(CharacterType.SCENERY, 1191, 632, 153, 33, 500, 500, "mosscurve", false);
            addObject(CharacterType.SCENERY, 948, 577, 153, 33, 500, 500, "mosscurve", false);

            addObject(CharacterType.SCENERY, 1634, 166, 200, 352, 500, 500, "largestalactite", false);
            addObject(CharacterType.SCENERY, 741, 960, 50, 11, 500, 500, "mosscurve", false);

            addObject(CharacterType.HEALINGSCENERY, 420, 990, 200, 44, 500, 500, "pool1", false);

            addObject(CharacterType.DECORATION, 181, 928, 39, 93, 500, 500, "grass1", false);
            //addObject(CharacterType.DECORATION, 1505, 979, 53, 43, 500, 500, "shroom1", false);
            
            addObject(CharacterType.THREAT, 1330, 972, 20, 50, 500, 800, "fire1", false);
            addObject(CharacterType.THREAT, 1306, 972, 20, 50, 500, 800, "fire3", false);
            addObject(CharacterType.THREAT, 1281, 973, 20, 50, 500, 800, "fire2", false);
            addObject(CharacterType.THREAT, 1463, 695, 20, 50, 500, 900, "fire2", false);
        }
        
        private void levelFour(){
            startingPoint = new Point(-260,300);
            players.get(0).setXLocal(startingPoint.getX());
            players.get(0).setYLocal(startingPoint.getY());
            
            GAME_BACKGROUND = "halficebackground";
            GAME_FOREGROUND = "shade";
            GAME_SHADOW = "shade";
            GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
            GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
            GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            
            addObject(CharacterType.SCENERY, 7, 950, 600, 108, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 537, 951, 600, 108, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1133, 951, 400, 40, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1400, 952, 400, 40, 500, 500, "iceplatform", false);
            
            addObject(CharacterType.DECORATION, 6, 937, 306, 24, 500, 500, "grass", false);
            addObject(CharacterType.DECORATION, 831, 531, 191, 15, 500, 500, "grass", false);

            addObject(CharacterType.PERMEABLESCENERY, 1263, 770, 68, 212, 500, 500, "iceright", false);
            addObject(CharacterType.THREAT, 1106, 918, 30, 75, 500, 100, "ice1", false);
            addObject(CharacterType.THREAT, 1076, 918, 30, 75, 500, 100, "ice2", false);
            addObject(CharacterType.THREAT, 1047, 918, 30, 75, 500, 100, "ice3", false);
            addObject(CharacterType.THREAT, 1018, 918, 30, 75, 500, 100, "ice2", false);
            addObject(CharacterType.THREAT, 989, 918, 30, 75, 500, 100, "ice1", false);
            addObject(CharacterType.THREAT, 961, 918, 30, 75, 500, 100, "ice2", false);
            addObject(CharacterType.THREAT, 932, 918, 30, 75, 500, 100, "ice3", false);
            addObject(CharacterType.THREAT, 903, 918, 30, 75, 500, 100, "ice2", false);
            addObject(CharacterType.THREAT, 874, 918, 30, 75, 500, 100, "ice1", false);
            addObject(CharacterType.THREAT, 845, 918, 30, 75, 500, 100, "ice2", false);
            addObject(CharacterType.THREAT, 816, 918, 30, 75, 500, 100, "ice3", false);
            addObject(CharacterType.THREAT, 787, 918, 30, 75, 500, 100, "ice2", false);
            addObject(CharacterType.THREAT, 758, 918, 30, 75, 500, 100, "ice1", false);
            
            addObject(CharacterType.SCENERY, 8, 950, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 138, 950, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 290, 950, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 441, 950, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 593, 950, 153, 33, 500, 500, "mossflat", false);
            
            addObject(CharacterType.PERMEABLESCENERY, 473, 770, 68, 212, 500, 500, "rockright", false);

            addObject(CharacterType.SCENERY, 652, 210, 800, 80, 500, 500, "iceplatform", false);
            
            addObject(CharacterType.HEALINGSCENERY, 98, 927, 200, 44, 500, 500, "pool1", false);
            addObject(CharacterType.SCENERY, 1655, 890, 150, 15, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1414, 800, 150, 15, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1655, 710, 150, 15, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1414, 620, 150, 15, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1655, 530, 150, 15, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1414, 440, 150, 15, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1264, 425, 150, 15, 500, 500, "iceplatform", false);
            
            addObject(CharacterType.SCENERY, 826, 540, 200, 60, 500, 500, "fatplatform", false);
            addObject(CharacterType.DECORATION, 837, 455, 39, 93, 500, 500, "grass2", false);
            addObject(CharacterType.SCENERY, 749, 791, 400, 40, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 822, 539, 204, 44, 500, 500, "mosscurve", false);
        }
        
        private void levelFive(){
            startingPoint = new Point(-260,300);
            players.get(0).setXLocal(startingPoint.getX());
            players.get(0).setYLocal(startingPoint.getY());
            LEVEL_TEMPERATURE = 100;
            
            GAME_BACKGROUND = "icebackground";
            GAME_FOREGROUND = "shade";
            GAME_SHADOW = "shade";
            GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
            GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
            GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            
            addObject(CharacterType.PERMEABLESCENERY, 13, 763, 68, 212, 500, 500, "iceleft", false);
            addObject(CharacterType.SCENERY, 1199, 944, 600, 60, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 4, 949, 800, 80, 500, 500, "iceplatform", false);
            
            addObject(CharacterType.PERMEABLESCENERY, 1744, 810, 51, 159, 500, 500, "iceright", false);
            
            addObject(CharacterType.SCENERY, 713, 1125, 90, 90, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 713, 1037, 90, 90, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 713, 948, 90, 90, 500, 500, "squareplatform", false);
            addObject(CharacterType.THREAT, 775, 897, 25, 62, 500, 800, "fire1", false);
            addObject(CharacterType.THREAT, 745, 897, 25, 62, 500, 800, "fire2", false);
            addObject(CharacterType.THREAT, 715, 897, 25, 62, 500, 800, "fire3", false);
            
            
            addObject(CharacterType.SCENERY, 701, 555, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 897, 704, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1094, 704, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 796, 704, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1535, 699, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1197, 926, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1176, 907, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1154, 887, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1118, 868, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1081, 850, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 1040, 830, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 993, 811, 200, 20, 500, 500, "iceplatform", false);
            
            addObject(CharacterType.SCENERY, 504, 555, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 307, 555, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.PERMEABLESCENERY, 787, 414, 51, 159, 500, 500, "iceleft", false);
            addObject(CharacterType.SCENERY, 1598, 699, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.PERMEABLESCENERY, 1764, 608, 34, 106, 500, 500, "iceright", false);
            
            addObject(CharacterType.SCENERY, 1713, 698, 22, 22, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1692, 698, 22, 22, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1671, 698, 22, 22, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1652, 698, 22, 22, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1647, 689, 90, 33, 500, 500, "rightmoss", false);
            addObject(CharacterType.SCENERY, 714, 947, 89, 19, 500, 500, "mosscurve", false);
            
            addObject(CharacterType.DECORATION, 1674, 606, 39, 93, 500, 500, "grass2", false);

        }
        private void levelSix(){
            startingPoint = new Point(-220,1700);
            players.get(0).setXLocal(startingPoint.getX());
            players.get(0).setYLocal(startingPoint.getY());
            LEVEL_TEMPERATURE = 500;
            
            GAME_BACKGROUND = "largebackground";
            GAME_FOREGROUND = "shade";
            GAME_SHADOW = "shade";
            GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 3600, 2485);
            GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 3600, 2485);
            GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 3600, 2485);
            
            /*---------------------------Puzzle 1------------------------------------------------*/
            addObject(CharacterType.SCENERY, 2, 2294, 800, 144, 500, 500, "platform", false);
            
            addObject(CharacterType.SCENERY, 2, 220, 120, 2080, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 5, 235, 800, 144, 500, 500, "platform", false);
            
            addObject(CharacterType.SCENERY, 678, 2050, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 563, 2050, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 445, 2050, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 329, 755, 120, 1480, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 385, 688, 200, 36, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 329, 652, 800, 144, 500, 500, "platform", false);
            
            addObject(CharacterType.SCENERY, 1037, 258, 120, 540, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 919, 259, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 801, 259, 120, 120, 500, 500, "squareplatform", false);

            addObject(CharacterType.SCENERY, 291, 2140, 100, 18, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 52, 1977, 100, 18, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 296, 1789, 100, 18, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 59, 1639, 100, 18, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 288, 1455, 100, 18, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 55, 1248, 100, 18, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 300, 1057, 100, 18, 500, 500, "platform", false);
            
            addObject(CharacterType.THREAT, 305, 1014, 20, 50, 500, 900, "fire2", false);
            addObject(CharacterType.THREAT, 128, 1205, 20, 50, 500, 900, "fire3", false);
            addObject(CharacterType.THREAT, 294, 1414, 20, 50, 500, 900, "fire1", false);
            addObject(CharacterType.THREAT, 131, 1600, 20, 50, 500, 900, "fire3", false);
            addObject(CharacterType.THREAT, 300, 1750, 20, 50, 500, 900, "fire2", false);
            addObject(CharacterType.THREAT, 127, 1938, 20, 50, 500, 900, "fire2", false);
            addObject(CharacterType.THREAT, 296, 2104, 20, 50, 500, 900, "fire2", false);
            addObject(CharacterType.THREAT, 130, 2263, 20, 50, 500, 900, "fire3", false);
            addObject(CharacterType.THREAT, 155, 2264, 20, 50, 500, 900, "fire2", false);
            addObject(CharacterType.SCENERY, 60, 799, 100, 18, 500, 500, "platform", false);
            addObject(CharacterType.THREAT, 133, 756, 20, 50, 500, 900, "fire2", false);

            addObject(CharacterType.THREAT, 560, 622, 20, 50, 500, 0, "ice1", false);
            addObject(CharacterType.THREAT, 540, 622, 20, 50, 500, 0, "ice2", false);
            addObject(CharacterType.THREAT, 522, 622, 20, 50, 500, 0, "ice3", false);
            addObject(CharacterType.THREAT, 503, 622, 20, 50, 500, 0, "ice2", false);
            addObject(CharacterType.THREAT, 484, 622, 20, 50, 500, 0, "ice1", false);
            addObject(CharacterType.THREAT, 465, 622, 20, 50, 500, 0, "ice2", false);
            addObject(CharacterType.THREAT, 445, 622, 20, 50, 500, 0, "ice3", false);
            addObject(CharacterType.THREAT, 427, 622, 20, 50, 500, 0, "ice2", false);
            addObject(CharacterType.THREAT, 407, 622, 20, 50, 500, 0, "ice1", false);
            
            addObject(CharacterType.PERMEABLESCENERY, 678, 517, 51, 159, 500, 500, "rockright", false);
            addObject(CharacterType.DECORATION, 947, 568, 39, 93, 500, 500, "grass2", false);
            addObject(CharacterType.SCENERY, 286, 652, 100, 18, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1053, 2291, 800, 144, 500, 500, "platform", false);
            
            addObject(CharacterType.SCENERY, 886, 650, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 733, 650, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 580, 651, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 447, 652, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 284, 651, 76, 16, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 335, 651, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 349, 2294, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 196, 2294, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 502, 2294, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 654, 2293, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 108, 2293, 90, 33, 500, 500, "leftmoss", false);
            
            addObject(CharacterType.DECORATION, 847, 616, 53, 43, 500, 500, "shroom1", false);
            addObject(CharacterType.HEALINGSCENERY, 200, 2278, 150, 33, 500, 500, "pool1", false);
            
            addObject(CharacterType.HEALINGSCENERY, 1349, 2275, 150, 33, 500, 500, "pool1", false);
            addObject(CharacterType.PERMEABLESCENERY, 1509, 2211, 29, 92, 500, 500, "rockright", false);
            /*--------------------------Puzzle 2----------------------------------------------------*/
            addObject(CharacterType.SCENERY, 1836, 2030, 800, 144, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1036, 2030, 800, 144, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 2568, 2291, 800, 144, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 2154, 2291, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2427, 2030, 800, 144, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 3363, 2289, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 3481, 2289, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 3357, 2399, 200, 36, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 3400, 2399, 200, 36, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 3510, 201, 90, 2090, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 3107, 712, 120, 1360, 500, 500, "squareplatform", false);
            
            /*----------------stairs up----------------------------------------------------------------*/
            addObject(CharacterType.SCENERY, 3398, 2180, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 3321, 2223, 90, 90, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 3273, 2256, 60, 60, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 3454, 2128, 60, 60, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 3165, 2053, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3400, 1963, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3165, 1873, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3400, 1783, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3165, 1693, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3400, 1603, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3165, 1513, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3400, 1423, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3165, 1333, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3400, 1243, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3165, 1153, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3400, 1063, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3165, 973, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3400, 883, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3165, 793, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3400, 703, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3165, 620, 200, 20, 500, 500, "iceplatform", false);
            addObject(CharacterType.SCENERY, 3450, 620, 150, 20, 500, 500, "iceplatform", false);
            
            addObject(CharacterType.THREAT, 3489, 587, 20, 50, 500, 100, "ice1", false);
            addObject(CharacterType.THREAT, 3471, 587, 20, 50, 500, 100, "ice1", false);
            addObject(CharacterType.THREAT, 3451, 587, 20, 50, 500, 100, "ice1", false);
            addObject(CharacterType.PERMEABLESCENERY, 3402, 625, 29, 92, 500, 500, "iceright", false);
            /*-------------------------end stairs-----------------------------------------------------*/                               
            
            addObject(CharacterType.SCENERY, 2991, 618, 240, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2991, 418, 240, 120, 500, 500, "squareplatform", false);
            
            addObject(CharacterType.SCENERY, 2865, 711, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 2651, 331, 240, 90, 500, 500, "squareplatform", false);
            
            addObject(CharacterType.SCENERY, 2651, 618, 240, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2651, 418, 240, 120, 500, 500, "squareplatform", false);
            
            addObject(CharacterType.SCENERY, 2311, 618, 240, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2311, 418, 240, 120, 500, 500, "squareplatform", false);
            
            addObject(CharacterType.SCENERY, 2525, 711, 150, 27, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 2311, 241, 240, 180, 500, 500, "squareplatform", false);
            
            addObject(CharacterType.SCENERY, 2651, 298, 630, 36, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 2312, 208, 1020, 36, 500, 500, "platform", false);
            
            addObject(CharacterType.PERMEABLESCENERY, 2954, 618, 34, 106, 500, 500, "rockright", false);
            addObject(CharacterType.PERMEABLESCENERY, 2613, 619, 34, 106, 500, 500, "rockright", false);
            addObject(CharacterType.PERMEABLESCENERY, 2551, 620, 34, 106, 500, 500, "rockleft", false);
            addObject(CharacterType.PERMEABLESCENERY, 2893, 619, 34, 106, 500, 500, "rockleft", false);
            addObject(CharacterType.THREAT, 2590, 665, 20, 50, 500, 900, "fire3", false);
            addObject(CharacterType.THREAT, 2930, 666, 20, 50, 500, 900, "fire3", false);
            addObject(CharacterType.SCENERY, 2991, 532, 30, 30, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2861, 532, 30, 30, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2652, 532, 30, 30, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2521, 532, 30, 30, 500, 500, "squareplatform", false);
            
            addObject(CharacterType.SCENERY, 2991, 416, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 3140, 416, 90, 33, 500, 500, "rightmoss", false);
            addObject(CharacterType.DECORATION, 3183, 335, 39, 93, 500, 500, "grass2", false);
            addObject(CharacterType.SCENERY, 3126, 299, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2973, 298, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2820, 298, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2668, 298, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2644, 298, 90, 33, 500, 500, "leftmoss", false);
            addObject(CharacterType.SCENERY, 3331, 209, 200, 36, 500, 500, "platform", false);
            addObject(CharacterType.PERMEABLESCENERY, 1036, 1894, 51, 159, 500, 500, "rockleft", false);
            addObject(CharacterType.PERMEABLESCENERY, 748, 1911, 51, 159, 500, 500, "rockright", false);
            addObject(CharacterType.SCENERY, 2311, 206, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2463, 206, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2616, 206, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2768, 206, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2920, 206, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 3073, 206, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 3225, 206, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 3377, 206, 153, 33, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 3524, 207, 90, 33, 500, 500, "rightmoss", false);
            addObject(CharacterType.DECORATION, 3227, 217, 39, 93, 500, 500, "grass1", false);
            
            /*-----------------------Tree god-------------------------------------------*/
            addObject(CharacterType.SCENERY, 1825, 1356, 200, 36, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 1475, 1317, 800, 144, 500, 500, "platform", false);
            addObject(CharacterType.SCENERY, 2155, 1201, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1474, 1200, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2099, 1239, 90, 90, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1568, 1232, 90, 90, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2057, 1273, 60, 60, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1640, 1278, 60, 60, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1827, 1459, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1947, 1459, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1709, 1460, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1919, 1578, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1739, 1578, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1833, 1577, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1879, 1696, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1774, 1695, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1826, 1747, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 2023, 1395, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1634, 1387, 120, 120, 500, 500, "squareplatform", false);
            addObject(CharacterType.SCENERY, 1854, 1312, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2104, 1237, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2061, 1271, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1593, 1275, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1550, 1230, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1469, 1198, 60, 22, 500, 500, "leftmoss", false);
            addObject(CharacterType.SCENERY, 1526, 1198, 60, 22, 500, 500, "rightmoss", false);
            addObject(CharacterType.SCENERY, 2156, 1201, 60, 22, 500, 500, "leftmoss", false);
            addObject(CharacterType.SCENERY, 2213, 1202, 60, 22, 500, 500, "rightmoss", false);
            addObject(CharacterType.SCENERY, 1681, 1312, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.PERMEABLESCENERY, 1478, 1061, 51, 159, 500, 500, "rockleft", false);
            addObject(CharacterType.PERMEABLESCENERY, 2221, 1066, 51, 159, 500, 500, "rockright", false);
            addObject(CharacterType.DECORATION, 2108, 1152, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.DECORATION, 1603, 1143, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.DECORATION, 1542, 1114, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.DECORATION, 2169, 1116, 39, 93, 500, 500, "grass1", false);
            addObject(CharacterType.SCENERY, 1037, 2023, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1241, 2023, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1443, 2024, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1647, 2025, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1851, 2025, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2053, 2025, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2256, 2026, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2460, 2026, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2664, 2026, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2866, 2026, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 593, 2047, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 446, 2046, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1053, 2289, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1255, 2290, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1458, 2290, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 1661, 2291, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2163, 2292, 102, 22, 500, 500, "mossflat", false);
            addObject(CharacterType.DECORATION, 2191, 2210, 39, 93, 500, 500, "grass2", false);
            addObject(CharacterType.SCENERY, 2569, 2290, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2772, 2291, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 2975, 2290, 204, 44, 500, 500, "mossflat", false);
            addObject(CharacterType.SCENERY, 3174, 2290, 120, 44, 500, 500, "rightmoss", false);
            
            addObject(CharacterType.TREE, 1865, 1269, 29, 50, 500, 500, "tree1", false);
            
        }
        
        private void updateBackground(float xlocx, float ylocy){
            if (level == 1){
                GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
                GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
                GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            } else if (level == 2){
                GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
                GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
                GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            } else if (level == 3){
                GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
                GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
                GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            } else if (level == 4){
                GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
                GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
                GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            } else if (level == 5){
                GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
                GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
                GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            } else if (level == 6){
                GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 3600, 2485);
                GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 3600, 2485);
                GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 3600, 2485);
            }
        }
}
