/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package drip;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;

/**
 *
 * @author Dan Ford
 */
public class Player extends DefaultCharacter {

    public boolean LEVEL_EDITOR = false;
    /**
     * Movement velocity in a direction
     */
    float L = 0, R = 0, U = 0, D = 0;
    
    /**
     * Prevents jumping when true
     */
    boolean fall = false;
    
    /**
     * Set screen bounds
     */
    int LEFT_BOUND = 0;
    int RIGHT_BOUND = 736;
    int CEILING_BOUND = 0;
    int FLOOR_BOUND = 600;
    private int xlocal = 0;
    private int ylocal = 0;
    
    /**
     * Set default gravity value
     */
    float GRAVITY = 0.04f;
    boolean gravityWorking;
    
    /**
     * Creates player variables, given value in state test
     */
    float ACCEL_SPEED, DECEL_SPEED, DECEL_AMOUNT, GRAVITY_AMOUNT, JUMP_HEIGHT, 
            MAX_SPEED, MAX_FALL_SPEED, MAX_CEILING_CLING, CEILING_CLING, CLING_REDUC,
            JUMP_TIMER, JUMP_COOLDOWN;
    
    Temp temp = Temp.WATER;
    
    public enum Temp {
        WATER(0), ICE(1), GAS(2);
        
        private int value;
        
        private Temp(int value) {
            this.value = value;
        }
        
        public static Temp fromValue(int value){
            switch(value){
                case 0:
                    return Temp.WATER;
                case 1:
                    return Temp.ICE;
                case 2:
                    return Temp.GAS;
                default: 
                    return Temp.WATER;
            }
        }
    }
    
    public Player(Point p, int width, int height) {
        super(p, width, height);
        xlocal = p.getX();
        ylocal = p.getY();

    }
    
    public Player(Point p, int width, int height,  int health) {
        super(p, width, height, health);
        xlocal = p.getX();
        ylocal = p.getY();
    }
        
    public Player(int x, int y, int width, int height,  int health) {
        super(x, y, width, height, health);
        xlocal = x;
        ylocal = y;
    }

    public Player(int x, int y, int width, int height,  int health, State state) {
        super(x, y, width, height, health, state);
        xlocal = x;
        ylocal = y;
    }
    
    public Player(int x, int y, int width, int height,  int health, String tex) {
        super(x, y, width, height, health, tex);
        xlocal = x;
        ylocal = y;
    }
    
    public Player(int x, int y, int width, int height,  int health, int temper, String tex) {
        super(x, y, width, height, health, temper, tex);
        xlocal = x;
        ylocal = y;
    }
    
    public Player(int x, int y, int width, int height,  int health, int temperature, String tex, boolean edit) {
        super(x, y, width, height, health, temperature, tex, edit);
        xlocal = 100;
        ylocal = 400;
    }
    
    public Player(int x, int y, int width, int height,  int health, int temperature, String tex, boolean edit, boolean leveleditor) {
        super(x, y, width, height, health, temperature, tex, edit);
        xlocal = 100;
        ylocal = 400;
        LEVEL_EDITOR = leveleditor;
    }
    
    @Override
    public void update(int delta, String collisionDirection){
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
        controls(delta, collisionDirection);
        checkTemp();
    }

    private void checkTemp() {
        if (getTemperature() < 300){
            temp = Temp.ICE;
            setTex("icey");
        } else if (getTemperature() < 700){
            temp = Temp.WATER;
            if (getTexture().equals("cloudyR")){
                setTex("drippyR");
                D = 1;
                fall = true;
                gravityWorking = true;
            }  else if (getTexture().equals("cloudyL") || getTexture().equals("icey")) {
                setTex("drippyL");
                D = 1;
                fall = true;
                gravityWorking = true;
            }
            if (L > R){
                setTex("drippyL");
            } else if (R > L) {
                setTex("drippyR");
            } else {
            
            }
        } else {
            temp = Temp.GAS;
            if (getTexture().equals("drippyR")){
                setTex("cloudyR");       
                U = 15;
            }  else if (getTexture().equals("drippyL") || getTexture().equals("icey")) {
                setTex("cloudyL");
                U = 15;
            }
            if (L > R){
                setTex("cloudyL");
            } else if (R > L) {
                setTex("cloudyR");
            } else {
              
            }
        }
    }
    
    private void controls(int delta, String collisionDirection) {
        //position.setX(Mouse.getX()-32);
        //position.setY(600-Mouse.getY()-32);
        if (collisionDirection.contains("L")){
                L = 0;
                //JUMP_TIMER = 0;
        }
        if (collisionDirection.contains("R")){
                R = 0;
                //JUMP_TIMER = 0;
        }
        if (collisionDirection.contains("T")){
                if (gravityWorking){
                    U = 0;
                } else {
                    
                }
                D = 0;
                fall = true;
        }
        if (collisionDirection.contains("B")){
                D = 0;
                if (temp != Temp.GAS){
                    U = 0;
                }
                CEILING_CLING = MAX_CEILING_CLING;
                JUMP_TIMER = 0;
                JUMP_COOLDOWN--;
                fall = false;
        }
        if (collisionDirection.contains("A")){
                fall = false;
                JUMP_COOLDOWN--;
                JUMP_TIMER = 0;
        }
        if (collisionDirection.contains("G")){
                JUMP_TIMER = 0;
        }
        if (!collisionDirection.contains("B")&&!collisionDirection.contains("A")){
            JUMP_COOLDOWN= 5;
        }
        if (temp == Temp.ICE){
            ACCEL_SPEED = 0.008f;
            DECEL_SPEED = 0.008f;
            JUMP_HEIGHT = 15;
            GRAVITY_AMOUNT = 2;
            gravityWorking = true;
            GRAVITY = 0.04f;
            DECEL_AMOUNT = .25f;
            MAX_SPEED = 100;
            MAX_FALL_SPEED = 30;
            MAX_CEILING_CLING = 0;
            CLING_REDUC = 1;
        } else if (temp == Temp.GAS){
            ACCEL_SPEED = 0.02f;
            DECEL_SPEED = 0.02f;
            JUMP_HEIGHT = 15;
            GRAVITY_AMOUNT = 1;
            gravityWorking = false;
            GRAVITY = 0.04f;
            DECEL_AMOUNT = 1;
            MAX_SPEED = 20;
            MAX_FALL_SPEED = 30;
            MAX_CEILING_CLING = 0;
            CLING_REDUC = 1;
        } else {
            ACCEL_SPEED = 0.02f;
            DECEL_SPEED = 0.02f;
            JUMP_HEIGHT = 22;
            GRAVITY_AMOUNT = 2;
            gravityWorking = true;
            GRAVITY = 0.04f;
            DECEL_AMOUNT = 2;
            MAX_SPEED = 20;
            MAX_FALL_SPEED = 30;
            MAX_CEILING_CLING = 100;
            CLING_REDUC = 1f;
        }
        try {
            Keyboard.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (L > MAX_SPEED){
            L = MAX_SPEED;
        } else if (R > MAX_SPEED){
            R = MAX_SPEED;
        }
        if (D > MAX_FALL_SPEED){
            D = MAX_FALL_SPEED;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            CEILING_CLING = 0;
        }
        if (JUMP_TIMER > 5 && temp != Temp.GAS){
            fall = true;
        }
        if(!collisionDirection.contains("L")){
            if (Keyboard.isKeyDown(Keyboard.KEY_A)){
                if (temp==Temp.WATER){
                    setTex("drippyL");
                } else if (temp==Temp.GAS){
                    setTex("cloudyL");
                }
                if (R > 0){
                    R-=DECEL_AMOUNT;
                    incXLocal((int)((DECEL_SPEED * R) * delta));
                }else if (R <= 0){
                    R=0;
                    if (L < MAX_SPEED){
                        L++;
                    }
                    decXLocal((int)((ACCEL_SPEED * L) * delta));
                }
            }
        }
        if(!collisionDirection.contains("R")){
            if (Keyboard.isKeyDown(Keyboard.KEY_D)){
                if (temp ==Temp.WATER){
                    setTex("drippyR");
                } else if (temp==Temp.GAS){
                    setTex("cloudyR");
                }
                if (L > 0){
                    L-=DECEL_AMOUNT;
                    decXLocal((int)((DECEL_SPEED * L) * delta));
                }else if (L <= 0){
                    L=0;
                    if (R <= MAX_SPEED){
                        R++;
                    }
                    incXLocal((int)((ACCEL_SPEED * R) * delta));
                }
            } 
        }
        if (!Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)){
            if(!collisionDirection.contains("L")){
                if (L > 0){
                    L-=DECEL_AMOUNT;
                    if(L < 0)
                        L=0;
                    decXLocal((int)((DECEL_SPEED * L) * delta));
                }
            } else if (collisionDirection.contains("L")){
                L = 0;
            }
            if(!collisionDirection.contains("R")){
                if (R > 0){
                    R-=DECEL_AMOUNT;
                    if(R < 0)
                        R=0;
                    incXLocal((int)((DECEL_SPEED * R) * delta));
                }
            } else if (collisionDirection.contains("R")){
                R = 0;
            }
        }
        if (LEVEL_EDITOR){
            if (Keyboard.isKeyDown(Keyboard.KEY_W)){
                if (D > 0){
                    D-=DECEL_AMOUNT;
                    incYLocal((int)((DECEL_SPEED * D) * delta));
                }else if (D <= 0){
                    D=0;
                    if (U < MAX_SPEED){
                        U++;
                    }
                    decYLocal((int)((ACCEL_SPEED * U) * delta));
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)){
                if (U > 0){
                    U-=DECEL_AMOUNT;
                    decYLocal((int)((DECEL_SPEED * U) * delta));
                }else if (U <= 0){
                    U=0;
                    if (D < MAX_FALL_SPEED){
                        D++;
                    }
                    incYLocal((int)((ACCEL_SPEED * D) * delta));
                }
            }
            if (!Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S)){
                if (D > 0){
                    D-=DECEL_AMOUNT;
                    incYLocal((int)((DECEL_SPEED * D) * delta));
                }else if (U > 0){
                    U-=DECEL_AMOUNT;
                    decYLocal((int)((DECEL_SPEED * U) * delta));
                }
            }
        } else if (!LEVEL_EDITOR){
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !fall && !collisionDirection.contains("T") && (JUMP_COOLDOWN<=2 || temp == Temp.GAS)){
                U=JUMP_HEIGHT;
                fall=true;
                decYLocal((int)((GRAVITY * U) * delta));
            } else if ((fall && U > 0 && !collisionDirection.contains("T")) || (temp == Temp.GAS && U > 0 && !collisionDirection.contains("T"))){
                U-=GRAVITY_AMOUNT;
                decYLocal((int)((GRAVITY * U) * delta));
            } else if (U <= 0){
                if(((temp == Temp.ICE || temp == Temp.WATER) && !collisionDirection.contains("B")) || (temp == Temp.GAS)){
                    if (U < 0){
                        U = 0;
                    }
                    JUMP_TIMER++;
                    //System.out.println("Falling.");
                    if (gravityWorking && collisionDirection.contains("T") && CEILING_CLING > 0){
                        CEILING_CLING -= CLING_REDUC;
                    } else if (gravityWorking && (!collisionDirection.contains("T") || (CEILING_CLING <=0 && collisionDirection.contains("T")))){
                        D+=GRAVITY_AMOUNT;
                        incYLocal((int)((GRAVITY * D) * delta));
                    } else {
                       U+=GRAVITY_AMOUNT;
                       fall = false;
                       decYLocal((int)((GRAVITY * U) * delta));
                    }
                }
            }
        }
        //System.out.println("Player: U-" + U + ", D-" + D + ", L-" + L + ", R-" + R + ".");
    }
    
    public void setXLocal(int amount){
        xlocal = amount;
    }
    
    public void incXLocal(int amount){
        xlocal += amount;
    }
    
    public void decXLocal(int amount){
        xlocal -= amount;
    }
    
    public int getXLocal(){
        return xlocal;
    }
        
    public void setYLocal(int amount){
        ylocal = amount;
    }
    
    public void incYLocal(int amount){
        ylocal += amount;
    }
    
    public void decYLocal(int amount){
        ylocal -= amount;
    }
    
    public int getYLocal(){
        return ylocal;
    }
    
    public Temp getTemp(){
        return temp;        
    }
}
